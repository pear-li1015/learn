package com.coap;

import java.io.File;

public abstract class ResourceRule {

    public File returnFile(String rule) {// 规则类型未定

        return new File("D:\\test\\file.jpg");
    }
}
