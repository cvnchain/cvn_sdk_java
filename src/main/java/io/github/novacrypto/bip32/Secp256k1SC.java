package io.github.novacrypto.bip32;

import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

final class Secp256k1SC {
  static final X9ECParameters CURVE = CustomNamedCurves.getByName("secp256k1");
  
  static BigInteger n() {
    return CURVE.getN();
  }
  
  static byte[] pointSerP(ECPoint point) {
    return point.getEncoded(true);
  }
  
  static byte[] pointSerP_gMultiply(BigInteger p) {
    return pointSerP(gMultiply(p));
  }
  
  static ECPoint gMultiplyAndAddPoint(BigInteger p, byte[] toAdd) {
    return gMultiply(p).add(decode(toAdd));
  }
  
  private static ECPoint decode(byte[] toAdd) {
    return CURVE.getCurve().decodePoint(toAdd);
  }
  
  private static ECPoint gMultiply(BigInteger p) {
    return CURVE.getG()
      .multiply(p);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\Secp256k1SC.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */