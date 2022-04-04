package kryptografia.model.cipher;

import kryptografia.model.helper.Helper;
import kryptografia.model.helper.Tables;

public class DES {

    private final long mainKey;
    private final long[] subKeys = new long[16];
    private long smallerMainKey; // 56 bit key created from main key.
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
        System.out.println("Encryption process starts.");
        this.setData64bit(data64bit); // set block of data to encrypt
        this.initialDataBlockPermutation(); // permutation of 64 bit initial data.
        this.generateSmallerMainKey(); // generate 56bit key from 64bit main key.
        this.create28BitHalfKeys(); // divide 56 bit key into two 28bit parts.
        this.generateSubKeys(); // generate 16 48bit sub-keys.
        this.execute16RoundsOfEncryption(); // rounds of encryption + final permutation
        System.out.println('\n');
        System.out.println("FINAL ENCRYPTED DATA: " + Long.toHexString(this.encryptedData64bit));
        return this.encryptedData64bit; // return encrypted data.
    }

    /**
     * Public function decrypting 64 bits of data/
     * @param data64bit 64 bits of data.
     * @return decrypted 64bit data.
     */
    public long decrypt(long data64bit) {
        System.out.println("Decryption process starts.");
        this.setData64bit(data64bit); // set block of data to encrypt
        this.initialDataBlockPermutation(); // permutation of 64 bit initial data.
        this.generateSmallerMainKey(); // generate 56bit key from 64bit main key.
        this.create28BitHalfKeys(); // divide 56 bit key into two 28bit parts.
        this.generateSubKeys(); // generate 16 48bit sub-keys.
        this.execute16RoundsOfDecryption(); // rounds of decryption.
        System.out.println('\n');
        System.out.println("FINAL DECRYPTED DATA: " + Long.toHexString(this.encryptedData64bit));
        return this.encryptedData64bit; // return decrypted data.
    }

    /**
     * Go through 16 rounds of encryption.
     */
    public void execute16RoundsOfEncryption() {
        int L0 = (int) Helper.extractBits(this.data64bit,32,32);
        int R0 = (int) Helper.extractBits(this.data64bit,32,0);
        nthRound(1,L0,R0,false);

    }

    /**
     * Go through 16 rounds of decryption.
     */
    public void execute16RoundsOfDecryption() {
        int L0 = (int) Helper.extractBits(this.data64bit,32,32);
        int R0 = (int) Helper.extractBits(this.data64bit,32,0);
        nthRound(1,L0,R0,true);

    }

    /**
     * Execute single round.
     * @param n number of round (1-16)
     */
    private void nthRound(int n, int L, int R, boolean decrypting) {
        int subKeyNumber = (decrypting) ? 17 - n : n; // first key when encrypting 1, when decrypting 16
        if (n <= 16 && n >= 1) {
            System.out.println();
            System.out.println("INFO: Round: " + n + " " + ((decrypting) ? "decrypting" : "encrypting") );
            System.out.printf("   L part: " + Integer.toHexString(L));
            System.out.println("   R part: " + Integer.toHexString(R));
            long subKey = this.getKey(subKeyNumber);
            int fFunctionOutput = fFunction(R, subKey);
            int newR = L ^ fFunctionOutput;
            if (n != 16) {
                System.out.printf("new L part: " + Integer.toHexString(L));
                System.out.printf("new R part: " + Integer.toHexString(newR));
            } else {
                System.out.printf("    new L part: " + Integer.toHexString(newR));
                System.out.println("    new R part: " + Integer.toHexString(R));
            }

            if (n == 16) { // last permutation
                this.encryptedData64bit = finalPermutation(newR, R); // last round, no swapping
            }
            nthRound(n + 1,R,newR,decrypting);
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
        rValue = Helper.setBits(R,32,rValue,0);
        System.out.printf("    Final permutation input: " + Long.toHexString(rValue));
        long rValCopy = rValue;
        for (int i = 1; i <= Tables.IP_1.length; i++) {
            int positionOfBit = 64 - Tables.IP_1[i - 1]; // get position of bit inside 64bit key
            int valueOfBit = Helper.getBit(rValCopy, positionOfBit);
            rValue = Helper.setBit(rValue,64 - i,valueOfBit);
        }
        System.out.printf("    Final permutation output: " + Long.toHexString(rValue));
        System.out.println();
        System.out.println();
        System.out.println();
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
            int positionOfBit = 32 - Tables.EBIT[i - 1]; // get position of bit inside 64bit key
            int valueOfBit = Helper.getBit(R, positionOfBit);
            expanded48BitR = Helper.setBit(expanded48BitR,48 - i,valueOfBit);
        }
        System.out.println("(48 bit) F(): Expanded: ");
        Helper.printBits(expanded48BitR);

        System.out.println("(48 bit) F(): 48bit XOR sub-key: ");
        Helper.printBits(expanded48BitR);
        System.out.println(" XOR ");
        Helper.printBits(subKey);

        long xor48bit = expanded48BitR ^ subKey;

        System.out.println("(48 bit) F(): XOR output ");
        Helper.printBits(xor48bit);

        int new32bit = 0;
        for (int n = 7; n >= 0; n--) {
            int beginIndex = n * 6;
            int numberOfBits = 6;
            int bits = (int) Helper.extractBits(xor48bit,numberOfBits, beginIndex); // extract group of 6 bits
            int newBits = sbox(8 - n, bits); // returns int with 4 important bits
            new32bit = Helper.setBits(newBits,4,new32bit,4 * n);
        }


        int copy32bit = new32bit;

        System.out.println("(32 bit) F(): Compressed to 32 bits");
        Helper.printBits(new32bit);
        for (int i = 1; i <= Tables.PF.length; i++) { // Works fine
            int indexInTable = Tables.PF[i - 1];
            int valueToSet = Helper.getBit(copy32bit, 32 - indexInTable);
            //System.out.printf("(32bi) Val before setting " + valueToSet + " at " + i + ": ");
            //Helper.printBits(new32bit);
            new32bit = Helper.setBit(new32bit, 32 - i,valueToSet);
            //System.out.printf("(32bi) Val after  setting " + valueToSet + " at " + i + ": ");
            //Helper.printBits(new32bit);
        }
        System.out.println("(32 bit) F(): Permutation after s-block applied: ");
        Helper.printBits(new32bit);
        return new32bit;
    }

    /**
     * S-box function.
     * @param n number of s-box.
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
        for (int i = 1; i <= Tables.PC1.length; i++) {
            byte valueToSet = (byte) Helper.getBit(mainKey,64 - Tables.PC1[i - 1]);
            smallerMainKey = Helper.setBit(smallerMainKey,56 - i,valueToSet);
        }
        this.smallerMainKey = smallerMainKey;
        System.out.printf("Smaller main key: ");
        Helper.printBits(this.smallerMainKey);
    }


    public void create28BitHalfKeys(){
        this.D0 = (int) Helper.extractBits(smallerMainKey,28,0); // get 28 bit key from 56 bit key
        this.C0 = (int) Helper.extractBits(smallerMainKey,28,28); // get 28 bit key from 56 bit key
        System.out.printf("C0 (28 bits)");
        Helper.printBits(C0);
        System.out.printf("D0 (28 bits)");
        Helper.printBits(D0);
    }
    /**
     * Generate 16 sub-keys
     */
    public void generateSubKeys() {
        int sourceCN = C0;
        int sourceDN = D0;
        for (int i = 0; i < Tables.LEFT_SHIFTS.length; i++) { // Tables.SHIFTS.length = 16
            int numberOfLeftShifts = Tables.LEFT_SHIFTS[i];

            int CX = Helper.leftShift(sourceCN, numberOfLeftShifts, 28);
            int DX = Helper.leftShift(sourceDN, numberOfLeftShifts, 28);

            sourceCN = CX;
            sourceDN = DX;

            long CD;
            CD = Helper.setBits(CX,28,0L,28);
            CD = Helper.setBits(DX,28,CD,0); // create long form int

            System.out.printf("(56bit) Sub-key " + i + " after " + numberOfLeftShifts + " left shifts applied, parts connected:  ");
            Helper.printBits(CD);
            long tmp = 0;
            for (int j = 1; j <= Tables.PC2.length; j++) { // Tables.PC2.length = 48
                int positionOfBit = 56 - Tables.PC2[j - 1]; // get position of bit inside 56 bit key
                int bitValueToSet = Helper.getBit(CD, positionOfBit); // get value of bit
                int positionOfBitInLong = 48 - j;
                tmp = Helper.setBit(tmp, positionOfBitInLong, bitValueToSet); // set value
            }
            this.subKeys[i] = tmp;
            System.out.printf("(48bit) Sub-key " + i + " after permutation compressing to 48bits:       ");
            Helper.printBits(tmp);
        }
    }

    public long getKey(int n) {
        return this.subKeys[n - 1];
    }

    /**
     * initial permutation of 64bit message with IP table.
     */
    public void initialDataBlockPermutation() {
        long messageCopy = this.data64bit; // copy the initial data
        System.out.println("INFO: initial permutation input:");
        Helper.printBits(messageCopy);
        for (int i = 1; i <= Tables.IP.length; i++) { // form i = 1 to i = 64
            int positionOfBit = 64 - Tables.IP[i - 1]; // get position of bit inside 64bit key
            int valueOfBit = Helper.getBit(messageCopy, positionOfBit);
            data64bit = Helper.setBit(data64bit,64 - i,valueOfBit);
        }
        System.out.println("INFO: initial permutation output:");
        Helper.printBits(data64bit);
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

    public long[] getSubKeys() {
        return this.subKeys;
    }
}
