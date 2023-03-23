package org.example;


import java.util.*;


public class Main {
    public static void main(String[] args) {

        String[] entryStr = new String[2];
        entryStr[0] = "02, 79876543221, 20230321160455, 20230321163211, 11";
        entryStr[1] = "02, 79876543221, 20230321160455, 20230321163211, 11";

        List<CDR> cdrs = new ArrayList<>();
        cdrs.add(0, new CDR(entryStr[0]));
        cdrs.add(1, new CDR(entryStr[1]));

        // ввод номера
        System.out.println("Hello world!");
    }
}