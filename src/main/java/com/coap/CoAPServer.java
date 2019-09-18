package com.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import org.eclipse.californium.core.coap.*;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: bin
 * @Date: 2019/9/14 10:14
 * @Description:
 */
public class CoAPServer {

    public static void main(String[] args) {


        MyServer myServer = new MyServer();
        myServer.addResource("file", MyResourceRule.class);

        myServer.start();


        CoapServer server = new CoapServer(5684); //主机为localhost 端口为默认端口5683

        server.add(new CoapResource("hello"){ //创建一个资源为hello 请求格式为 主机：端口\hello
            @Override
            public void handleGET(CoapExchange exchange) { //重写处理GET请求的方法
                exchange.respond(ResponseCode.CONTENT, "Hello CoAP!");
            }
        });
        server.add(new CoapResource("time"){ //创建一个资源为time 请求格式为 主机：端口\time
            @Override
            public void handleGET(CoapExchange exchange) {
                Date date = new Date();
                exchange.respond(ResponseCode.CONTENT,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
            }
        });
        server.start();
    }




}
