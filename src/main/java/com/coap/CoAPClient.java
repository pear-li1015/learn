package com.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.*;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.network.stack.BlockwiseLayer;
import org.eclipse.californium.core.server.MessageDeliverer;
import org.eclipse.californium.elements.EndpointContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author: bin
 * @Date: 2019/9/14 13:52
 * @Description:
 */
public class CoAPClient {

    public static void main(String[] args) throws Exception {

        URI uri = null;
        uri = new URI("localhost:5683/file");  //创建一个资源请求hello资源，注意默认端口为5683
        CoapClient client = new CoapClient(uri);
        CoapResponse response = client.get();
//        Request request = new Request(CoAP.Code.POST, CoAP.Type.CON);
        Request request = new Request(CoAP.Code.POST, CoAP.Type.ACK);
        client.advanced(request);
        System.out.println("==" + response);
        if(response != null) {
            System.out.println("----");
            byte[] out = response.getPayload();
            File file = new File("D:\\test\\coap.jpg");

            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(out);
            System.out.println("wancheng");
//            System.out.println(prettyPrint(response));  //打印格式良好的输出
        }

//        uri = new URI("localhost:5684/hello");  //创建一个资源请求hello资源，注意默认端口为5683
//        CoapClient client1 = new CoapClient(uri);
//        CoapResponse response1 = client1.get();
//        System.out.println("=-=" + response1);

    }


    private static String prettyPrint(CoapResponse response) {
        return "\n=====================\n" + response.getResponseText() + "" +
                response.advanced().getCode() + "\n================\n";
    }

}
