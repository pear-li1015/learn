package com.communication;

import com.coap.dtlsTest.CoAPMessage;
import com.communication.coap.MessageList;
import org.eclipse.californium.elements.util.DaemonThreadFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: bin
 * @Date: 2019/9/28 12:25
 * @Description:
 */
public class MessageHandler {
    // 此类 处理 preHandList中内容
    // ying支持多线程 注意线程安全问题

    public void startAHandler() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                new DaemonThreadFactory("AuxH#"));
        executor.execute(new Runnable() {

            @Override
            public void run() {
                List<CoAPMessage> preHandList = MessageList.getPreHandList();
                while (true) {
                    System.out.println("当前 preHandList 的长度为 " + preHandList.size());
                    if (preHandList.size() > 0) {
                        CoAPMessage message = preHandList.remove(0);
                        System.out.println("handler: " + message.toString());
                        if (message.getState() == ConstUtil.MESSAGE_MY_CALLBACK) {
                            System.out.println("找到一个需要被回调的message");
                            // 本机之前的请求，现在返回了
                            CoAPMessage preMessage = MessageList.getByUuid(message.getUuid(), message.getFrom(), message.getTo());
                            if (preMessage != null) {
                                System.out.println("handler 调用 之前的回调函数");
                                // 调用之前设置的回调函数
                                preMessage.getCallBack().callback(message);
                            } else {
                                // 可以考虑把这个message再次入队，不过，感觉是浪费资源了。
                                System.out.println("刚刚也打印了，没有找到相应的 回调。");
                            }
                        } else if (message.getState() == ConstUtil.MESSAGE_HIS_CALLBACK) {
                            System.out.println("对方从这里请求，以便执行他的回调");
                            // 对方那里 需要从 这里请求数据，以进行下一步操作。
                            // TODO 拿到对方数据，做点什么。
                            message.getContent();
                            // TODO 比如说返回一个文件
                            message.setContent("这是返回的文件".getBytes());
                            // 设置发送者和接收者
                            message.setTo(message.getFrom());
                            message.setFrom("");
                            message.setSendTime(new Date());
                            // 改状态
                            // 告诉对方，那里有回调 等待执行
                            message.setState(ConstUtil.MESSAGE_MY_CALLBACK);
                            // 将此信息发回去
                            MessageList.getPreSendList().add(message);
                        } else if (message.getState() == ConstUtil.MESSAGE_NO_CALLBACK) {
                            // 正常处理，只是没有回调函数而已。 我也不知道这是干啥的数据
                        } else {
                            System.out.println("这里应该报错，或者增加新的逻辑");
                        }
                        // 保存文件
//                        saveBytesToFile(message.getContent());
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    private void saveBytesToFile(byte[] bytes) {
        System.out.println("message handler save bytes to file ...");

    }

}
