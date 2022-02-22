package io.github.novacrypto.bip32;

public interface ExtendedKey {
  Network network();
  
  int depth();
  
  int childNumber();
  
  String extendedBase58();
  
  byte[] extendedKeyByteArray();
  
  ExtendedKey toNetwork(Network paramNetwork);
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\ExtendedKey.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */