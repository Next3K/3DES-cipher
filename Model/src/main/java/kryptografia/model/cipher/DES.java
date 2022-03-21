package kryptografia.model.cipher;

import kryptografia.model.helper.Helper;
import kryptografia.model.helper.Tables;

public class DES {

    private final long mainKey;
    private final long[] subKeys = new long[16];
    private long smallerMainKey; // 56 bit key created form main key.
    private int C0 = 0;
    private int D0 = 0;
    private long data64bit = 0;
    private long encryptedData64bit = 0;

    public DES(long key) {
        this.mainKey = key;
    }

    /**
     * Public function encrypting 64 bits of data.
     * @param data64bit 64 bits of data, long number.
     * @return encrypted 64 bits, long number.
     */
    public long encrypt(long data64bit) {
        this.setData64bit(data64bit); // set block of data to encrypt
        this.initialDataBlockPermutation(); // permutation of 64 bit initial data.
        this.generateSmallerMainKey(); // generate 56bit key from 64bit main key.
        this.create28BitHalfKeys(); // divide 56 bit key into two 28bit parts.
        this.generateSubKeys(); // generate 16 48bit sub-keys.
        this.execute16Rounds(); // 16 rounds of encryption.
        return this.encryptedData64bit; // return encrypted data.
    }

    /**
     * Public function decrypting 64 bits of data/
     * @param data64bit 64 bits of data.
     * @return decrypted 64bit data.
     */
    public long decrypt(long data64bit) {
        // TODO link all methods to decrypt block of data
        return 0;
    }

    /**
     * Go through 16 rounds.
     */
    public void execute16Rounds() {
        int L0 = (int) Helper.extractBits(this.data64bit,32,32);
        int R0 = (int) Helper.extractBits(this.data64bit,32,0);
        nthRound(1,L0,R0);

    }

    /**
     * Execute single round.
     * @param n number of round (1-16)
     */
    private void nthRound(int n, int L, int R) {
        if (n <= 16) {
            int index = n - 1; // array index (0 - 15)
            long subKey = this.subKeys[index];
            int newR = L ^ fFunction(R, subKey);
            nthRound(n + 1,R,newR);
        } else {
            this.encryptedData64bit = finalPermutation(L, R);
        }
    }

    /**
     * Final permutation after 16 rounds.
     * @param L 32bit number, left side.
     * @param R 32bit number, right side.
     * @return combined and permuted 64 bits.
     */
    public long finalPermutation(int L, int R) {
        long rValue;
        rValue = Helper.setBits(L,32,0L,32);
        rValue = Helper.setBits(R,32,0L,0);
        long rValCopy = rValue;
        for (int i = 1; i <= Tables.IP_1.length; i++) {
            int positionOfBit = 64 - Tables.IP[i - 1]; // get position of bit inside 64bit key
            int valueOfBit = Helper.getBit(rValCopy, positionOfBit);
            rValue = Helper.setBit(rValue,64 - i,valueOfBit);
        }
        return rValue;
    }

    /**
     * f function encrypting.
     * @param R 32bit int.
     * @param subKey sub-key from generated sub-keys array.
     * @return new 32bit number.
     */
    public int fFunction(int R, long subKey) {
        long expanded48BitR = 0;
        for (int i = 1; i <= Tables.EBIT.length; i++) { // length = 48
            int positionOfBit = 32 - Tables.IP[i - 1]; // get position of bit inside 64bit key
            int valueOfBit = Helper.getBit(R, positionOfBit);
            expanded48BitR = Helper.setBit(expanded48BitR,64 - i,valueOfBit);
        }

        long xor48bit = expanded48BitR ^ subKey;

        int new32bit = 0;
        for (int n = 0; n < 8; n++) {
            int bits = (int) Helper.extractBits(xor48bit,6, n * 6); // extract group of 6 bits
            int newBits = sbox(n + 1, bits); // returns int with 4 important bits
            new32bit = Helper.setBits(newBits,4,new32bit,4 * n);
        }

        int copy32bit = new32bit;

        for (int i = 1; i <= Tables.PF.length; i++) {
            int valueToSet = Helper.getBit(copy32bit,Tables.PF[i - 1]);
            new32bit = Helper.setBit(new32bit, 32 - i,valueToSet);
        }
        return new32bit;
    }

    /**
     * Sbox function.
     * @param n number of sbox.
     * @param bits int: 6 bits important.
     * @return int: 4 bits important
     */
    public int sbox(int n, int bits) {
        int[][] table = switch (n) {
            case 1 -> Tables.S1;
            case 2 -> Tables.S2;
            case 3 -> Tables.S3;
            case 4 -> Tables.S4;
            case 5 -> Tables.S5;
            case 6 -> Tables.S6;
            case 7 -> Tables.S7;
            case 8 -> Tables.S8;
            default -> null;
        };
        int column = Helper.extractBits(bits,4,1);
        int row = 0;
        int firstBit = Helper.getBit(bits,5);
        int lastBit = Helper.getBit(bits,0);
        row = Helper.setBit(row, 1, firstBit);
        row = Helper.setBit(row, 0, lastBit);
        assert table != null;
        return table[row][column];
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


    public void create28BitHalfKeys(){
        this.D0 = (int) Helper.extractBits(smallerMainKey,28,0); // get 28 bit key from 56 bit key
        this.C0 = (int) Helper.extractBits(smallerMainKey,28,28); // get 28 bit key from 56 bit key
    }
    /**
     * Generate 16 sub-keys
     */
    public void generateSubKeys() {
        this.create28BitHalfKeys(); // set proper C0 & D0
        for (int i = 0 ; i < Tables.SHIFTS.length; i++) { // Tables.SHIFTS.length = 16
            int numberOfLeftShifts = Tables.SHIFTS[i];
            int CX = Helper.leftShift(C0, numberOfLeftShifts, 28);
            int DX = Helper.leftShift(D0, numberOfLeftShifts, 28);
            long CD;
            CD = Helper.setBits(CX,28,0L,28); // create long form int
            CD = Helper.setBits(DX,28,0L,0); // create long form int
            long tmp = 0;
            for (int j = 1; j <= Tables.PC1.length; j++) { // Tables.PC1.length = 48
                int positionOfBit = 56 - Tables.PC1[j - 1]; // get position of bit inside 56 bit key
                int bitValueToSet = Helper.getBit(CD, positionOfBit); // get value of bit
                int positionOfBitInLong = 48 - j;
                tmp = Helper.setBit(tmp, positionOfBitInLong, bitValueToSet); // set value
            }
            this.subKeys[i] = tmp;
        }
    }

    /**
     * initial permutation of 64bit message with IP table.
     */
    public void initialDataBlockPermutation() {
        long messageCopy = this.data64bit; // copy the initial data
        for (int i = 1; i <= Tables.IP.length; i++) { // form i = 1 to i = 64
            int positionOfBit = 64 - Tables.IP[i - 1]; // get position of bit inside 64bit key
            int valueOfBit = Helper.getBit(messageCopy, positionOfBit);
            data64bit = Helper.setBit(data64bit,64 - i,valueOfBit);
        }
    }

    public void setData64bit(long data64bit) {
        this.data64bit = data64bit;
    }

    public long getSmallerMainKey() {
        return smallerMainKey;
    }

    public long getData64bit() {
        return data64bit;
    }

    public int getC0() {
        return C0;
    }

    public int getD0() {
        return D0;
    }
}
