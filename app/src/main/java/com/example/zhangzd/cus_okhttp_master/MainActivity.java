package com.example.zhangzd.cus_okhttp_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;



import com.example.zhangzd.cus_okhttp_master.okhttp.Call;
import com.example.zhangzd.cus_okhttp_master.okhttp.Callback;
import com.example.zhangzd.cus_okhttp_master.okhttp.OkHttpClient;
import com.example.zhangzd.cus_okhttp_master.okhttp.Request;
import com.example.zhangzd.cus_okhttp_master.okhttp.RequestBody;
import com.example.zhangzd.cus_okhttp_master.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "OKHTTP";
//    private final String url = "http://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2";
    private final String url = "http://restapi.amap.com/v3/weather/weatherInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public  void startRequest(View view) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        RequestBody requestBody = new RequestBody();
        requestBody.addBody("city","110101");
        requestBody.addBody("key","13cb58f5884f9749287abbead9c658f2");
        Request request = new Request.Builder().url(url).post(requestBody).builder();


        Call call = okHttpClient.newCall(request);


        call.enqueue(new Callback(){

            @Override
            public void onFailure(Call call, Exception e) {
                Log.e(TAG,"走了onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG,"onResponse" + response.getBody());
            }
        });

    }

    public void httpsRequest(View view) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        RequestBody requestBody = new RequestBody();

        Request request = new Request.Builder().url("https://www.baidu.com/s?wd=a&rsv_spt=1&rsv_iqid=0xe21a4c360001bb84&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_dl=tb&rsv_sug3=2&rsv_sug2=0&inputT=625&rsv_sug4=1017").get().builder();


        Call call = okHttpClient.newCall(request);


        call.enqueue(new Callback(){

            @Override
            public void onFailure(Call call, Exception e) {
                Log.e(TAG,"走了onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG,"onResponse" + response.getBody());
            }
        });
    }
}
