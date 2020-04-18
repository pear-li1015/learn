package com;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Random {
    public static void main(String[] args) {
//        double length = (Math.random() * 10 + 7);
////        asc 33-126
//        String code = "";
//        int i = 0;
//        while (i < length) {
//            System.out.print((char)(Math.round(Math.random() * 92) + 33));
//            i ++;
//        }
//        System.out.println(" ");
        test();
    }


    public static void test() {
        Integer a = 10;
        System.out.println(Integer.toOctalString(a));
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toHexString(a));

        c:for (int k = 0; k < 10; k ++) {
            System.out.println("---k");
        }

        b:for (int i = 0; i < 10; i ++) {
            System.out.println(i);
            for (int j = 0; j < 10; j ++) {
                if (j == 9 && i == 3) {
                    break b;
                }
            }
        }

    }
}
