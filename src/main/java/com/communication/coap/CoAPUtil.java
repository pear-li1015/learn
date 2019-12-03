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

    public static final String TEST_IN_IMAGE_PATH = "D:\\test\\coap\\file.jpg";
    public static final String TEST_OUT_IMAGE_PATH = "D:\\test\\coap\\out.jpg";
    public static final String TEST_IN_MP4_PATH = "D:\\test\\coap\\test.mp4";
    public static final String TEST_OUT_MP4_PATH = "D:\\test\\coap\\out.mp4";
    public static final String TEST_IN_DOCX_PATH = "D:\\test\\coap\\zzz.docx";
    public static final String TEST_OUT_DOCX_PATH = "D:\\test\\coap\\zzz1.docx";


    public static final String TEST_IN_FILE_PATH = TEST_IN_MP4_PATH;
    public static final String TEST_OUT_FILE_PATH = TEST_OUT_MP4_PATH;

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
