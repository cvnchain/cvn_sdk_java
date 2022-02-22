package io.github.novacrypto.base58;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

final class SecureWorkingBuffer implements WorkingBuffer {
  private ByteBuffer bytes;
  
  private byte[] key = new byte[1021];
  
  SecureWorkingBuffer() {
    (new SecureRandom()).nextBytes(this.key);
  }
  
  public void setCapacity(int atLeast) {
    this.bytes = ensureCapacity(this.bytes, atLeast);
    clear(this.bytes);
  }
  
  public byte get(int index) {
    assertIndexValid(index);
    return encodeDecode(this.bytes.get(index), index);
  }
  
  public void put(int index, byte value) {
    assertIndexValid(index);
    this.bytes.put(index, encodeDecode(value, index));
  }
  
  public void clear() {
    clear(this.bytes);
  }
  
  private void assertIndexValid(int index) {
    if (index < 0 || index >= capacity())
      throw new IndexOutOfBoundsException(); 
  }
  
  private int capacity() {
    return (this.bytes == null) ? 0 : this.bytes.capacity();
  }
  
  private ByteBuffer ensureCapacity(ByteBuffer bytes, int atLeast) {
    if (bytes != null && bytes.capacity() >= atLeast)
      return bytes; 
    if (bytes != null)
      clear(bytes); 
    return ByteBuffer.allocateDirect(atLeast);
  }
  
  private void clear(ByteBuffer bytes) {
    bytes.position(0);
    int capacity = bytes.capacity();
    for (int i = 0; i < capacity; i++)
      bytes.put(i, encodeDecode((byte)-1, i)); 
  }
  
  private byte encodeDecode(byte b, int index) {
    return (byte)(b ^ this.key[index % this.key.length]);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\base58\SecureWorkingBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */