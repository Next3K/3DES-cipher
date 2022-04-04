package kryptografia.model;

import kryptografia.model.cipher.ThreeDes;


public class Main {
    public static void main(String[] args) {

        long key1 = 0xAABB09182736CCDDL;
        long key2 = 0x9182736A0CCDABBDL;
        long key3 = 0x6CCA9182A73BB0DDL;
        long data64bit = 0x123456ABCD132536L;

        ThreeDes threeDes = new ThreeDes(key1, key2, key3);
        long decryptedData = threeDes.decrypt(threeDes.encrypt(data64bit));

        System.out.println("3DES Symmetrical encryption-decryption status: " +
                ((data64bit == decryptedData) ? "success" : "failure"));
    }
}
