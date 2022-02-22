package io.github.novacrypto.base58;

public final class Base58 {
  public static GeneralEncoderDecoder newInstanceWithBuffer(WorkingBuffer workingBuffer) {
    return new Base58EncoderDecoder(workingBuffer);
  }
  
  public static EncoderDecoder newInstance() {
    return newInstanceWithBuffer(new ByteArrayWorkingBuffer());
  }
  
  public static SecureEncoderDecoder newSecureInstance() {
    return newInstanceWithBuffer(new SecureWorkingBuffer());
  }
  
  private static final ThreadLocal<EncoderDecoder> working = new ThreadLocal<>();
  
  public static String base58Encode(byte[] bytes) {
    return getThreadSharedBase58().encode(bytes);
  }
  
  public static byte[] base58Decode(CharSequence base58) {
    return getThreadSharedBase58().decode(base58);
  }
  
  private static EncoderDecoder getThreadSharedBase58() {
    EncoderDecoder base58 = working.get();
    if (base58 == null) {
      base58 = newInstance();
      working.set(base58);
    } 
    return base58;
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\base58\Base58.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */