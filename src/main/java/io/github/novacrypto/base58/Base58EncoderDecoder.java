package io.github.novacrypto.base58;

import java.util.Arrays;

final class Base58EncoderDecoder implements GeneralEncoderDecoder {
  private static final char[] DIGITS = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
  
  private static final int[] VALUES = initValues(DIGITS);
  
  private final WorkingBuffer workingBuffer;
  
  Base58EncoderDecoder(WorkingBuffer workingBuffer) {
    this.workingBuffer = workingBuffer;
  }
  
  public String encode(byte[] bytes) {
    StringBuilderEncodeTarget target = new StringBuilderEncodeTarget();
    encode(bytes, target);
    return target.toString();
  }
  
  private WorkingBuffer getBufferOfAtLeastBytes(int atLeast) {
    this.workingBuffer.setCapacity(atLeast);
    return this.workingBuffer;
  }
  
  public void encode(byte[] bytes, EncodeTarget target) {
    char[] a = DIGITS;
    int bLen = bytes.length;
    WorkingBuffer d = getBufferOfAtLeastBytes(bLen << 1);
    try {
      int dlen = -1;
      int blanks = 0;
      int j = 0;
      for (int i = 0; i < bLen; ) {
        int c = bytes[i] & 0xFF;
        if (c == 0 && blanks == i) {
          target.append(a[0]);
          blanks++;
        } 
        j = 0;
        for (;; i++) {
          if (j <= dlen || c != 0) {
            int n;
            if (j > dlen) {
              dlen = j;
              n = c;
            } else {
              n = d.get(j);
              n = (n << 8) + c;
            } 
            d.put(j, (byte)(n % 58));
            c = n / 58;
            j++;
            continue;
          } 
        } 
      } 
      while (j-- > 0)
        target.append(a[d.get(j)]); 
    } finally {
      d.clear();
    } 
  }
  
  public byte[] decode(CharSequence base58) {
    ByteArrayTarget target = new ByteArrayTarget();
    decode(base58, target);
    return target.asByteArray();
  }
  
  public void decode(CharSequence base58, DecodeTarget target) {
    int strLen = base58.length();
    WorkingBuffer d = getBufferOfAtLeastBytes(strLen);
    try {
      int dlen = -1;
      int blanks = 0;
      int j = 0;
      for (int i = 0; i < strLen; ) {
        j = 0;
        char charAtI = base58.charAt(i);
        int c = valueOf(charAtI);
        if (c < 0)
          throw new BadCharacterException(charAtI); 
        if (c == 0 && blanks == i)
          blanks++; 
        for (;; i++) {
          if (j <= dlen || c != 0) {
            int n;
            if (j > dlen) {
              dlen = j;
              n = c;
            } else {
              n = d.get(j) & 0xFF;
              n = n * 58 + c;
            } 
            d.put(j, (byte)n);
            c = n >>> 8;
            j++;
            continue;
          } 
        } 
      } 
      int outputLength = j + blanks;
      DecodeWriter writer = target.getWriterForLength(outputLength);
      for (int k = 0; k < blanks; k++)
        writer.append((byte)0); 
      int end = outputLength - 1;
      for (int m = blanks; m < outputLength; m++)
        writer.append(d.get(end - m)); 
    } finally {
      d.clear();
    } 
  }
  
  private static int[] initValues(char[] alphabet) {
    int[] lookup = new int[123];
    Arrays.fill(lookup, -1);
    for (int i = 0; i < alphabet.length; i++)
      lookup[alphabet[i]] = i; 
    return lookup;
  }
  
  private static int valueOf(char base58Char) {
    if (base58Char >= VALUES.length)
      return -1; 
    return VALUES[base58Char];
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\base58\Base58EncoderDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */