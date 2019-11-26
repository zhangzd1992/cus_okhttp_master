package com.example.zhangzd.cus_okhttp_master.okhttp;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-25 15:10
 */
public class SocketRequestServer {
    private final String TAG ="OKHTTP";
    private final String blackSpace = " ";
    private final String protocolVersion = "HTTP/1.1";
    private final String GRGN = "\r\n";
    private final String EN = "utf-8";


    public String host(Request request) {
        try {
            URL url = new URL(request.getUrl());
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int port(Request request) {
        try {
            URL url = new URL(request.getUrl());
            return url.getPort() == -1? url.getDefaultPort(): url.getPort();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public String getAllRequestHeader(Request request) throws  Exception{
        //创建请求头
        Map<String, String> headerAttriMap = request.getHeaderAttriMap();
        if (headerAttriMap.isEmpty()) {
            throw  new Exception("请求头数据为空");
        }

        StringBuilder builder = new StringBuilder();
        //请求行
        builder.append(request.getMethod())
                .append(blackSpace)
                .append(getFile(request))
                .append(blackSpace)
                .append(protocolVersion)
                .append(GRGN);

        //添加请求属性集
        Set<Map.Entry<String, String>> entries = headerAttriMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            builder.append(URLEncoder.encode(entry.getKey(),EN))
                    .append(":")
                    .append(blackSpace)
                    .append(URLEncoder.encode(entry.getValue(),EN))
                    .append(GRGN);
        }

        //请求头结束后的空行
        builder.append(GRGN);
        //添加请求体
        if (Request.POST.equalsIgnoreCase(request.getMethod())){
            builder.append(request.getRequestBody().getBody());
        }

        Log.e(TAG,builder.toString());

        return builder.toString();

    }

    public String getProtocol(Request request) {
        try {
            URL url = new URL(request.getUrl());
            return url.getProtocol();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFile(Request request) {
        try {
            URL url = new URL(request.getUrl());
            return url.getFile();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return "";
    }
}
