package com.example.zhangzd.cus_okhttp_master.okhttp.interceptor;

import com.example.zhangzd.cus_okhttp_master.okhttp.Request;
import com.example.zhangzd.cus_okhttp_master.okhttp.Response;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-22 16:48
 */
public interface Interceptor {
    Response doNext(Chan chan) throws Exception;


    interface Chan{
        Request request();
        Response proceed(Request request) throws Exception;
    }
}
