package com.example.zhangzd.cus_okhttp_master.okhttp.interceptor;

import com.example.zhangzd.cus_okhttp_master.okhttp.Request;
import com.example.zhangzd.cus_okhttp_master.okhttp.Response;

import java.util.Map;

/**
 * @Description:  请求头拦截器
 * @Author: zhangzd
 * @CreateDate: 2019-11-25 11:45
 */
public class RequestHeaderInterceptor implements Interceptor {


    @Override
    public Response doNext(Chan chan) throws Exception {
        Request request = chan.request();
        Map<String, String> headerAttriMap = request.getHeaderAttriMap();


        headerAttriMap.put("host",request.url());


        if (Request.POST.equalsIgnoreCase(request.getMethod())) {
            headerAttriMap.put("Content-Type", Request.TYPE);
            headerAttriMap.put("Content-Length","" + request.getRequestBody().getBody().length());
        }
        return chan.proceed(request);
    }
}
