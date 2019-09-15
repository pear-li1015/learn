package com.tomcat1.server;

import io.seata.spring.annotation.GlobalTransactional;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: bin
 * @Date: 2019/9/8 10:09
 * @Description:
 */
public class MyTomcat {

    private int port = 8080;

    public MyTomcat() {

    }

    public MyTomcat(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("my tomcat start on " + port);
        while (true) {
            Socket socket = serverSocket.accept();
            socket.getOutputStream().write("hello world".getBytes());
            socket.close();
        }
    }

    public static void main(String[] args) throws Exception {
        MyTomcat myTomcat = new MyTomcat();
        myTomcat.start();
    }
}
