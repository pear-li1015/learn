package com.communication.coap;

import com.Random;
import com.coap.dtlsTest.CoAPMessage;
import com.communication.ConstUtil;
import com.communication.Util;
import org.eclipse.californium.elements.Connector;
import org.eclipse.californium.elements.RawData;
import org.eclipse.californium.elements.RawDataChannel;
import org.eclipse.californium.elements.util.DaemonThreadFactory;
import org.eclipse.californium.elements.util.SslContextUtil;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.CertificateType;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: bin
 * @Date: 2019/9/27 17:44
 * @Description:
 */
public class CoAPServer {

    private static final int DEFAULT_PORT = 5684;
    private static final Logger LOG = LoggerFactory
            .getLogger(CoAPServer.class.getName());
    private static final char[] KEY_STORE_PASSWORD = "endPass".toCharArray();
    private static final String KEY_STORE_LOCATION = "certs/keyStore.jks";
    private static final char[] TRUST_STORE_PASSWORD = "rootPass".toCharArray();
    private static final String TRUST_STORE_LOCATION = "certs/trustStore.jks";

    private DTLSConnector dtlsConnector;

    public CoAPServer() {
        InMemoryPskStore pskStore = new InMemoryPskStore();
        // put in the PSK store the default identity/psk for tinydtls tests
        pskStore.setKey("Client_identity", "secretPSK".getBytes());
        try {
            // load the key store
            SslContextUtil.Credentials serverCredentials = SslContextUtil.loadCredentials(
                    SslContextUtil.CLASSPATH_SCHEME + KEY_STORE_LOCATION, "server", KEY_STORE_PASSWORD,
                    KEY_STORE_PASSWORD);
            Certificate[] trustedCertificates = SslContextUtil.loadTrustedCertificates(
                    SslContextUtil.CLASSPATH_SCHEME + TRUST_STORE_LOCATION, "root", TRUST_STORE_PASSWORD);

            DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder();
//            builder.setRecommendedCipherSuitesOnly(false);
            builder.setAddress(new InetSocketAddress(DEFAULT_PORT));
            builder.setPskStore(pskStore);
            builder.setIdentity(serverCredentials.getPrivateKey(), serverCredentials.getCertificateChain(),
                    CertificateType.RAW_PUBLIC_KEY, CertificateType.X_509);
            builder.setTrustStore(trustedCertificates);
            builder.setRpkTrustAll();
            dtlsConnector = new DTLSConnector(builder.build());
            dtlsConnector
                    .setRawDataReceiver(new CoAPServer.RawDataChannelImpl(dtlsConnector));

        } catch (GeneralSecurityException | IOException e) {
            LOG.error("Could not load the keystore", e);
            System.out.println("Could not load the keystore");
        }
    }

    public void start() {
        try {
            dtlsConnector.start();
            System.out.println("DTLS example server started");
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unexpected error starting the DTLS UDP server", e);
        }
    }

    private class RawDataChannelImpl implements RawDataChannel {

        private Connector connector;

        public RawDataChannelImpl(Connector con) {
            this.connector = con;
        }

        @Override
        public void receiveData(final RawData raw) {
//            if (LOG.isInfoEnabled()) {
//                LOG.info("Received request: {}", new String(raw.getBytes()));
//            }

            /** server 处理接收到数据的逻辑 ， 有时会调用回调函数， 有时会 返回请求 。 不管怎样都会再压入 list*/

            // 根据 raw获取 message
            CoAPMessage message = Util.transBytesToMessage(raw.getBytes());
            if (message.getState() == ConstUtil.MESSAGE_NEED_RESPONSE) {
                // TODO 如果需要从此server返回, 直接从这里返回 请求结果
                RawData response = RawData.outbound("ACK".getBytes(),
                        raw.getEndpointContext(), null, false);
                connector.send(response);
            } else {
                // message 压入 待处理列表
                // 这里为了保证通信效率，直接将message压入list
                MessageList.getPreHandList().add(message);
            }

            System.out.println("server receive a message, 当前 PreHandList 的长度为： " + MessageList.getPreHandList().size());

        }
    }

    public void startAServer() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                new DaemonThreadFactory("Aux" + new Date().getTime() + "#"));
        executor.execute(new Runnable() {

            @Override
            public void run() {
                CoAPServer server = new CoAPServer();
                server.start();
                System.out.println("a server is started ...");
                try {
                    for (;;) {
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {
                }
            }
        });
    }

}
