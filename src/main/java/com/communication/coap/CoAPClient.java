package com.communication.coap;

import com.coap.dtls.ExampleDTLSClient;
import com.coap.dtlsTest.CoAPMessage;
import com.communication.ConstUtil;
import com.communication.Util;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.eclipse.californium.elements.*;
import org.eclipse.californium.elements.util.DaemonThreadFactory;
import org.eclipse.californium.elements.util.SslContextUtil;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.CertificateType;
import org.eclipse.californium.scandium.dtls.pskstore.StaticPskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: bin
 * @Date: 2019/9/27 16:53
 * @Description:
 */
public class CoAPClient {
    private static final int DEFAULT_PORT = 5684;
    private static final long DEFAULT_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(10000);
    private static final char[] KEY_STORE_PASSWORD = "endPass".toCharArray();
    private static final String KEY_STORE_LOCATION = "certs/keyStore.jks";
    private static final char[] TRUST_STORE_PASSWORD = "rootPass".toCharArray();
    private static final String TRUST_STORE_LOCATION = "certs/trustStore.jks";
    private DTLSConnector dtlsConnector;

    public CoAPClient() {
        try {
            // load key store
            SslContextUtil.Credentials clientCredentials = SslContextUtil.loadCredentials(
                    SslContextUtil.CLASSPATH_SCHEME + KEY_STORE_LOCATION, "client", KEY_STORE_PASSWORD,
                    KEY_STORE_PASSWORD);
            Certificate[] trustedCertificates = SslContextUtil.loadTrustedCertificates(
                    SslContextUtil.CLASSPATH_SCHEME + TRUST_STORE_LOCATION, "root", TRUST_STORE_PASSWORD);

            DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder();
            builder.setPskStore(new StaticPskStore("Client_identity", "secretPSK".getBytes()));
            builder.setIdentity(clientCredentials.getPrivateKey(), clientCredentials.getCertificateChain(),
                    CertificateType.RAW_PUBLIC_KEY, CertificateType.X_509);
            builder.setTrustStore(trustedCertificates);
            builder.setRpkTrustAll();
            builder.setConnectionThreadCount(1);
            dtlsConnector = new DTLSConnector(builder.build());
            dtlsConnector.setRawDataReceiver(new RawDataChannel() {

                @Override
                public void receiveData(RawData raw) {
                    System.out.println("client receive data ");
                    if (dtlsConnector.isRunning()) {
                        // TODO 服务器的这里不应该有任何逻辑， 只有client的设备，必须从此接收响应。
//                        receive(raw);

                    }
                }
            });

        } catch (GeneralSecurityException | IOException e) {
            System.out.println("Could not load the keystore" + e);
        }
    }

    private void start() {
        try {
            dtlsConnector.start();

        } catch (IOException e) {
            System.out.println("Cannot start connector" + e);
        }
    }

    private void startTest(InetSocketAddress peer) {
//        CoAPMessage message = new CoAPMessage("to", "content".getBytes());
//        message.send();
//        RawData data = RawData.outbound(Util.transMessageToBytes(message), new AddressEndpointContext(peer), null, false);
//        dtlsConnector.send(data);

        while (true) {
            System.out.println("当前 preSendList 的长度为" + MessageList.getPreSendList().size());
            List<CoAPMessage> messageList = MessageList.getPreSendList();
            if (messageList.size() > 0) {
                System.out.println("client send a message, 当前 list 的长度为： " + messageList.size());
                CoAPMessage message1 = messageList.remove(0);
                // 如果 此message需要在此 服务器上执行回调
                if (message1.getState() == ConstUtil.MESSAGE_HIS_CALLBACK) {
                    MessageList.getPreCallBackList().add(message1);
                }
//                System.out.println(message1);
//                System.out.println(Util.transMessageToBytes(message1).length);
                RawData data1 = RawData.outbound(Util.transMessageToBytes(message1), new AddressEndpointContext(peer), null, false);
                dtlsConnector.send(data1);
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startAClient() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                new DaemonThreadFactory("Aux#"));
        final CoAPClient client = new CoAPClient();
        final InetSocketAddress peer = new InetSocketAddress(InetAddress.getLoopbackAddress(), DEFAULT_PORT);
        executor.execute(new Runnable() {

            @Override
            public void run() {
                client.start();
                System.out.println("one client is started ...");
                client.startTest(peer);
            }
        });

    }


}
