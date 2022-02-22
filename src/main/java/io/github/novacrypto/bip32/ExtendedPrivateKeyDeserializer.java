package io.github.novacrypto.bip32;

import io.github.novacrypto.base58.Base58;
import io.github.novacrypto.bip32.networks.DefaultNetworks;

import java.util.Arrays;

final class ExtendedPrivateKeyDeserializer implements Deserializer<ExtendedPrivateKey> {
  static final ExtendedPrivateKeyDeserializer DEFAULT = new ExtendedPrivateKeyDeserializer((Networks)DefaultNetworks.INSTANCE);
  
  private final Networks networks;
  
  ExtendedPrivateKeyDeserializer(Networks networks) {
    this.networks = networks;
  }
  
  public ExtendedPrivateKey deserialize(CharSequence extendedBase58Key) {
    byte[] extendedKeyData = Base58.base58Decode(extendedBase58Key);
    try {
      return deserialize(extendedKeyData);
    } finally {
      Arrays.fill(extendedKeyData, (byte)0);
    } 
  }
  
  public ExtendedPrivateKey deserialize(byte[] extendedKeyData) {
    Checksum.confirmExtendedKeyChecksum(extendedKeyData);
    ByteArrayReader reader = new ByteArrayReader(extendedKeyData);
    return new ExtendedPrivateKey((new HdKey.Builder())
        
        .network(this.networks.findByPrivateVersion(reader.readSer32()))
        .depth(reader.read())
        .parentFingerprint(reader.readSer32())
        .childNumber(reader.readSer32())
        .chainCode(reader.readRange(32))
        .key(getKey(reader))
        .neutered(false)
        .build());
  }
  
  private byte[] getKey(ByteArrayReader reader) {
    if (reader.read() != 0)
      throw new BadKeySerializationException("Expected 0 padding at position 45"); 
    return reader.readRange(32);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\ExtendedPrivateKeyDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */