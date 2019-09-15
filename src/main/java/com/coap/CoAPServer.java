package com.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: bin
 * @Date: 2019/9/14 10:14
 * @Description:
 */
public class CoAPServer {

    public static void main(String[] args) {
        CoapServer server = new CoapServer(); //主机为localhost 端口为默认端口5683

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

        server.add(new CoapResource("file"){ //创建一个资源为time 请求格式为 主机：端口\time
            @Override
            public void handleGET(CoapExchange exchange) {
                System.out.println("处理请求...");
//                Response response = new Response()
//                exchange.advanced().sendResponse();\
                OptionSet optionSet = exchange.getRequestOptions();
                System.out.println("-------------");
                System.out.println(optionSet.toString());
//                exchange.respond();
//                Response response = new Response();

//                for (int i = 0; i < 5; i ++) {
//
//                    exchange.respond(ResponseCode.CONTINUE,
//                            "内容" + i);
//                }
                exchange.respond(ResponseCode.CONTENT,
                        "内容结束");
            }

        });
        server.start();
    }




}
