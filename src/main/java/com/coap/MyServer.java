package com.coap;

import com.coap.pack.MyCoapResource;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.*;

public class MyServer extends CoapServer {

    CoapServer server; //主机为localhost 端口为默认端口5683

    public MyServer() {
        super();
        server = new CoapServer();
    }

    public MyServer(String url, String port) {

    }

    public void addResource(String name, Class clazz) {
        // TODO ...
//        if (!clazz.isInstance(ResourceRule.class)) {
//            System.out.println("规则必须实现 ResourceRule 抽象类");
//            return;
//        }
//        CoapResource coapResource = new CoapResource(name);
        MyCoapResource myCoapResource = new MyCoapResource(name, clazz);
        server.add((CoapResource)myCoapResource);
    }
//    private byte[] transFileToBytes(File file) {
//        InputStream in = null;
//        byte[] content = new byte[(int) file.length()];
//        try {
//            in = new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            return content;
//        }
//
//    }

    public void start() {
        server.start();
    }
}
