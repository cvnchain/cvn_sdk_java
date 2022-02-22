package com.develop.mnemonic.crypto;

import java.security.SecureRandom;

public class SecureRandomUtils {
  static {
    if (isAndroidRuntime())
      new LinuxSecureRandom(); 
  }
  
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  
  public static SecureRandom secureRandom() {
    return SECURE_RANDOM;
  }
  
  private static int isAndroid = -1;
  
  static boolean isAndroidRuntime() {
    if (isAndroid == -1) {
      String runtime = System.getProperty("java.runtime.name");
      isAndroid = (runtime != null && runtime.equals("Android Runtime")) ? 1 : 0;
    } 
    return (isAndroid == 1);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\com\develop\mnemonic\crypto\SecureRandomUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */