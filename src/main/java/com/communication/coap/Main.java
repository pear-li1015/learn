package com.communication.coap;

import com.coap.dtlsTest.CoAPMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: bin
 * @Date: 2019/9/26 15:43
 * @Description:
 */
public class Main {

    public static void main(String[] args) {
        // 启动一个server
        CoAPServer server = new CoAPServer();
        server.startAServer();

        // 启动一个client
        CoAPClient client = new CoAPClient();
        client.startAClient();


        while (true) {
            try {
                Thread.sleep(2000);
                // 这样会报错， dtlsConnector的原因。 应该需要新建一个。
//                if (MessageList.getPreHandList().size() > 100) {
//                    CoAPServer newServer = new CoAPServer();
//                    newServer.startAServer();
//                    Thread.sleep(10000);
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }





    }

}
