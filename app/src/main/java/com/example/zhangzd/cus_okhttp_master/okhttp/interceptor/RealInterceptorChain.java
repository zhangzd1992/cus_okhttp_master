package com.example.zhangzd.cus_okhttp_master.okhttp.interceptor;

import com.example.zhangzd.cus_okhttp_master.okhttp.Call;
import com.example.zhangzd.cus_okhttp_master.okhttp.Request;
import com.example.zhangzd.cus_okhttp_master.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-25 09:59
 */
public class RealInterceptorChain implements Interceptor.Chan {
    private List<Interceptor> interceptors;
    private int index;
    private Request request;
    private Call call;

    public RealInterceptorChain(List<Interceptor> interceptors, int index, Request request, Call call) {
        this.interceptors = interceptors;
        this.index = index;
        this.request = request;
        this.call = call;
    }


    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response proceed(Request request) throws Exception {

        if (interceptors.isEmpty()) {
            throw new IOException("拦截器为空");
        }

        Interceptor interceptor = interceptors.get(index);

        RealInterceptorChain chain = new RealInterceptorChain(interceptors, index + 1, request, call);
        return interceptor.doNext(chain);
    }
}
