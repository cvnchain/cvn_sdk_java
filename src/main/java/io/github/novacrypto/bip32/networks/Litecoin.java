package io.github.novacrypto.bip32.networks;

import io.github.novacrypto.bip32.Network;

public enum Litecoin implements Network {
  MAIN_NET {
    public int getPrivateVersion() {
      return 27106558;
    }
    
    public int getPublicVersion() {
      return 27108450;
    }
    
    public byte p2pkhVersion() {
      return 48;
    }
    
    public byte p2shVersion() {
      return 50;
    }
  };
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\networks\Litecoin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */