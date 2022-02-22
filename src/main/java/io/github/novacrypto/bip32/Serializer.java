package io.github.novacrypto.bip32;

final class Serializer {
  private final Network network;
  
  private final boolean neutered;
  
  private final int depth;
  
  private final int childNumber;
  
  private final int fingerprint;
  
  private Serializer(Builder builder) {
    this.network = builder.network;
    this.neutered = builder.neutered;
    this.depth = builder.depth;
    this.childNumber = builder.childNumber;
    this.fingerprint = builder.fingerprint;
  }
  
  byte[] serialize(byte[] key, byte[] chainCode) {
    if (key == null)
      throw new IllegalArgumentException("Key is null"); 
    if (chainCode == null)
      throw new IllegalArgumentException("Chain code is null"); 
    if (chainCode.length != 32)
      throw new IllegalArgumentException("Chain code must be 32 bytes"); 
    if (this.neutered) {
      if (key.length != 33)
        throw new IllegalArgumentException("Key must be 33 bytes for neutered serialization"); 
    } else if (key.length != 32) {
      throw new IllegalArgumentException("Key must be 32 bytes for non neutered serialization");
    } 
    byte[] privateKey = new byte[82];
    ByteArrayWriter writer = new ByteArrayWriter(privateKey);
    writer.concatSer32(getVersion());
    writer.concat((byte)this.depth);
    writer.concatSer32(this.fingerprint);
    writer.concatSer32(this.childNumber);
    writer.concat(chainCode);
    if (!this.neutered) {
      writer.concat((byte)0);
      writer.concat(key);
    } else {
      writer.concat(key);
    } 
    writer.concat(Checksum.checksum(privateKey), 4);
    return privateKey;
  }
  
  private int getVersion() {
    return this.neutered ? this.network.getPublicVersion() : this.network.getPrivateVersion();
  }
  
  static class Builder {
    private Network network;
    
    private boolean neutered;
    
    private int depth;
    
    private int childNumber;
    
    private int fingerprint;
    
    Builder network(Network network) {
      this.network = network;
      return this;
    }
    
    Builder neutered(boolean neutered) {
      this.neutered = neutered;
      return this;
    }
    
    Builder depth(int depth) {
      if (depth < 0 || depth > 255)
        throw new IllegalArgumentException("Depth must be [0..255]"); 
      this.depth = depth;
      return this;
    }
    
    Builder childNumber(int childNumber) {
      this.childNumber = childNumber;
      return this;
    }
    
    Builder fingerprint(int fingerprint) {
      this.fingerprint = fingerprint;
      return this;
    }
    
    Serializer build() {
      return new Serializer(this);
    }
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\Serializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */