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


}