package com.example.zhangzd.cus_okhttp_master.okhttp;

import android.util.ArrayMap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-22 15:25
 */
public class Request {
    public static final String TYPE = "application/x-www-form-urlencoded";
    private String url;
    public final static String POST = "POST";
    public final static String GET = "GET";
    private String method = GET;
    private RequestBody requestBody;

    //属性集集合
    private Map<String,String> headerAttriMap = new ArrayMap<>();
    public Request(Builder builder) {
        url = builder.url;
        this.requestBody = builder.requestBody;
        this.method = builder.method;

    }

    public String getMethod() {
        return method;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public Map<String, String> getHeaderAttriMap() {
        return headerAttriMap;
    }

    public String url() {
        try {
            URL url = new URL(this.url);
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }



    public String getUrl() {
        return url;
    }

    public static final class Builder {
        private String url;
        private String method = GET;
        private RequestBody requestBody;

        public Request builder() {

            return new Request(this);
        }

        public Builder get() {
            method = GET;
            return this;
        }

        public Builder post(RequestBody requestBody) {
            method = POST;
            this.requestBody = requestBody;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }
    }
}
