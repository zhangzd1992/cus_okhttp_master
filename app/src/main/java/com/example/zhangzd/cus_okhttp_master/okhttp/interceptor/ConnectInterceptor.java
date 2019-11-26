package com.example.zhangzd.cus_okhttp_master.okhttp.interceptor;

import com.example.zhangzd.cus_okhttp_master.okhttp.Request;
import com.example.zhangzd.cus_okhttp_master.okhttp.Response;
import com.example.zhangzd.cus_okhttp_master.okhttp.SocketRequestServer;
import com.example.zhangzd.cus_okhttp_master.okhttp.connectionPool.ConnectionPool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;


/**
 * @Description: 建立连接的拦截器
 * @Author: zhangzd
 * @CreateDate: 2019-11-25 13:31
 */
public class ConnectInterceptor implements Interceptor {

    @Override
    public Response doNext(Chan chan) throws Exception {
        Request request = chan.request();



        SocketRequestServer socketRequestServer = new SocketRequestServer();


        Socket socket = ConnectionPool.getInstance().queryConnect(socketRequestServer.host(request), socketRequestServer.port(request));

        if (socket == null) {
            socket = new Socket(socketRequestServer.host(request),socketRequestServer.port(request));
            if ("HTTPS".equalsIgnoreCase(socketRequestServer.getProtocol(request))){
                socket = SSLSocketFactory.getDefault().createSocket(socketRequestServer.host(request),socketRequestServer.port(request));
            }
            ConnectionPool.getInstance().addConnection(socket);
        }
        //请求
        OutputStream os = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
        bufferedWriter.write(socketRequestServer.getAllRequestHeader(request));
        bufferedWriter.flush();
        //响应
        InputStream inputStream = socket.getInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//                String readLine = null;
//                while (true) {
//                    try {
//                        if ((readLine = reader.readLine()) == null) break;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Log.e(TAG,readLine);
//                }
//
//            }
//        }.start();

        Response response = new Response();
        String[] responseLines = reader.readLine().split(" ");
        if (responseLines.length != 0) {
            response.setCode(Integer.parseInt(responseLines[1]));
        }
        String readLine = null;
        while ((readLine = reader.readLine()) != null) {
            if ("".equals(readLine)) {
                // 读到空行了，就代表下面就是 响应体了
                response.setBody(reader.readLine());
                break;
            }
        }


        return response;
    }
}
