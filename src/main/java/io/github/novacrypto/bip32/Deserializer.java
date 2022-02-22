package io.github.novacrypto.bip32;

public interface Deserializer<T> {
  T deserialize(CharSequence paramCharSequence);
  
  T deserialize(byte[] paramArrayOfbyte);
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\Deserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */