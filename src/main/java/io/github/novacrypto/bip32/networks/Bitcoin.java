package io.github.novacrypto.bip32.networks;

import io.github.novacrypto.bip32.Network;

public enum Bitcoin implements Network {
  MAIN_NET {
    public int getPrivateVersion() {
      return 76066276;
    }
    
    public int getPublicVersion() {
      return 76067358;
    }
    
    public byte p2pkhVersion() {
      return 0;
    }
    
    public byte p2shVersion() {
      return 5;
    }
  },
  TEST_NET {
    public int getPrivateVersion() {
      return 70615956;
    }
    
    public int getPublicVersion() {
      return 70617039;
    }
    
    public byte p2pkhVersion() {
      return 111;
    }
    
    public byte p2shVersion() {
      return -60;
    }
  };
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\networks\Bitcoin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */