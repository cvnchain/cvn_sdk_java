package io.github.novacrypto.base58;

final class ByteArrayTarget implements DecodeTarget {
  private int idx = 0;
  
  private byte[] bytes;
  
  public DecodeWriter getWriterForLength(int len) {
    this.bytes = new byte[len];
    return new DecodeWriter() {
        public void append(byte b) {
          ByteArrayTarget.this.bytes[ByteArrayTarget.this.idx++] = b;
        }
      };
  }
  
  byte[] asByteArray() {
    return this.bytes;
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\base58\ByteArrayTarget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */