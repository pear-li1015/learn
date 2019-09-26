package com.flume;

import org.apache.log4j.Logger;

/**
 * @Author: bin
 * @Date: 2019/9/23 13:59
 * @Description:
 */
public class FlumeTest3 {
    private static Logger log = Logger.getLogger(FlumeTest3.class);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i ++ ) {
            log.info("this is a log. 这是一个log。");
            log.debug("this is a debug.");
            log.error("this is a error.");
            log.warn("this is a warn.");
        }
    }
}
