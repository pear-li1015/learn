package com.communication.coap;

import com.coap.dtlsTest.CoAPMessage;

import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * @Author: bin
 * @Date: 2019/10/1 9:32
 * @Description:
 */
public class CoAPUtil {


    /**
     * 将超出传输大小限制的信息拆分为多条信息
     * @param inMessage
     * @return
     */
//    public List<CoAPMessage> devideCoAPMessage(CoAPMessage inMessage) {
//        char a = '\n';
//    }
    public static void main(String[] args) {


        String s = "123456789012";

        System.out.println(s.getBytes().length);
        System.out.println(new Date().getTime());
        System.out.println(CoAPMessage.getUUID());
        System.out.println(CoAPMessage.getUUID().length());
        int a = 1;
        System.out.println();
    }
}
