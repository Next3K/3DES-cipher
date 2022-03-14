package kryptografia.model.helper;

/**
 *  Class with helpful functions.
 */
public class Helper {

    /**
     * Get bit at position.
     * @param src source number
     * @param position position of bit.
     * @return 0 or 1.
     */
    public static int getBit(int src, int position){
        return (src >> position) & 1;
    }

    /**
     * Get bit at position.
     * @param src source number
     * @param position position of bit.
     * @return 0 or 1.
     */
    public static int getBit(long src, int position){
        return (int) ((src >> position) & 1);
    }


    /**
     * Set a bit of a given int number.
     * @param src int number.
     * @param position position of bit.
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
     * @param position position of bit.
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
}
