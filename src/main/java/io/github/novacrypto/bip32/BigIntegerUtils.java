package io.github.novacrypto.bip32;

import java.math.BigInteger;
import java.util.Arrays;

final class BigIntegerUtils {
  static BigInteger parse256(byte[] bytes) {
    return new BigInteger(1, bytes);
  }
  
  static void ser256(byte[] target, BigInteger integer) {
    if (integer.bitLength() > target.length * 8)
      throw new RuntimeException("ser256 failed, cannot fit integer in buffer"); 
    byte[] modArr = integer.toByteArray();
    Arrays.fill(target, (byte)0);
    copyTail(modArr, target);
    Arrays.fill(modArr, (byte)0);
  }
  
  private static void copyTail(byte[] src, byte[] dest) {
    if (src.length < dest.length) {
      System.arraycopy(src, 0, dest, dest.length - src.length, src.length);
    } else {
      System.arraycopy(src, src.length - dest.length, dest, 0, dest.length);
    } 
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\BigIntegerUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */