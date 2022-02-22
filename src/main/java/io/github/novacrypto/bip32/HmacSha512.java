package io.github.novacrypto.bip32;

import io.github.novacrypto.toruntime.CheckedExceptionToRuntime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

final class HmacSha512 {
  private static final String HMAC_SHA512 = "HmacSHA512";
  
  static byte[] hmacSha512(byte[] byteKey, byte[] seed) {
    return initialize(byteKey)
      .doFinal(seed);
  }
  
  private static Mac initialize(byte[] byteKey) {
    final Mac hmacSha512 = getInstance("HmacSHA512");
    final SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA512");
    CheckedExceptionToRuntime.toRuntime(new CheckedExceptionToRuntime.Action() {
          public void run() throws Exception {
            hmacSha512.init(keySpec);
          }
        });
    return hmacSha512;
  }
  
  private static Mac getInstance(final String HMAC_SHA256) {
    return (Mac)CheckedExceptionToRuntime.toRuntime(new CheckedExceptionToRuntime.Func<Mac>() {
          public Mac run() throws Exception {
            return Mac.getInstance(HMAC_SHA256);
          }
        });
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\HmacSha512.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */