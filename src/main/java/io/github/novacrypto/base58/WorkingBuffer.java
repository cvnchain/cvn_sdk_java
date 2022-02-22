package io.github.novacrypto.base58;

public interface WorkingBuffer {
  void setCapacity(int paramInt);
  
  byte get(int paramInt);
  
  void put(int paramInt, byte paramByte);
  
  void clear();
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\base58\WorkingBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */