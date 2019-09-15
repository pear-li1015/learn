package com.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.server.MessageDeliverer;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @Author: bin
 * @Date: 2019/9/14 14:47
 * @Description:
 */
public class CoAPServerForFile {

    public static void main(String[] args) {
        final CoapServer server = new CoapServer();//主机为localhost 端口为默认端口5683
        final MessageDeliverer messageDeliverer = new MessageDeliverer() {
            public void deliverRequest(Exchange exchange) {

                Request request = exchange.getRequest();

                System.out.println("get Request");
                if (exchange.getRequest().getCode() == CoAP.Code.GET) {
                    if (!exchange.getRequest().getOptions().hasBlock2()) {
                        System.out.println("tiao chu");
                        Response response = new Response(ResponseCode.NOT_FOUND);
                        exchange.sendResponse(response);
                    }
//                    String Path = "E://FOTA/" + exchange.getRequest().getOptions().getUriPathString();
                    String Path = "D://home/testFile.mp4";
                    File file = new File(Path);
                    if (file.exists()) {
                        System.out.println("file exists");
                        if (file.canRead()) {
                            System.out.println("file canRead");
                            OptionSet optionSet = new OptionSet();
                            optionSet.setUriPath(exchange.getRequest().getOptions().getUriPathString());
                            int num = exchange.getRequest().getOptions().getBlock2().getNum();
                            int size = exchange.getRequest().getOptions().getBlock2().getSize();
                            Response response = null;
                            RandomAccessFile ran = null;
                            try {
                                System.out.println("trying...");
                                int hasread;
                                ran = new RandomAccessFile(file, "r");
                                ran.seek(num * size);
                                byte[] b = new byte[size];
                                if (ran.length() - num * size <= 0) {
                                    System.out.println("bad size");
                                    response = new Response(ResponseCode.NOT_FOUND);
                                    exchange.sendResponse(response);
                                    return;
                                }
                                if (ran.length() > (num + 1) * size) {
                                    System.out.println(" ran.length() >  ");
                                    optionSet.setBlock2(5, true, num);
                                } else {
                                    System.out.println(" ran.length() <  ");
                                    optionSet.setBlock2(5, false, num);
                                }
                                hasread = ran.read(b);
                                byte[] payload = new byte[hasread];
                                System.arraycopy(b, 0, payload, 0, hasread);
                                response = new Response(ResponseCode.CONTENT);
                                response.setPayload(payload);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    System.out.println(" ran.close()");
                                    ran.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            response.setOptions(optionSet);
                            exchange.sendResponse(response);
                        }
                    } else {
                        System.out.println("get file" + file.getPath().toString() + "Fail");
                        Response response = new Response(ResponseCode.NOT_FOUND);
                        exchange.sendResponse(response);
                        return;
                    }
                }
            }

            public void deliverResponse(Exchange exchange, Response response) {
                exchange.sendResponse(response);
                System.out.println("send Request");
            }
        };
        server.setMessageDeliverer(messageDeliverer);
        server.start();
    }

}
