package io.github.novacrypto.bip32;

import io.github.novacrypto.base58.Base58;
import io.github.novacrypto.bip32.derivation.*;
import io.github.novacrypto.toruntime.CheckedExceptionToRuntime;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.Arrays;

public final class ExtendedPrivateKey implements Derive<ExtendedPrivateKey>, CKDpriv, CKDpub, ExtendedKey {
  public static Deserializer<ExtendedPrivateKey> deserializer() {
    return ExtendedPrivateKeyDeserializer.DEFAULT;
  }
  
  public static Deserializer<ExtendedPrivateKey> deserializer(Networks networks) {
    return new ExtendedPrivateKeyDeserializer(networks);
  }
  
  private static final CkdFunction<ExtendedPrivateKey> CKD_FUNCTION = new CkdFunction<ExtendedPrivateKey>() {
      public ExtendedPrivateKey deriveChildKey(ExtendedPrivateKey parent, int childIndex) {
        return parent.cKDpriv(childIndex);
      }
    };
  
  private static final byte[] BITCOIN_SEED = getBytes("Bitcoin seed");
  
  private final HdKey hdKey;
  
  private ExtendedPrivateKey(Network network, byte[] key, byte[] chainCode) {
    this((new HdKey.Builder())
        .network(network)
        .neutered(false)
        .key(key)
        .chainCode(chainCode)
        .depth(0)
        .childNumber(0)
        .parentFingerprint(0)
        .build());
  }
  
  ExtendedPrivateKey(HdKey hdKey) {
    this.hdKey = hdKey;
  }
  
  public static ExtendedPrivateKey fromSeed(byte[] seed, Network network) {
    byte[] I = HmacSha512.hmacSha512(BITCOIN_SEED, seed);
    byte[] Il = ByteArrayWriter.head32(I);
    byte[] Ir = ByteArrayWriter.tail32(I);
    return new ExtendedPrivateKey(network, Il, Ir);
  }
  
  private static byte[] getBytes(final String seed) {
    return (byte[])CheckedExceptionToRuntime.toRuntime(new CheckedExceptionToRuntime.Func<byte[]>() {
          public byte[] run() throws Exception {
            return seed.getBytes("UTF-8");
          }
        });
  }
  
  public byte[] extendedKeyByteArray() {
    return this.hdKey.serialize();
  }
  
  public ExtendedPrivateKey toNetwork(Network otherNetwork) {
    if (otherNetwork == network())
      return this; 
    return new ExtendedPrivateKey(this.hdKey
        .toBuilder()
        .network(otherNetwork)
        .build());
  }
  
  public byte[] getKey() {
    return this.hdKey.getKey();
  }
  
  public String getPrivateKey() {
    return new String(Hex.encode(getKey()));
  }
  
  public String extendedBase58() {
    return Base58.base58Encode(extendedKeyByteArray());
  }
  
  public ExtendedPrivateKey cKDpriv(int index) {
    byte[] data = new byte[37];
    ByteArrayWriter writer = new ByteArrayWriter(data);
    if (Index.isHardened(index)) {
      writer.concat((byte)0);
      writer.concat(this.hdKey.getKey(), 32);
    } else {
      writer.concat(this.hdKey.getPoint());
    } 
    writer.concatSer32(index);
    byte[] I = HmacSha512.hmacSha512(this.hdKey.getChainCode(), data);
    Arrays.fill(data, (byte)0);
    byte[] Il = ByteArrayWriter.head32(I);
    byte[] Ir = ByteArrayWriter.tail32(I);
    byte[] key = this.hdKey.getKey();
    BigInteger parse256_Il = BigIntegerUtils.parse256(Il);
    BigInteger ki = parse256_Il.add(BigIntegerUtils.parse256(key)).mod(Secp256k1SC.n());
    if (parse256_Il.compareTo(Secp256k1SC.n()) >= 0 || ki.equals(BigInteger.ZERO))
      return cKDpriv(index + 1); 
    BigIntegerUtils.ser256(Il, ki);
    return new ExtendedPrivateKey((new HdKey.Builder())
        .network(this.hdKey.getNetwork())
        .neutered(false)
        .key(Il)
        .chainCode(Ir)
        .depth(this.hdKey.depth() + 1)
        .childNumber(index)
        .parentFingerprint(this.hdKey.calculateFingerPrint())
        .build());
  }
  
  public ExtendedPublicKey cKDpub(int index) {
    return cKDpriv(index).neuter();
  }
  
  public ExtendedPublicKey neuter() {
    return ExtendedPublicKey.from(this.hdKey);
  }
  
  public Derive<ExtendedPrivateKey> derive() {
    return derive(CKD_FUNCTION);
  }
  
  public Derive<ExtendedPrivateKey> deriveWithCache() {
    return derive(CkdFunctionResultCacheDecorator.newCacheOf(CKD_FUNCTION));
  }
  
  public ExtendedPrivateKey derive(CharSequence derivationPath) {
    return (ExtendedPrivateKey)derive().derive(derivationPath);
  }
  
  public <Path> ExtendedPrivateKey derive(Path derivationPath, Derivation<Path> derivation) {
    return (ExtendedPrivateKey)derive().derive(derivationPath, derivation);
  }
  
  private Derive<ExtendedPrivateKey> derive(CkdFunction<ExtendedPrivateKey> ckdFunction) {
    return (Derive<ExtendedPrivateKey>)new CkdFunctionDerive(ckdFunction, this);
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
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\ExtendedPrivateKey.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */