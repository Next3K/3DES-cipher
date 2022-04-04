package kryptografia.model.cipher;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DESTest {

    private final long mainKey64Bit = 0b00010011_00110100_01010111_01111001_10011011_10111100_11011111_11110001L;


    @Test
    public void testGenerateSubKeys() {
        DES des = new DES(0xAABB09182736CCDDL);
        des.encrypt(0x123456ABCD132536L);
        Assert.assertArrayEquals(new long[]{
                        0x194CD072DE8CL,0x4568581ABCCEL,
                        0x06EDA4ACF5B5L,0xDA2D032B6EE3L,
                        0x69A629FEC913L,0xC1948E87475EL,
                        0x708AD2DDB3C0L,0x34F822F0C66DL,
                        0x84BB4473DCCCL,0x02765708B5BFL,
                        0x6D5560AF7CA5L,0xC2C1E96A4BF3L,
                        0x99C31397C91FL,0x251B8BC717D0L,
                        0x3330C5D9A36DL,0x181C5D75C66DL},des.getSubKeys());
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
