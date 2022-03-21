package kryptografia.model;

import kryptografia.model.cipher.DES;

public class Main {
    public static void main(String[] args) {
        System.out.println("encrypting: " + Long.toBinaryString(12432465412345L));
        long mainKey = 0b00010011_00110100_01010111_01111001_10011011_10111100_11011111_11110001L;
        long data64bit = 0b11111101_1111010_0001111_0010101_0001000_0101010_1011001_1001111_0001111L;
        DES des = new DES(mainKey);
        System.out.printf("encrypted:" + Long.toBinaryString(des.encrypt(data64bit)));
    }
}
