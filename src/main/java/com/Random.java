package com;

public class Random {
    public static void main(String[] args) {
        double length = (Math.random() * 10 + 7);
//        asc 33-126
        String code = "";
        int i = 0;
        while (i < length) {
            System.out.print((char)(Math.round(Math.random() * 92) + 33));
            i ++;
        }
        System.out.println(" ");
    }
}
