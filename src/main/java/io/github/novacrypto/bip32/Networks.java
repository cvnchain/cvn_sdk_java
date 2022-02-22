package io.github.novacrypto.bip32;

public interface Networks {
  Network findByPrivateVersion(int paramInt);
  
  Network findByPublicVersion(int paramInt);
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\Networks.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */