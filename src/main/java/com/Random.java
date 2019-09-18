package com;

public class Random {
    public static void main(String[] args) {
        double length = (Math.random() * 10 + 5);
//        asc 33-126
        String code = "";
        int i = 0;
        while (i < length) {
            System.out.print((char)Math.random());
            i ++;
        }
        System.out.println(" ");
    }
}
