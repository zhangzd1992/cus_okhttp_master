package com.example.zhangzd.cus_okhttp_master.okhttp.connectionPool;

import android.util.Log;

import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 连接池
 * @Author: zhangzd
 * @CreateDate: 2019-11-25 17:23
 */
public class ConnectionPool {
    private static ConnectionPool instance;
    private final String TAG = "OKHTTP";
    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    private Deque<HttpConnetion> connectionCache = new ArrayDeque<>();
    private boolean isStartClear = false;   //标记是否已经开启了清理任务
    //清理任务
    private Runnable clearRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (ConnectionPool.this) {
                while (true) {
                    long maxSaveTime = findIdealConnection(System.currentTimeMillis());
                    if (maxSaveTime == -1) {
                        //不存在闲置任务，直接退出任务
                        return;
                    }
                    try {
                        //等待最大闲置时间之后，在进行清理工作，防止进行频繁无效的清理操作
                        ConnectionPool.this.wait(maxSaveTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    private long findIdealConnection(long currentTime) {
        Iterator<HttpConnetion> iterator = connectionCache.iterator();
        long maxSaveTime = -1;
        while (iterator.hasNext()) {
            HttpConnetion next = iterator.next();
            long saveTime = currentTime - next.getAddTime();
            //闲置存活时间
            int keepAlive = 60;
            if (saveTime / 1000  >= keepAlive) {
                //当前已存在时间大于最大存活时间，则直接移除掉
                iterator.remove();
                continue;
            }
            if (maxSaveTime < saveTime) {
                maxSaveTime = saveTime;
            }
        }


        if (maxSaveTime > 0 ) {
            return maxSaveTime;
        }
        return -1;
    }

    private Executor threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(false);
                    return thread;
                }
            });


    public synchronized void addConnection(Socket socket) {
        HttpConnetion httpConnetion = new HttpConnetion();
        httpConnetion.setSocket(socket);
        httpConnetion.setAddTime(System.currentTimeMillis());
        connectionCache.add(httpConnetion);
        if (!isStartClear) {
            isStartClear = true;
            threadPool.execute(clearRunnable);
        }

    }


    /**
     * 查询连接缓冲池中是存在可用的socket连接
     * @param host
     * @param port
     */
    public Socket queryConnect(String host, int port) {

        for (HttpConnetion next : connectionCache) {
            if (next.isOneConnection(host, port)) {
                Log.e(TAG,"连接池中存在可用的socket连接");
                return next.getSocket();
            }
        }

        return null;
    }
}
