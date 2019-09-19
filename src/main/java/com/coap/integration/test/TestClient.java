package com.coap.integration.test;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;


import java.io.*;
import java.net.URI;

public class TestClient {
    /**
     * 1、向服务端发送字符串
     * 2、向服务端发送文件，并接收服务端文件
     * mediaType 祥参 #{@link MediaTypeRegistry}
     */

    public static void main(String[] args) throws Exception {
        strTest();
        System.out.println("-------------分割线--------------");
        fileTest();

    }

    /**
     * 向指定接口发送字符串 ，并接收返回的字符串
     */
    private static void strTest() throws Exception {

        CoapClient client = new CoapClient(new URI("localhost:5683/string"));
        CoapResponse response = client.post("payload from client", MediaTypeRegistry.TEXT_PLAIN);

        if (response != null) {
            System.out.println("response text: " + response.getResponseText());
            if (response.getOptions().getContentFormat() == MediaTypeRegistry.TEXT_PLAIN) {
                System.out.println("response contentFormat == TEXT_PLAIN");
            }
        }
    }
    /**
     * 向指定接口发送文件，并保存 返回的文件
     */
    private static void fileTest() throws Exception {
        CoapClient client = new CoapClient(new URI("localhost:5683/file"));
        File inFile = new File("D:\\test\\file.jpg");
        InputStream inputStream = new FileInputStream(inFile);
        byte[] inFileBytes = new byte[(int) inFile.length()];
        if (inFile.length() == inFileBytes.length) {
            System.out.println("inFileBytes == inFile.length()");
            inputStream.read(inFileBytes);
            inputStream.close();
            CoapResponse response = client.post(inFileBytes, MediaTypeRegistry.IMAGE_JPEG);

            if (response != null) {
                System.out.println("response code: " + response.getCode());
                byte[] outFileBytes = response.getPayload();
                if (response.getOptions().getContentFormat() == MediaTypeRegistry.IMAGE_JPEG) {
                    System.out.println("返回的文件后缀为 .jpg");
                }
                File outFile = new File("D:\\test\\c.jpg");
                OutputStream outputStream = new FileOutputStream(outFile);
                outputStream.write(outFileBytes);
                outputStream.close();
            }
        }
    }



}
