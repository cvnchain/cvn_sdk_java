package io.github.novacrypto.bip32;

public interface Network {
  int getPrivateVersion();
  
  int getPublicVersion();
  
  byte p2pkhVersion();
  
  byte p2shVersion();
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\Network.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */