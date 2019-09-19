package com.coap.integration.test;

import com.coap.integration.CoAPServer;

public class TestServer {

    public static void main(String[] args) {


        // TODO
        /**
         * 需改进之处
         * 1、无法选择udp或tcp协议
         * 2、Udp必须使用dtls加密才能保证可靠传输
         * 3、注意四种组合方式，是否互相补充不足。
         *
         * 注意
         * 1、目前单个请求仍有最大限制， 长度必须小于int的范围
         */
        CoAPServer server = new CoAPServer();
        // 添加一个 传输文件的 资源。
        server.addResource("file", new TestFileResourceRule());
        // 添加一个传输字符串的资源。
        server.addResource("string", new TestStringResourceRule());

        server.start();

    }


}
