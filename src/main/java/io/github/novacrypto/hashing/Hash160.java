package io.github.novacrypto.hashing;

import org.spongycastle.crypto.digests.RIPEMD160Digest;

public final class Hash160 {
  private static final int RIPEMD160_DIGEST_SIZE = 20;
  
  public static byte[] hash160(byte[] bytes) {
    return ripemd160(Sha256.sha256(bytes));
  }
  
  public static void hash160into(byte[] target, int offset, byte[] bytes) {
    ripemd160into(Sha256.sha256(bytes), target, offset);
  }
  
  private static byte[] ripemd160(byte[] bytes) {
    byte[] output = new byte[20];
    ripemd160into(bytes, output, 0);
    return output;
  }
  
  private static void ripemd160into(byte[] bytes, byte[] target, int offset) {
    RIPEMD160Digest digest = new RIPEMD160Digest();
    digest.update(bytes, 0, bytes.length);
    digest.doFinal(target, offset);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\hashing\Hash160.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */