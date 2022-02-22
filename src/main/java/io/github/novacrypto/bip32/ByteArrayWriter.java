package io.github.novacrypto.bip32;

import java.util.Arrays;

final class ByteArrayWriter {
  private final byte[] bytes;
  
  private int idx = 0;
  
  ByteArrayWriter(byte[] target) {
    this.bytes = target;
  }
  
  void concat(byte[] bytesSource, int length) {
    System.arraycopy(bytesSource, 0, this.bytes, this.idx, length);
    this.idx += length;
  }
  
  void concat(byte[] bytesSource) {
    concat(bytesSource, bytesSource.length);
  }
  
  void concatSer32(int i) {
    concat((byte)(i >> 24));
    concat((byte)(i >> 16));
    concat((byte)(i >> 8));
    concat((byte)i);
  }
  
  void concat(byte b) {
    this.bytes[this.idx++] = b;
  }
  
  static byte[] tail32(byte[] bytes64) {
    byte[] ir = new byte[bytes64.length - 32];
    System.arraycopy(bytes64, 32, ir, 0, ir.length);
    return ir;
  }
  
  static byte[] head32(byte[] bytes64) {
    return Arrays.copyOf(bytes64, 32);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\ByteArrayWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */