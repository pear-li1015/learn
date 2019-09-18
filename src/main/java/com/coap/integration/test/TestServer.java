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
         * 4、请求的 payload 暂时无法携带。
         *
         * 注意
         * 1、目前单个请求仍有最大限制， 长度必须小于int的范围
         */
        CoAPServer server = new CoAPServer();
        server.addResource("file", new TestFileResourceRule());
        server.addResource("string", new TestStringResourceRule());
        server.addResource("postfile", new TestPostFileResourceRule());

        server.start();

    }


}
