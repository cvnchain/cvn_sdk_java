package io.github.novacrypto.bip32;

import io.github.novacrypto.hashing.Sha256;

final class Checksum {
  static void confirmExtendedKeyChecksum(byte[] extendedKeyData) {
    byte[] expected = checksum(extendedKeyData);
    for (int i = 0; i < 4; i++) {
      if (extendedKeyData[78 + i] != expected[i])
        throw new BadKeySerializationException("Checksum error"); 
    } 
  }
  
  static byte[] checksum(byte[] privateKey) {
    return Sha256.sha256Twice(privateKey, 0, 78);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\Checksum.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */