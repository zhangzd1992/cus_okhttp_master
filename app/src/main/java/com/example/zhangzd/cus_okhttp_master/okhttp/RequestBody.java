package com.example.zhangzd.cus_okhttp_master.okhttp;

import android.os.Environment;
import android.util.ArrayMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-25 11:50
 */
public class RequestBody {
    // 表单提交Type application/x-www-form-urlencoded
    public static final String TYPE = "application/x-www-form-urlencoded";

    private final String ENC = "utf-8";

    private Map<String,String> bodys = new ArrayMap<>();

    public void addBody(String key,String value) {
        try {
            bodys.put(URLEncoder.encode(key, ENC),URLEncoder.encode(value,ENC));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    public String getBody() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : bodys.entrySet()) {
            builder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }


        if (!builder.toString().isEmpty()) {
            builder.deleteCharAt(builder.toString().length() - 1);
        }
        return builder.toString();
    }
}
