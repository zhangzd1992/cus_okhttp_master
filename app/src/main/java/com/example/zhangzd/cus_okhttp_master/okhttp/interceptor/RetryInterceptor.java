package com.example.zhangzd.cus_okhttp_master.okhttp.interceptor;

import com.example.zhangzd.cus_okhttp_master.okhttp.OkHttpClient;
import com.example.zhangzd.cus_okhttp_master.okhttp.Request;
import com.example.zhangzd.cus_okhttp_master.okhttp.Response;

/**
 * @Description:  重试拦截器
 * @Author: zhangzd
 * @CreateDate: 2019-11-22 16:53
 */
public class RetryInterceptor implements Interceptor {
    private OkHttpClient client;

    public RetryInterceptor(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public Response doNext(Chan chan) throws Exception {
        Request request = chan.request();

        Exception exception = new Exception("请求失败");

        for (int retryCount = client.getRetryCount(); retryCount > 0; retryCount--) {
            try {
                return chan.proceed(request);
            } catch (Exception e) {
                exception = e;
            }


        }

       throw exception;
    }
}
