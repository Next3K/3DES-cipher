package kryptografia.model;

import kryptografia.model.cipher.DES;
import kryptografia.model.helper.Helper;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HelperTest {


    @Test
    public void testGetBit() {
        assertEquals(1,Helper.getBit(0b0000,0));
        assertEquals(0,Helper.getBit(0b0100,0));
        assertEquals(1,Helper.getBit(0b1000,3));
    }


    @Test
    public void testSetBit(){
        assertEquals(0b001,Helper.setBit(0,0,1));
        assertEquals(0b000,Helper.setBit(0b1000,3,0));
    }

    @Test
    public void generateSmallerMainKey() {
        long mainKey = 0b00010011_00110100_01010111_01111001_10011011_10111100_11011111_11110001L;
        DES des = new DES(mainKey, "test");
        des.generateSmallerMainKey();
        long expected = 0b00000000_1111000_0110011_0010101_0101111_0101010_1011001_1001111_0001111L;
        assertEquals(expected,des.getSmallerMainKey());
    }

    @Test
    public void testExtractBits() {
        long key56bit = 0b00000000_1111000_0110011_0010101_0101111_0101010_1011001_1001111_0001111L;
        int leftBits = 0b1111000_0110011_0010101_0101111;
        int rightBits = 0b0101010_1011001_1001111_0001111;
        assertEquals(rightBits,Helper.extractBits(key56bit,28,0));
        assertEquals(leftBits,Helper.extractBits(key56bit,28,28));
        assertEquals(0b1,Helper.extractBits(0b0000111,1,0));
        assertEquals(0b0110,Helper.extractBits(0b01100,4,1));
        assertEquals(0b0111,Helper.extractBits(0b00001110000,4,4));
        assertEquals(0b100011,Helper.extractBits(0b1000111,6,1));
    }


}