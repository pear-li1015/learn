package com.coap.dtls;

/**
 * @Author: bin
 * @Date: 2019/9/20 9:34
 * @Description:
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;

import com.coap.dtlsTest.CoAPMessage;
import com.communication.Util;
import com.communication.coap.MessageList;
import org.eclipse.californium.elements.Connector;
import org.eclipse.californium.elements.RawData;
import org.eclipse.californium.elements.RawDataChannel;
import org.eclipse.californium.elements.util.SslContextUtil;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.CertificateType;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleDTLSServer {

    private static final int DEFAULT_PORT = 5684;
    private static final Logger LOG = LoggerFactory
            .getLogger(ExampleDTLSServer.class.getName());
    private static final char[] KEY_STORE_PASSWORD = "endPass".toCharArray();
    private static final String KEY_STORE_LOCATION = "certs/keyStore.jks";
    private static final char[] TRUST_STORE_PASSWORD = "rootPass".toCharArray();
    private static final String TRUST_STORE_LOCATION = "certs/trustStore.jks";

    private DTLSConnector dtlsConnector;

    public ExampleDTLSServer() {
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
                    .setRawDataReceiver(new RawDataChannelImpl(dtlsConnector));

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
            if (LOG.isInfoEnabled()) {
                LOG.info("Received request: {}", new String(raw.getBytes()));
            }

            CoAPMessage message = Util.transBytesToMessage(raw.getBytes());
            MessageList.getPreHandList().add(message);
            MessageList.getPreHandList().add(message);
            MessageList.getPreHandList().add(message);
            System.out.println("server receive a message");
            System.out.println(MessageList.getPreHandList().size());

//            System.out.println("receive data " + new String(raw.getBytes()));
            RawData response = RawData.outbound("ACK------------".getBytes(),
                    raw.getEndpointContext(), null, false);
            connector.send(response);

        }
    }

    public static void main(String[] args) {

        ExampleDTLSServer server = new ExampleDTLSServer();
        server.start();
        try {
            for (;;) {
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
        }
    }
}
