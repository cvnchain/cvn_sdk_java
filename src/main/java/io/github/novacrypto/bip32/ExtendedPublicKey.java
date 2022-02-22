package io.github.novacrypto.bip32;

import io.github.novacrypto.base58.Base58;
import io.github.novacrypto.bip32.derivation.*;
import io.github.novacrypto.hashing.Hash160;
import io.github.novacrypto.hashing.Sha256;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;

public final class ExtendedPublicKey implements Derive<ExtendedPublicKey>, CKDpub, ExtendedKey {
  public static Deserializer<ExtendedPublicKey> deserializer() {
    return ExtendedPublicKeyDeserializer.DEFAULT;
  }
  
  public static Deserializer<ExtendedPublicKey> deserializer(Networks networks) {
    return new ExtendedPublicKeyDeserializer(networks);
  }
  
  private static final CkdFunction<ExtendedPublicKey> CKD_FUNCTION = new CkdFunction<ExtendedPublicKey>() {
      public ExtendedPublicKey deriveChildKey(ExtendedPublicKey parent, int childIndex) {
        return parent.cKDpub(childIndex);
      }
    };
  
  private final HdKey hdKey;
  
  static ExtendedPublicKey from(HdKey hdKey) {
    return new ExtendedPublicKey((new HdKey.Builder())
        .network(hdKey.getNetwork())
        .neutered(true)
        .key(hdKey.getPoint())
        .parentFingerprint(hdKey.getParentFingerprint())
        .depth(hdKey.depth())
        .childNumber(hdKey.getChildNumber())
        .chainCode(hdKey.getChainCode())
        .build());
  }
  
  ExtendedPublicKey(HdKey hdKey) {
    this.hdKey = hdKey;
  }
  
  public ExtendedPublicKey cKDpub(int index) {
    if (Index.isHardened(index))
      throw new IllegalCKDCall("Cannot derive a hardened key from a public key"); 
    HdKey parent = this.hdKey;
    byte[] kPar = parent.getKey();
    byte[] data = new byte[37];
    ByteArrayWriter writer = new ByteArrayWriter(data);
    writer.concat(kPar, 33);
    writer.concatSer32(index);
    byte[] I = HmacSha512.hmacSha512(parent.getChainCode(), data);
    byte[] Il = ByteArrayWriter.head32(I);
    byte[] Ir = ByteArrayWriter.tail32(I);
    BigInteger parse256_Il = BigIntegerUtils.parse256(Il);
    ECPoint ki = Secp256k1SC.gMultiplyAndAddPoint(parse256_Il, kPar);
    if (parse256_Il.compareTo(Secp256k1SC.n()) >= 0 || ki.isInfinity())
      return cKDpub(index + 1); 
    byte[] key = Secp256k1SC.pointSerP(ki);
    return new ExtendedPublicKey((new HdKey.Builder())
        .network(parent.getNetwork())
        .neutered(true)
        .depth(parent.depth() + 1)
        .parentFingerprint(parent.calculateFingerPrint())
        .key(key)
        .chainCode(Ir)
        .childNumber(index)
        .build());
  }
  
  public byte[] extendedKeyByteArray() {
    return this.hdKey.serialize();
  }
  
  public ExtendedPublicKey toNetwork(Network otherNetwork) {
    if (otherNetwork == network())
      return this; 
    return new ExtendedPublicKey(this.hdKey
        .toBuilder()
        .network(otherNetwork)
        .build());
  }
  
  public String extendedBase58() {
    return Base58.base58Encode(extendedKeyByteArray());
  }
  
  public String p2pkhAddress() {
    return encodeAddress(this.hdKey.getNetwork().p2pkhVersion(), this.hdKey.getKey());
  }
  
  public String p2shAddress() {
    byte[] script = new byte[22];
    script[1] = 20;
    Hash160.hash160into(script, 2, this.hdKey.getKey());
    return encodeAddress(this.hdKey.getNetwork().p2shVersion(), script);
  }
  
  private static String encodeAddress(byte version, byte[] data) {
    byte[] address = new byte[25];
    address[0] = version;
    Hash160.hash160into(address, 1, data);
    System.arraycopy(Sha256.sha256Twice(address, 0, 21), 0, address, 21, 4);
    return Base58.base58Encode(address);
  }
  
  public Derive<ExtendedPublicKey> derive() {
    return derive(CKD_FUNCTION);
  }
  
  public Derive<ExtendedPublicKey> deriveWithCache() {
    return derive(CkdFunctionResultCacheDecorator.newCacheOf(CKD_FUNCTION));
  }
  
  public ExtendedPublicKey derive(CharSequence derivationPath) {
    return (ExtendedPublicKey)derive().derive(derivationPath);
  }
  
  public <Path> ExtendedPublicKey derive(Path derivationPath, Derivation<Path> derivation) {
    return (ExtendedPublicKey)derive().derive(derivationPath, derivation);
  }
  
  private Derive<ExtendedPublicKey> derive(CkdFunction<ExtendedPublicKey> ckdFunction) {
    return (Derive<ExtendedPublicKey>)new CkdFunctionDerive(ckdFunction, this);
  }
  
  public Network network() {
    return this.hdKey.getNetwork();
  }
  
  public int depth() {
    return this.hdKey.depth();
  }
  
  public int childNumber() {
    return this.hdKey.getChildNumber();
  }
  
  public byte[] getKey() {
    return this.hdKey.getKey();
  }
  
  public String getPublicKey() {
    return new String(Hex.encode(getKey()));
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\ExtendedPublicKey.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */