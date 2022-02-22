package io.github.novacrypto.bip32;

import io.github.novacrypto.hashing.Hash160;

final class HdKey {
  private final boolean neutered;
  
  private final Network network;
  
  private final byte[] chainCode;
  
  private final byte[] key;
  
  private final Serializer serializer;
  
  private final int parentFingerprint;
  
  private final int childNumber;
  
  private final int depth;
  
  private HdKey(Builder builder) {
    this.neutered = builder.neutered;
    this.network = builder.network;
    this.key = builder.key;
    this.parentFingerprint = builder.parentFingerprint;
    this.childNumber = builder.childNumber;
    this.chainCode = builder.chainCode;
    this.depth = builder.depth;
    this
      
      .serializer = (new Serializer.Builder()).network(builder.network).neutered(builder.neutered).depth(builder.depth).childNumber(builder.childNumber).fingerprint(builder.parentFingerprint).build();
  }
  
  byte[] serialize() {
    return this.serializer.serialize(this.key, this.chainCode);
  }
  
  byte[] getPoint() {
    return Secp256k1SC.pointSerP_gMultiply(BigIntegerUtils.parse256(this.key));
  }
  
  byte[] getKey() {
    return this.key;
  }
  
  int getParentFingerprint() {
    return this.parentFingerprint;
  }
  
  int calculateFingerPrint() {
    byte[] point = getPublicBuffer();
    byte[] o = Hash160.hash160(point);
    return (o[0] & 0xFF) << 24 | (o[1] & 0xFF) << 16 | (o[2] & 0xFF) << 8 | o[3] & 0xFF;
  }
  
  private byte[] getPublicBuffer() {
    return this.neutered ? this.key : getPoint();
  }
  
  int depth() {
    return this.depth;
  }
  
  Network getNetwork() {
    return this.network;
  }
  
  byte[] getChainCode() {
    return this.chainCode;
  }
  
  int getChildNumber() {
    return this.childNumber;
  }
  
  Builder toBuilder() {
    return (new Builder())
      .neutered(this.neutered)
      .chainCode(this.chainCode)
      .key(this.key)
      .depth(this.depth)
      .childNumber(this.childNumber)
      .parentFingerprint(this.parentFingerprint);
  }
  
  static class Builder {
    private Network network;
    
    private boolean neutered;
    
    private byte[] chainCode;
    
    private byte[] key;
    
    private int depth;
    
    private int childNumber;
    
    private int parentFingerprint;
    
    Builder network(Network network) {
      this.network = network;
      return this;
    }
    
    Builder neutered(boolean neutered) {
      this.neutered = neutered;
      return this;
    }
    
    Builder key(byte[] key) {
      this.key = key;
      return this;
    }
    
    Builder chainCode(byte[] chainCode) {
      this.chainCode = chainCode;
      return this;
    }
    
    Builder depth(int depth) {
      this.depth = depth;
      return this;
    }
    
    Builder childNumber(int childNumber) {
      this.childNumber = childNumber;
      return this;
    }
    
    Builder parentFingerprint(int parentFingerprint) {
      this.parentFingerprint = parentFingerprint;
      return this;
    }
    
    HdKey build() {
      return new HdKey(this);
    }
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\HdKey.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */