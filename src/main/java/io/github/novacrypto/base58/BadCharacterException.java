package io.github.novacrypto.base58;

public final class BadCharacterException extends RuntimeException {
  public BadCharacterException(char charAtI) {
    super("Bad character in base58 string, '" + charAtI + "'");
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\base58\BadCharacterException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */