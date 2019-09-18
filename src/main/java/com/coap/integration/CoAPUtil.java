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
     *
     */
    void saveFile(byte[] payload, int format, String dir) throws Exception {
        String fileName = "asdf";
        File file = new File(dir + "//" + fileName + "." + getSuffixByFormat(format));

        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(payload);

    }

    /**
     * 参见 {@link MediaTypeRegistry} 类
     * @param format
     * @return
     */
    String getSuffixByFormat(int format) {
        String result = "";
        switch (format) {
            // TEXT_PLAIN
            case MediaTypeRegistry.TEXT_PLAIN:
                result = "";
            break;
            case MediaTypeRegistry.APPLICATION_OCTET_STREAM:
                result = "";
        }
        return result;
    }
}
