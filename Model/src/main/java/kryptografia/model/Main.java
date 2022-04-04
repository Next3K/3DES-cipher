package kryptografia.model;

import kryptografia.model.cipher.DES;


public class Main {
    public static void main(String[] args) {

        long mainKey = 0xAABB09182736CCDDL;
        long data64bit = 0x123456ABCD132536L;

        DES encryptingDes = new DES(mainKey);
        long encryptedMessage = encryptingDes.encrypt(data64bit);
        DES decryptingDes = new DES(mainKey);
        long decryptedMessage = decryptingDes.decrypt(encryptedMessage);

        System.out.println("Symmetrical encryption-decryption status: " +
                ((decryptedMessage == data64bit) ? "success" : "failure"));
    }
}
