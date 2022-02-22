package io.github.novacrypto.bip32.networks;

import io.github.novacrypto.bip32.Network;
import io.github.novacrypto.bip32.Networks;

import java.util.Arrays;
import java.util.List;

public final class NetworkCollection implements Networks {
  private final List<? extends Network> networks;
  
  public NetworkCollection(Network... networks) {
    this.networks = Arrays.asList(networks);
  }
  
  public Network findByPrivateVersion(int privateVersion) {
    for (Network network : this.networks) {
      if (network.getPrivateVersion() == privateVersion)
        return network; 
    } 
    throw new UnknownNetworkException(String.format("Can't find network that matches private version 0x%x", new Object[] { Integer.valueOf(privateVersion) }));
  }
  
  public Network findByPublicVersion(int publicVersion) {
    for (Network network : this.networks) {
      if (network.getPublicVersion() == publicVersion)
        return network; 
    } 
    throw new UnknownNetworkException(String.format("Can't find network that matches public version 0x%x", new Object[] { Integer.valueOf(publicVersion) }));
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\networks\NetworkCollection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */