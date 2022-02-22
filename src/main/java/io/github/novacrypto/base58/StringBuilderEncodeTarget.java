package io.github.novacrypto.base58;

final class StringBuilderEncodeTarget implements EncodeTarget {
  final StringBuilder sb = new StringBuilder();
  
  public void append(char c) {
    this.sb.append(c);
  }
  
  public String toString() {
    return this.sb.toString();
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\base58\StringBuilderEncodeTarget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */