package kryptografia.model.cipher;

import kryptografia.model.helper.Helper;
import kryptografia.model.helper.Tables;

public class DES {

    private final long mainKey;
    private String message;
    private long[] subKeys = new long[16];
    private long smallerMainKey; // 56 bit key created form main key.

    public DES(long key, String message) {
        this.message = message;
        this.mainKey = key;
    }

    /**
     * Generates 56 bit sub-key from main key.
     */
    public void generateSmallerMainKey() {
        long smallerMainKey = 0;
        for (int i = 0; i < Tables.PC1.length; i++) {
            byte valueToSet = (byte) Helper.getBit(mainKey,64 - Tables.PC1[i]);
            smallerMainKey = Helper.setBit(smallerMainKey,55 - i,valueToSet);
        }
        this.smallerMainKey = smallerMainKey;
    }
    

    public long getSmallerMainKey() {
        return smallerMainKey;
    }
}
