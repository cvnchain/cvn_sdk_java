package io.github.novacrypto.hashing;

import io.github.novacrypto.toruntime.CheckedExceptionToRuntime;

import java.security.MessageDigest;

public final class Sha256 {
  public static byte[] sha256(byte[] bytes) {
    return sha256(bytes, 0, bytes.length);
  }
  
  public static byte[] sha256(byte[] bytes, int offset, int length) {
    MessageDigest digest = sha256();
    digest.update(bytes, offset, length);
    return digest.digest();
  }
  
  public static byte[] sha256Twice(byte[] bytes) {
    return sha256Twice(bytes, 0, bytes.length);
  }
  
  public static byte[] sha256Twice(byte[] bytes, int offset, int length) {
    MessageDigest digest = sha256();
    digest.update(bytes, offset, length);
    digest.update(digest.digest());
    return digest.digest();
  }
  
  private static MessageDigest sha256() {
    return (MessageDigest)CheckedExceptionToRuntime.toRuntime(new CheckedExceptionToRuntime.Func<MessageDigest>() {
          public MessageDigest run() throws Exception {
            return MessageDigest.getInstance("SHA-256");
          }
        });
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\hashing\Sha256.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */