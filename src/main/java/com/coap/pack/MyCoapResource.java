package com.coap.pack;

import com.coap.MyResourceRule;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: bin
 * @Date: 2019/9/16 21:23
 * @Description:
 */
public class MyCoapResource extends CoapResource {
    private Class clazz;
    private MyCoapResource(String name) {
        super(name);
    }

    private MyCoapResource(String name, boolean visible) {
        super(name, visible);
    }


    public MyCoapResource(String name, Class clazz) {
        super(name);
        this.clazz = clazz;
    }

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
        Method method = null;
        MyResourceRule myResourceRule = new MyResourceRule();
        try {
            method = clazz.getDeclaredMethod("returnFile", String.class);
            File file = (File)method.invoke(myResourceRule, request.getOptions().getUriPathString());
            transFileToBytes(file);
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
                    System.out.println("--response--");
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
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            return;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            return;
        }
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

}
