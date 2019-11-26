package com.example.zhangzd.cus_okhttp_master.okhttp;

/**
 * @Description:
 * @Author: zhangzd
 * @CreateDate: 2019-11-22 15:22
 */
public class OkHttpClient {
    private Dispatcher dispatcher;
    private boolean isCancel;
    private int retryCount = 5;  //请求重试次数，默认为5次



    public OkHttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.isCancel = builder.isCancel;
        this.retryCount = builder.retryCount;
    }

    public Call newCall(Request request) {
        return RealCall.newRealCall(this, request, false /* for web socket */);
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public boolean isCanceled() {
        return isCancel;
    }


    public int getRetryCount(){
        return retryCount;
    }

    public static final class Builder {
        private Dispatcher dispatcher;
        private boolean isCancel;
        private int retryCount = 5;  //请求重试次数，默认为5次
        public Builder setDispathcer(Dispatcher dispathcer) {
            this.dispatcher = dispathcer;
            return this;
        }


        public Builder setRetryCount(int count) {
            this.retryCount = count;
            return this;
        }

        public Builder isCancel(boolean isCancel) {
            this.isCancel = isCancel;
            return this;
        }


        public OkHttpClient build() {
            this.dispatcher = new Dispatcher();
            return new OkHttpClient(this);
        }

    }
}
