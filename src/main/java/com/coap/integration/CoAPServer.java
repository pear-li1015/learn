package com.coap.integration;

import com.coap.pack.MyCoapResource;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

public class CoAPServer extends CoapServer {

    CoapServer server; //主机为localhost 端口为默认端口5683

    public CoAPServer() {
        server = new CoapServer();
    }

    public CoAPServer(int port) {
        server = new CoapServer(port);
    }

    public void addResource(String name, ResourceRule rule) {
        CoAPResource coAPResource = new CoAPResource(name, rule);
        server.add(coAPResource);
    }

    public void start() {
        server.start();
    }
}
