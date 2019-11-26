package com.example.zhangzd.cus_okhttp_master.okhttp;

import java.io.IOException;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-22 15:35
 */
public interface Callback {
    void onFailure(Call call, Exception e);

    void onResponse(Call call, Response response) throws IOException;
}
