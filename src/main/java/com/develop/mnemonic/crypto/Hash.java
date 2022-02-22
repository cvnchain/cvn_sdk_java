package com.develop.mnemonic.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
  public static byte[] sha256(byte[] input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return digest.digest(input);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Couldn't find a SHA-256 provider", e);
    } 
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\com\develop\mnemonic\crypto\Hash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */