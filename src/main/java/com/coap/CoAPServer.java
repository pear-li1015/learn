package com.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import org.eclipse.californium.core.coap.*;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: bin
 * @Date: 2019/9/14 10:14
 * @Description:
 */
public class CoAPServer {

    public static void main(String[] args) {
        CoapServer server = new CoapServer(); //主机为localhost 端口为默认端口5683

        server.add(new CoapResource("hello"){ //创建一个资源为hello 请求格式为 主机：端口\hello
            @Override
            public void handleGET(CoapExchange exchange) { //重写处理GET请求的方法
                exchange.respond(ResponseCode.CONTENT, "Hello CoAP!");
            }
        });
        server.add(new CoapResource("time"){ //创建一个资源为time 请求格式为 主机：端口\time
            @Override
            public void handleGET(CoapExchange exchange) {
                Date date = new Date();
                exchange.respond(ResponseCode.CONTENT,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
            }
        });

        server.add(new CoapResource("file"){ //创建一个资源为time 请求格式为 主机：端口\time
            @Override
            public void handleGET(CoapExchange exchange) {
                System.out.println("处理请求...");
                Request request = exchange.advanced().getRequest();
//                LOG.info("Get received : {}", request);

                int accept = request.getOptions().getAccept();
                if (MediaTypeRegistry.UNDEFINED == accept) {
                    accept = MediaTypeRegistry.APPLICATION_OCTET_STREAM;
                } else if (MediaTypeRegistry.APPLICATION_OCTET_STREAM != accept) {
                    exchange.respond(CoAP.ResponseCode.UNSUPPORTED_CONTENT_FORMAT);
                    return;
                }

                String myURI = getURI() + "/";
                System.out.println(" myuri " + myURI);
                System.out.println("path: " + request.getOptions().getUriPathString());
//                String path = "D:\\test\\file.jpg";
                String path = "D:\\test\\testvideo.mp4";
//                if (!path.startsWith(myURI)) {
//                    System.out.println(" Request {} does not match {} " + path + myURI);
////                    LOG.info("Request {} does not match {}!", path, myURI);
//                    exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
//                    return;
//                }
//                path = path.substring(myURI.length());
                if (request.getOptions().hasBlock2()) {
                    System.out.println("send file " + path + " " + request.getOptions().getBlock2());
//                    LOG.info("Send file {} {}", path, request.getOptions().getBlock2());
                } else {
                    System.out.println("send file " + path);
//                    LOG.info("Send file {}", path);
                }
                File file = new File(path);
                long maxLength = 30011041;
//                long maxLength = config.getInt(NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE);
                long length = file.length();
                if (length > maxLength) {
                    System.out.println("file " + file.getAbsolutePath() + " is too large " + length + " max: " + maxLength);
//                    LOG.warn("File {} is too large {} (max.: {})!", file.getAbsolutePath(), length, maxLength);
                    exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
                    return;
                }
                try (InputStream in = new FileInputStream(file)) {
                    byte[] content = new byte[(int) length];
                    int r = in.read(content);
                    if (length == r) {
                        Response response = new Response(CoAP.ResponseCode.CONTENT);
                        response.setPayload(content);
                        response.getOptions().setSize2((int) length);
                        response.getOptions().setContentFormat(accept);
                        exchange.respond(response);
                    } else {
                        System.out.println("file " + file.getAbsolutePath() + " could not be read in");
//                        LOG.warn("File {} could not be read in!", file.getAbsolutePath());
                        exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
                    }
                } catch (IOException ex) {
                    System.out.println("file " + file.getAbsolutePath() + " " + ex);
//                    LOG.warn("File {}:", file.getAbsolutePath(), ex);
                    exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
                }
            }

        });
        server.start();
    }




}
