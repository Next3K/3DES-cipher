package kryptografia.model.helper;

/**
 *  Class with helpful functions.
 */
public class Helper {

    /**
     * Print bit representation of number.
     * Example input:  0x19BA9212CF26B472 (hexadecimal format)
     * Example output: 00010010_00110100_01010110_10101011_11001101_00010011_00100101_00110110
     * @param number 64bit number.
     */
    public static void printBits (long number) {
        String str = Long.toBinaryString(number);
        int missingFirstBits = 64 - str.length();
        str = "0".repeat(missingFirstBits) + str; // pad left most "empty" space with zeros
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i ++) {
            int beginIndex = i * 8;
            String tmp = str.substring(beginIndex, beginIndex + 8);
            builder.append(tmp);
            if (i != 7) {
                builder.append("_"); // add spacing after every 8 bits
            }
        }
        System.out.println(builder);
    }



    /**
     * Get the bit value inside the number at given position.
     * Position 0 represents right-most bit.
     * Position k represents kth position counting from right to left.
     * @param src source number
     * @param position position of bit; from 0 (right most bit) to n - 1 (left-most bit) in n bit number.
     * @return 0 or 1.
     */
    public static int getBit(int src, int position){
        return (src >> position) & 1;
    }

    /**
     * Get bit at position.
     * @param src source number
     * @param position position of bit; from 0 (right most bit) to n - 1 (left-most bit) in n bit number.
     * @return 0 or 1.
     */
    public static int getBit(long src, int position){
        return (int) ((src >> position) & 1);
    }


    /**
     * Set a bit of a given int number.
     * @param src int number.
     * @param position position of bit; from 0 (right most bit) to n - 1 (left-most bit) in n bit number.
     * @param value value to set.
     * @return value with changed bit.
     */
    public static int setBit(int src, int position, int value){
        if (value == 0) { // value = 0
            src &= ~(1 << position);
        } else {  // value = 1
            src |= 1 << position;
        }
        return src;
    }


    /**
     * Set a bit of a given long number.
     * @param src long number.
     * @param position position of bit; from 0 (right most bit) to n - 1 (left-most bit) in n bit number.
     * @param value value to set.
     * @return value with changed bit.
     */
    public static long setBit(long src, int position, int value){
        if (value == 0) { // value = 0
            src &= ~(1L << position);
        } else {  // value = 1
            src |= 1L << position;
        }
        return src;
    }

    /**
     * Returns k bits from number.
     * @param number input number - number to extract bits from.
     * @param k number of bits to extract.
     * @param pos position of first bit to extract;
     *            positions range from 0 (right most bit) to n - 1 (left-most bit) in n bit number.
     * @return number representing extracted bits
     */
    public static int extractBits(int number, int k, int pos) {
        pos++;
        return ((1 << k) - 1) & (number >> (pos - 1));
    }


    /**
     * right most bit has an index 0. Returns bits [pos + n - 1, ... ,pos] total: n
     * @param number input number.
     * @param k number of bits to extract.
     * @param pos position of first bit to extract
     * @return number representing extracted bits
     */
    public static long extractBits(long number, int k, int pos) { // left most bit has position 0
        pos++;
        return ((1L << k) - 1) & (number >> (pos - 1));
    }

    /**
     * Circular left shift.
     * @param n number to shift.
     * @param k number of places to shift.
     * @return shifted number
     */
    public static int leftShift(int n, int k, int importantBits)
    {
        k = k % importantBits; // when left shift number is bigger than important bits
        if (importantBits == 32) { // full int capacity
            int leftBits = Helper.extractBits(n,k,importantBits - k);
            n = (n << k);
            n = Helper.setBits(leftBits,k,n,0);
            return n;
        }
        int numberShifted;
        int bitsLeft = 32 - importantBits; // 32 int capacity
        if (k <= bitsLeft) {
            numberShifted = (n << k) | (n >> (Integer.SIZE - k));
            int offsetBita = Helper.extractBits(numberShifted,k,importantBits);
            numberShifted = Helper.setBits(offsetBita,k,numberShifted,0);
            numberShifted = Helper.setBits(0,k,numberShifted,importantBits);
            return numberShifted;
        } else {
            return Helper.leftShift(n, k - bitsLeft,importantBits);
        }
    }

    /**
     * Circular right shift.
     * @param n number to shift.
     * @param k number of places to shift.
     * @return shifted number
     */
    public static int rightShift(int n, int k, int importantBits)
    {
        k = k % importantBits; // when right shift number is bigger than important bits
        int notImportantBits = 32 - importantBits;
        int rightmostBits = Helper.extractBits(n,k,0); // k bits that can not be forgotten
        int numberShifted = (n >> k) | (n << (Integer.SIZE - k)); // shift k places
        numberShifted = Helper.setBits(0,notImportantBits,numberShifted,importantBits);
        numberShifted = Helper.setBits(rightmostBits,k,numberShifted,importantBits - k);
        return numberShifted;
    }

    /**
     * Sets a subset of bits inside another set of bits.
     * @param sourceNumber set of bits to set.
     * @param numberOfBits number of bits in source number.
     * @param destNum number to set bits inside.
     * @param startPosition where to start setting a range of bits.
     * @return number with set bits.
     */
    public static int  setBits(int sourceNumber, int numberOfBits, int destNum, int startPosition) {
        for (int i = 0; i < numberOfBits; i++) {
            destNum = Helper.setBit(destNum,startPosition + i, Helper.getBit(sourceNumber,i));
        }
        return  destNum;
    }


    /**
     * Sets a subset of bits inside another set of bits.
     * @param sourceNumber set of bits to set.
     * @param numberOfBits number of bits in source number.
     * @param destNum number to set bits inside.
     * @param startPosition where to start setting a range of bits.
     * @return number with set bits.
     */
    public static long  setBits(int sourceNumber, int numberOfBits, long destNum, int startPosition) {
        for (int i = 0; i < numberOfBits; i++) {
            destNum = Helper.setBit(destNum,startPosition + i, Helper.getBit(sourceNumber,i));
        }
        return  destNum;
    }
}
