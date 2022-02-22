package io.github.novacrypto.bip32;

import io.github.novacrypto.base58.Base58;
import io.github.novacrypto.bip32.networks.DefaultNetworks;

import java.util.Arrays;

final class ExtendedPublicKeyDeserializer implements Deserializer<ExtendedPublicKey> {
  static final ExtendedPublicKeyDeserializer DEFAULT = new ExtendedPublicKeyDeserializer((Networks)DefaultNetworks.INSTANCE);
  
  private final Networks networks;
  
  ExtendedPublicKeyDeserializer(Networks networks) {
    this.networks = networks;
  }
  
  public ExtendedPublicKey deserialize(CharSequence extendedBase58Key) {
    byte[] extendedKeyData = Base58.base58Decode(extendedBase58Key);
    try {
      return deserialize(extendedKeyData);
    } finally {
      Arrays.fill(extendedKeyData, (byte)0);
    } 
  }
  
  public ExtendedPublicKey deserialize(byte[] extendedKeyData) {
    Checksum.confirmExtendedKeyChecksum(extendedKeyData);
    ByteArrayReader reader = new ByteArrayReader(extendedKeyData);
    return new ExtendedPublicKey((new HdKey.Builder())
        
        .network(this.networks.findByPublicVersion(reader.readSer32()))
        .depth(reader.read())
        .parentFingerprint(reader.readSer32())
        .childNumber(reader.readSer32())
        .chainCode(reader.readRange(32))
        .key(reader.readRange(33))
        .neutered(true)
        .build());
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\ExtendedPublicKeyDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */