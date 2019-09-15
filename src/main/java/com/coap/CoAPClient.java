package com.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.network.stack.BlockwiseLayer;
import org.eclipse.californium.core.server.MessageDeliverer;
import org.eclipse.californium.elements.EndpointContext;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
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
//        client.advanced();
//        client.
//        Request request = new Request();
//        request.
//        BlockwiseLayer blockwiseLayer =

        CoapResponse response = client.get();
        if(response != null) {
//            while (response.getCode().toString().equals("2.31")) {
//                System.out.println("还没有打完");
//                System.out.println(prettyPrint(response));  //打印格式良好的输出
//
//            }
            System.out.println(prettyPrint(response));  //打印格式良好的输出
        }
//        response.getOptions().hasBlock2()
    }


    private static String prettyPrint(CoapResponse response) {
        return "\n=====================\n" + response.getResponseText() + "" +
                response.advanced().getCode() + "\n================\n";
    }

}
