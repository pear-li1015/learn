package com.tomcat2.servlet;

import com.tomcat2.utils.FileUtil;
import com.tomcat2.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: bin
 * @Date: 2019/9/8 9:51
 * @Description:
 */
public class Response {

    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String content) {
        try {
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHtml(String path) throws Exception {
        String resoucePath = FileUtil.getResourcePath(path);
        File file = new File(resoucePath);
        if (file.exists()) {
            FileUtil.writeFile(file, outputStream);
        } else {
            HttpUtil.getHttpResponseContext404();
        }
    }
}
