package com.coap.integration;

import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @Author: bin
 * @Date: 2019/9/18 17:56
 * @Description:
 */
public class CoAPUtil {



    /**
     * 参见 {@link MediaTypeRegistry} 类
     * @param format
     * @return
     */
    String getSuffixByFormat(int format) {
        String result = "";
        switch (format) {
            case MediaTypeRegistry.UNDEFINED:
                result = "???";
            break;
            // TEXT_PLAIN
            case MediaTypeRegistry.TEXT_PLAIN:
                result = "txt";
            break;
            case MediaTypeRegistry.TEXT_CSV:
                result = "csv";
            break;
            case MediaTypeRegistry.TEXT_HTML:
                result = "html";
            break;
            case MediaTypeRegistry.IMAGE_GIF:
                result = "gif";
                break;
            case MediaTypeRegistry.IMAGE_JPEG:
                result = "jpg";
                break;
            case MediaTypeRegistry.IMAGE_PNG:
                result = "png";
                break;
            case MediaTypeRegistry.IMAGE_TIFF:
                result = "tif";
                break;
            case MediaTypeRegistry.APPLICATION_LINK_FORMAT:
                result = "wlnk";
                break;
            case MediaTypeRegistry.APPLICATION_XML:
                result = "xml";
                break;
            case MediaTypeRegistry.APPLICATION_OCTET_STREAM:
                result = "bin";
                break;
            case MediaTypeRegistry.APPLICATION_RDF_XML:
                result = "rdf";
                break;
            case MediaTypeRegistry.APPLICATION_SOAP_XML:
                result = "soap";
                break;
            case MediaTypeRegistry.APPLICATION_ATOM_XML:
                result = "atom";
                break;
            case MediaTypeRegistry.APPLICATION_XMPP_XML:
                result = "xmpp";
                break;
            case MediaTypeRegistry.APPLICATION_EXI:
                result = "exi";
                break;
            case MediaTypeRegistry.APPLICATION_FASTINFOSET:
                result = "finf";
                break;
            case MediaTypeRegistry.APPLICATION_SOAP_FASTINFOSET:
                result = "soap.finf";
                break;
            case MediaTypeRegistry.APPLICATION_JSON:
                result = "json";
                break;
            case MediaTypeRegistry.APPLICATION_X_OBIX_BINARY:
                result = "obix";
                break;
            case MediaTypeRegistry.APPLICATION_CBOR:
                result = "cbor";
                break;
            case MediaTypeRegistry.APPLICATION_SENML_JSON:
                result = "json";
                break;
            case MediaTypeRegistry.APPLICATION_SENML_CBOR:
                result = "cbor";
                break;
            case MediaTypeRegistry.APPLICATION_VND_OMA_LWM2M_TLV:
                result = "tlv";
                break;
            case MediaTypeRegistry.APPLICATION_VND_OMA_LWM2M_JSON:
                result = "json";
                break;
        }
        return result;
    }
}
