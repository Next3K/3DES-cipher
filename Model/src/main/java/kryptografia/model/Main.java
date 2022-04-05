package kryptografia.model;

import kryptografia.model.cipher.ThreeDes;
import kryptografia.model.helper.Helper;

public class Main {
    public static void main(String[] args) {

        long[] keys = Helper.generateKeysFromPassword("passwordToCipher");

        long data64bit = 0x123456ABCD132536L;
        long key1 = keys[0];
        long key2 = keys[1];
        long key3 = keys[2];

        System.out.print("Key one  : ");
        Helper.printBits(key1);

        System.out.print("Key two  : ");
        Helper.printBits(key2);

        System.out.print("Key three: ");
        Helper.printBits(key3);


        ThreeDes threeDes = new ThreeDes(key1, key2, key3);

        long encryptedData = threeDes.encrypt(data64bit);
        long decryptedData = threeDes.decrypt(encryptedData);


        System.out.println("3DES Symmetrical encryption-decryption status: " +
                ((data64bit == decryptedData) ? "success" : "failure"));
    }

}
