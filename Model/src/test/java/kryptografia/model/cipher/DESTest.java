package kryptografia.model.cipher;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DESTest {

    private final long mainKey64Bit = 0b00010011_00110100_01010111_01111001_10011011_10111100_11011111_11110001L;


    @Test
    public void testGenerateSubKeys() {

    }



    @Test
    public void testGenerateSmallerMainKey() {
        DES des = new DES(mainKey64Bit);
        des.generateSmallerMainKey();
        assertEquals(
                0b1111000_0110011_0010101_0101111_0101010_1011001_1001111_0001111L,des.getSmallerMainKey());
    }

    @Test
    public void testCreate28BitHalfKeys() {
        DES des = new DES(mainKey64Bit);
        des.generateSmallerMainKey();
        des.create28BitHalfKeys();
        assertEquals(
                0b1111000_0110011_0010101_0101111L,des.getC0());
        assertEquals(
                0b0101010_1011001_1001111_0001111L,des.getD0());
    }

    @Test
    public void testInitialPermutation() {
        long message64bit = 0b0000_0001_0010_0011_0100_0101_0110_0111_1000_1001_1010_1011_1100_1101_1110_1111L;
        DES des = new DES(0L);
        des.setData64bit(message64bit);
        des.initialDataBlockPermutation();
        long actual = des.getData64bit();
        assertEquals(0b1100_1100_0000_0000_1100_1100_1111_1111_1111_0000_1010_1010_1111_0000_1010_1010L,actual);

    }

    @Test
    public void testGetMessage() {
        long message64bit = 0b0000_0001_0010_0011_0100_0101_0110_0111_1000_1001_1010_1011_1100_1101_1110_1111L;
        DES des = new DES(0L);
        des.setData64bit(message64bit);
        assertEquals(message64bit, des.getData64bit());
    }

}
