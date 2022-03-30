package kryptografia.model;

import kryptografia.model.cipher.DES;

public class Main {
    public static void main(String[] args) {
        System.out.println("encrypting: " + Long.toBinaryString(5435463L));
        long mainKey = 0b00010011_00110100_01010111_01111001_10011011_10111100_11011111_11110001L;
        long mainKey2 = 0b11111111_00000000_00000000_01000001_10010001_00001100_01000001_11110011L;
        long mainKey3 = 0b00000000_00000100_11010111_01001001_10011011_10111100_11011001_11111111L;
        long mainKey4 = 0b00010011_00110100_01010111_01111001_10011011_10111100_01101111_10011101L;

        long data64bit = 0b0000_0001_0010_0011_0100_0101_0110_0111_1000_1001_1010_1011_1100_1101_1110_1111L;

        DES des = new DES(mainKey);
//        DES des2 = new DES(mainKey2);
//        DES des3 = new DES(mainKey3);
//        DES des4 = new DES(mainKey4);
        System.out.println("encrypted:" + Long.toBinaryString(des.encrypt(data64bit)));
//        System.out.println("encrypted:" + Long.toBinaryString(des2.encrypt(data64bit)));
//        System.out.println("encrypted:" + Long.toBinaryString(des3.encrypt(data64bit)));
//        System.out.println("encrypted:" + Long.toBinaryString(des4.encrypt(data64bit)));
    }
}
