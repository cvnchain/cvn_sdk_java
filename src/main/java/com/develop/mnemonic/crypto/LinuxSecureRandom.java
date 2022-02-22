package com.develop.mnemonic.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.Provider;
import java.security.SecureRandomSpi;
import java.security.Security;

public class LinuxSecureRandom extends SecureRandomSpi {
  private static final FileInputStream urandom;
  
  private static class LinuxSecureRandomProvider extends Provider {
    public LinuxSecureRandomProvider() {
      super("LinuxSecureRandom", 1.0D, "A Linux specific random number provider that uses /dev/urandom");
      put("SecureRandom.LinuxSecureRandom", LinuxSecureRandom.class.getName());
    }
  }
  
  private static final Logger log = LoggerFactory.getLogger(LinuxSecureRandom.class);
  
  static {
    try {
      File file = new File("/dev/urandom");
      urandom = new FileInputStream(file);
      if (urandom.read() == -1)
        throw new RuntimeException("/dev/urandom not readable?"); 
      int position = Security.insertProviderAt(new LinuxSecureRandomProvider(), 1);
      if (position != -1) {
        log.info("Secure randomness will be read from {} only.", file);
      } else {
        log.info("Randomness is already secure.");
      } 
    } catch (FileNotFoundException e) {
      log.error("/dev/urandom does not appear to exist or is not openable");
      throw new RuntimeException(e);
    } catch (IOException e) {
      log.error("/dev/urandom does not appear to be readable");
      throw new RuntimeException(e);
    } 
  }
  
  private final DataInputStream dis = new DataInputStream(urandom);
  
  protected void engineSetSeed(byte[] bytes) {}
  
  protected void engineNextBytes(byte[] bytes) {
    try {
      this.dis.readFully(bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
  }
  
  protected byte[] engineGenerateSeed(int i) {
    byte[] bits = new byte[i];
    engineNextBytes(bits);
    return bits;
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\com\develop\mnemonic\crypto\LinuxSecureRandom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */