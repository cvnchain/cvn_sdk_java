package io.github.novacrypto.bip32.networks;

import io.github.novacrypto.bip32.Network;
import io.github.novacrypto.bip32.Networks;

public enum DefaultNetworks implements Networks {
  INSTANCE(new NetworkCollection(new Network[] { Bitcoin.MAIN_NET, Litecoin.MAIN_NET, Bitcoin.TEST_NET }));
  
  private final Networks networks;
  
  DefaultNetworks(Networks networks) {
    this.networks = networks;
  }
  
  public Network findByPrivateVersion(int privateVersion) {
    return this.networks.findByPrivateVersion(privateVersion);
  }
  
  public Network findByPublicVersion(int publicVersion) {
    return this.networks.findByPublicVersion(publicVersion);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\networks\DefaultNetworks.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */