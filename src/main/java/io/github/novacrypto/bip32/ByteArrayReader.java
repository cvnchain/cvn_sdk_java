package io.github.novacrypto.bip32;

import java.util.Arrays;

final class ByteArrayReader {
  private final byte[] bytes;
  
  private int idx = 0;
  
  ByteArrayReader(byte[] source) {
    this.bytes = source;
  }
  
  byte[] readRange(int length) {
    byte[] range = Arrays.copyOfRange(this.bytes, this.idx, this.idx + length);
    this.idx += length;
    return range;
  }
  
  int readSer32() {
    int result = read();
    result <<= 8;
    result |= read();
    result <<= 8;
    result |= read();
    result <<= 8;
    result |= read();
    return result;
  }
  
  int read() {
    return 0xFF & this.bytes[this.idx++];
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\ByteArrayReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */