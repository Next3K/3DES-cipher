package kryptografia.model;

import kryptografia.model.cipher.DES;
import kryptografia.model.helper.Helper;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HelperTest {

    // hello
    @Test
    public void testGetBit() {
        assertEquals(0,Helper.getBit(0b0000,0));
        assertEquals(1,Helper.getBit(0b0100,2));
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
        DES des = new DES(mainKey);
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

    @Test
    public void testSetBits() {
        assertEquals(0b0011, Helper.setBits(0b11,2,0b0000,0));
        assertEquals(0b0011100, Helper.setBits(0b1110,4,0,1));
    }

    @Test
    public void testLeftShift() {
        assertEquals(0b010, Helper.leftShift(0b001,1,3));
        assertEquals(0b001, Helper.leftShift(0b001,3,3));
        assertEquals(0b00100, Helper.leftShift(0b00100,5,5));
        assertEquals(0b011100, Helper.leftShift(0b100011,3,6));
        assertEquals(0b011000, Helper.leftShift(0b000110,2,6));
        assertEquals(0b011000, Helper.leftShift(0b011000,0,6));
        assertEquals(0b1001, Helper.leftShift(0b01001,4,4));
        assertEquals(0b00000000_00000000_00000000_00000001,
                Helper.leftShift(0b10000000_00000000_00000000_00000000,1,32));
        assertEquals(0b00000000_00000000_00000000_10000001,
                Helper.leftShift(0b10000001_00000000_00000000_00000000,8,32));
        assertEquals(0b00100000_00000000_00000000_00011111,
                Helper.leftShift(0b11111001_00000000_00000000_00000000,5,32));
        assertEquals(0b0000000_0000000_0000000_0001000,
                Helper.leftShift(0b1000000_0000000_0000000_0000000,4,28));
        assertEquals(0b1110000_0000000_0000000_0001111,
                Helper.leftShift(0b1111111_0000000_0000000_0000000,4,28));
        assertEquals(0b00111100_00000011_11111111,
                Helper.leftShift(0b11111111_00111100_00000011,8,24));
        assertEquals(0b10,
                Helper.leftShift(0b01,3,2));
    }

    @Test
    public void testRightShift() {
        assertEquals(0b0010, Helper.rightShift(0b0100,1,4));
        assertEquals(0b0100, Helper.rightShift(0b0001,2,4));
        assertEquals(0b1000, Helper.rightShift(0b1000,4,4));
        assertEquals(0b11001, Helper.rightShift(0b01110,3,5));
        assertEquals(0b011111, Helper.rightShift(0b111110,7,6));
        assertEquals(0b10000000_00000000_00000000_00000000,
                Helper.rightShift(1,1,32));
        assertEquals(0b00011100_00000000_00000000_00000000,
                Helper.rightShift(0b111,6,32));
        assertEquals(0b10000000_00000000_00000000_00000000, // big bits fail
                Helper.rightShift(0b00000000_00000000_00000000_00000001,1,32));
        assertEquals(0b10000001_00000000_00000000_00000000,
                Helper.rightShift(0b00000000_00000000_00000000_10000001,8,32));
        assertEquals(0b11111001_00000000_00000000_00000000,
                Helper.rightShift(0b00100000_00000000_00000000_00011111,5,32));
        assertEquals(0b00001000_00000000_00000000_00000000,
                Helper.rightShift(1,1,28));
        assertEquals(0b0001_11000000_00000000_00000000, // only 28 bits from 32 are important.
                Helper.rightShift(0b111,6,28));
        assertEquals(0b00_11111111_11111111_000000, // big bits fail
                Helper.rightShift(0b11111111_00000000_11111111,10,24));
        assertEquals(0b10,
                Helper.rightShift(0b01,3,2));

    }


}