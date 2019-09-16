package com.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.*;

public abstract class MyServer {

    CoapServer server; //主机为localhost 端口为默认端口5683

    public MyServer() {
        server = new CoapServer();
    }

    public MyServer(String url, String port) {

    }

    public void addResource(String name) {
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

                MyResourceRule myResourceRule = new MyResourceRule();
                // 根据用户传入的内容，返回指定文件
                File file = myResourceRule.returnFile(request.getOptions().getUriPathString());
                transFileToBytes(file);
//                String myURI = getURI() + "/";
//                System.out.println(" myuri " + myURI);
//                System.out.println("path: " + request.getOptions().getUriPathString());
//                String path = "D:\\test\\testvideo.mp4";
//                if (request.getOptions().hasBlock2()) {
//                    System.out.println("send file " + path + " " + request.getOptions().getBlock2());
////                    LOG.info("Send file {} {}", path, request.getOptions().getBlock2());
//                } else {
//                    System.out.println("send file " + path);
////                    LOG.info("Send file {}", path);
//                }
//                File file = new File(path);
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
    }
    private byte[] transFileToBytes(File file) {
        InputStream in = null;
        byte[] content = new byte[(int) file.length()];
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return content;
        }

    }

    public void start() {
        server.start();
    }
}
