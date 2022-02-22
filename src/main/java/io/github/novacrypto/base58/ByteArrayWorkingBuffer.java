package io.github.novacrypto.base58;

import java.util.Arrays;

final class ByteArrayWorkingBuffer implements WorkingBuffer {
  private static byte[] EMPTY = new byte[0];
  
  private byte[] bytes = EMPTY;
  
  public void setCapacity(int atLeast) {
    this.bytes = ensureCapacity(this.bytes, atLeast);
    clear(this.bytes);
  }
  
  public byte get(int index) {
    return this.bytes[index];
  }
  
  public void put(int index, byte value) {
    this.bytes[index] = value;
  }
  
  public void clear() {
    clear(this.bytes);
  }
  
  private static byte[] ensureCapacity(byte[] bytes, int atLeast) {
    if (bytes.length >= atLeast)
      return bytes; 
    clear(bytes);
    return new byte[atLeast];
  }
  
  private static void clear(byte[] bytes) {
    Arrays.fill(bytes, (byte)-1);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\base58\ByteArrayWorkingBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */