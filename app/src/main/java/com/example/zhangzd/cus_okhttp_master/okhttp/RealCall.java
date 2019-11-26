package com.example.zhangzd.cus_okhttp_master.okhttp;




import com.example.zhangzd.cus_okhttp_master.okhttp.interceptor.ConnectInterceptor;
import com.example.zhangzd.cus_okhttp_master.okhttp.interceptor.Interceptor;
import com.example.zhangzd.cus_okhttp_master.okhttp.interceptor.RealInterceptorChain;
import com.example.zhangzd.cus_okhttp_master.okhttp.interceptor.RequestHeaderInterceptor;
import com.example.zhangzd.cus_okhttp_master.okhttp.interceptor.RetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-22 15:30
 */
public class RealCall implements Call {
    private final OkHttpClient client;
    private final Request originalRequest;
    private final boolean forWebSocket;
    private boolean executed;
    private RetryInterceptor retryInterceptor;

    private RealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
        this.client = client;
        this.originalRequest = originalRequest;
        this.forWebSocket = forWebSocket;
        retryInterceptor = new RetryInterceptor(client);

    }

    public static Call newRealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
        return  new RealCall(client, originalRequest, forWebSocket);
    }

    @Override
    public void enqueue(Callback responseCallback) {
        //判断是否重复加入队列
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        client.dispatcher().enqueue(new AsyncCall(responseCallback));
    }

    final class AsyncCall implements Runnable{
        private Callback callback;
        //获取主机地址
        String host() {
            return originalRequest.url();
        }

        public AsyncCall(Callback responseCallback) {
            callback = responseCallback;
        }


        @Override
        public void run() {
            //标记是否是用户操作导致的异常
            boolean signalledCallback = false;
            try {
                Response response = getResponseWithInterceptorChain();
                if (client.isCanceled()) {
                    signalledCallback = true;
                    callback.onFailure(RealCall.this, new IOException("Canceled"));
                } else {
                    signalledCallback = true;
                    callback.onResponse(RealCall.this, response);
                }
            } catch (Exception e) {
                if (signalledCallback) {
                    // Do not signal the callback twice!
                    System.out.println("Callback failure for Customer OkHttp" + e);
                } else {
//                    eventListener.callFailed(RealCall.this, e);
                    callback.onFailure(RealCall.this, e);
                }
            } finally {
                client.dispatcher().finished(this);
            }
        }
    }

    private Response getResponseWithInterceptorChain() throws Exception {
        // 责任链模式添加拦截器


        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(retryInterceptor);
        interceptors.add(new RequestHeaderInterceptor());
        interceptors.add(new ConnectInterceptor());


        RealInterceptorChain chain = new RealInterceptorChain(interceptors,0,originalRequest,this);

        return chain.proceed(originalRequest);
    }
}
