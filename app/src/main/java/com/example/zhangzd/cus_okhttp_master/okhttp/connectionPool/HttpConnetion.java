package com.example.zhangzd.cus_okhttp_master.okhttp.connectionPool;

import java.net.Socket;

/**
 * @Description: 连接对象
 * @Author: zhangzd
 * @CreateDate: 2019-11-25 17:24
 */
public class HttpConnetion {
    private Socket socket;
    private long addTime;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public boolean isOneConnection(String host, int port) {
        if (host == null || port == -1) {
            return false;
        }


        if (socket.getInetAddress().getHostName().equalsIgnoreCase(host) && port == socket.getPort()) {
            return true;
        }

        return false;

    }
}
