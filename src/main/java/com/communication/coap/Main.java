package com.communication.coap;

import com.coap.dtlsTest.CoAPCallBack;
import com.coap.dtlsTest.CoAPMessage;
import com.communication.MessageHandler;
import com.communication.common.CallBack;
import com.communication.common.Message;
import com.flume.FlumeTest3;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: bin
 * @Date: 2019/9/26 15:43
 * @Description:
 */
public class Main {

    private static Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        // 启动一个server
        CoAPServer server = new CoAPServer();
        server.startAServer();

        // 启动一个client
        CoAPClient client = new CoAPClient();
        client.startAClient();

        // 启动一个handler
        MessageHandler handler = new MessageHandler();
        handler.startAHandler();

        // 从client发起一个请求，请求server的一个文件。并在返回时保存。
        Message message = new CoAPMessage("", "".getBytes(), new CallBack() {
            @Override
            public void callback(Message response) {
                log.info(new String(response.getContent()));
                System.out.println("回调函数中内容" + new String(response.getContent()));
            }
        });
        message.send();



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
