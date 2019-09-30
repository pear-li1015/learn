package com.communication.coap;

import com.coap.dtlsTest.CoAPCallBack;
import com.coap.dtlsTest.CoAPMessage;
import com.communication.MessageHandler;
import com.communication.common.CallBack;
import com.communication.common.Message;
import com.flume.FlumeTest3;
import org.apache.log4j.Logger;
import org.eclipse.californium.scandium.DTLSConnector;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
                System.out.println("准备执行回调函数 。。。");
                // 保存文件到磁盘
                try {
                    OutputStream outputStream = new FileOutputStream("D:\\test\\coap\\output.jpg");
//                    outputStream.
                    outputStream.write(response.getContent());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                log.info(new String(response.getContent()));
//                System.out.println("回调函数中内容" + new String(response.getContent()));
            }
        });
//        DTLSConnector.
        message.send();
        /**
         * 以上测试的消息发送路径为：
         * 1、client向server发送请求，等待返回结果后执行回调
         * 2、server收到请求，将message交给 handler
         * 3、handler发现应该将执行结果交给对方，以便对方执行回调
         * 4、handler调用client发送响应
         * 5、server收到响应，并执行回调
         * 其实 这里只有一个client和server。
         */



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
