package kryptografia.model.cipher;

public class ThreeDes {
    private final DES cipherOne;
    private final DES cipherTwo;
    private final DES cipherThree;

    public ThreeDes(long keyOne, long keyTwo, long keyThree) {
        this.cipherOne = new DES(keyOne);
        this.cipherTwo = new DES(keyTwo);
        this.cipherThree = new DES(keyThree);
    }

    public long encrypt(long data64bit) {
        return cipherThree.encrypt(cipherTwo.decrypt(cipherOne.encrypt(data64bit)));
    }

    public long decrypt(long data64bit) {
      return cipherOne.decrypt(cipherTwo.encrypt(cipherThree.decrypt(data64bit)));
    }

}
