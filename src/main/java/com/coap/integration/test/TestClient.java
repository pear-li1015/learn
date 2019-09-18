package com.coap.integration.test;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class TestClient {
    /**
     * 1、向服务端发送字符串
     * 2、向服务端发送文件，并接收服务端文件
     */

    public static void main(String[] args) throws Exception {
//        stringTransTest();
//        fileTransTest();

        postFileTransTest();


    }


    /**
     * 向指定接口发送post请求
     * @param uri 格式 “localhost:5673/file”
     * @param payload
     * @param mediaType 祥参 #{@link MediaTypeRegistry}
     * @return
     * @throws Exception
     */
    private static CoapResponse transTest(String uri, byte[] payload, int mediaType) throws Exception {

        CoapClient client = new CoapClient(new URI(uri));
        CoapResponse response = client.post(payload, mediaType);
        if (response != null) {
            System.out.println(" response1.isSuccess() " + response.isSuccess());

            System.out.println("response text " + response.getResponseText());
        }

        return response;
    }

    private static CoapResponse transTest(String uri, String payload, int mediaType) throws Exception {
        return transTest(uri, payload.getBytes(), mediaType);
    }

    private static void stringTransTest()  throws Exception {

        CoapResponse response1 = transTest("localhost:5683/string", "payload from request", MediaTypeRegistry.TEXT_PLAIN);

        System.out.println(" response1.isSuccess() " + response1.isSuccess());

        System.out.println("response text " + response1.getResponseText());
    }

    private static void fileTransTest() throws Exception {
        CoapResponse response = transTest("localhost:5683/file", "payload from request", MediaTypeRegistry.TEXT_PLAIN);

        System.out.println(" response1.isSuccess() " + response.isSuccess());

        System.out.println("response text " + response.getResponseText());
        System.out.println("==" + response);
        if(response != null) {
            saveFileTest(response, "D:\\test\\coap.jpg");
        }
    }

    private static void saveFileTest(CoapResponse response, String filePath) throws Exception {
        int contentFormat = response.advanced().getOptions().getContentFormat();
        System.out.println("contentFormat " + contentFormat);

        File file = new File(filePath);
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(response.getPayload());
        System.out.println("finish");
    }


    private static byte[] postFileTest() throws IOException {
        File file = new File("D:\\test\\myfile.txt");
        InputStream inputStream = new FileInputStream(file);

        byte[] bytes = new byte[(int)file.length()];

        if (bytes.length != file.length()) {
            System.out.println("文件过大。");
        }
        inputStream.read(bytes);
        return bytes;
    }

    private static void postFileTransTest() throws Exception {

        CoapResponse response = transTest("localhost:5683/postfile", postFileTest(), MediaTypeRegistry.TEXT_PLAIN);

        System.out.println(" response1.isSuccess() " + response.isSuccess());

        System.out.println("response text " + response.getResponseText());
        System.out.println("==" + response);
//        if(response != null) {
//            saveFileTest(response, "D:\\test\\coap.jpg");
//        }
    }

}
