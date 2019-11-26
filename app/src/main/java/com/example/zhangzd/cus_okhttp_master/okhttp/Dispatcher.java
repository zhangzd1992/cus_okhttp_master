package com.example.zhangzd.cus_okhttp_master.okhttp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-22 15:38
 */
public class Dispatcher {
    private int maxRequests = 64; //最大执行请求数
    private int maxRequestsPerHost = 5; //同一服务器最大请求书


    /** Running asynchronous calls. Includes canceled calls that haven't finished yet. */
    private final Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque<>();

    /** Ready async calls in the order they'll be run. */
    private final Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque<>();



    public void enqueue(RealCall.AsyncCall call) {
        //将任务添加到执行队列或者是等待队列
        if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }



    }

    private synchronized Executor executorService() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(false);  //设置不是伴随线程
                thread.setName("自定义OKHTTP线程");
                return thread;
            }
        });
    }

    /**
     * @Description 获取请求队列中与传入任务主机地址相同的请求的个数
     * @param call  请求
     * @return      请求队列中与传入任务主机地址相同的请求的个数
     */
    private int runningCallsForHost(RealCall.AsyncCall call) {
        int count = 0;
        for (RealCall.AsyncCall c : runningAsyncCalls) {
           if (c.host().equals(call.host())) {
               count ++;
           }
        }
        return count;
    }

    public void finished(RealCall.AsyncCall asyncCall) {
        //从执行队队列中移除当前任务
        runningAsyncCalls.remove(asyncCall);

        for (Iterator<RealCall.AsyncCall> i = readyAsyncCalls.iterator(); i.hasNext(); ) {
            RealCall.AsyncCall call = i.next();

            if (runningCallsForHost(call) < maxRequestsPerHost) {
                i.remove();
                runningAsyncCalls.add(call);
                executorService().execute(call);
            }

            if (runningAsyncCalls.size() >= maxRequests) return; // Reached max capacity.
        }


    }
}
