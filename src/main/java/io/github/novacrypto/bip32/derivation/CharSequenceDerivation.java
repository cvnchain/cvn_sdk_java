package io.github.novacrypto.bip32.derivation;

import io.github.novacrypto.bip32.Index;

public enum CharSequenceDerivation implements Derivation<CharSequence> {
  INSTANCE;
  
  public <T> T derive(T rootKey, CharSequence derivationPath, CkdFunction<T> ckdFunction) {
    int length = derivationPath.length();
    if (length == 0)
      throw new IllegalArgumentException("Path cannot be empty"); 
    if (derivationPath.charAt(0) != 'm')
      throw new IllegalArgumentException("Path must start with m"); 
    if (length == 1)
      return rootKey; 
    if (derivationPath.charAt(1) != '/')
      throw new IllegalArgumentException("Path must start with m/"); 
    T currentKey = rootKey;
    int buffer = 0;
    for (int i = 2; i < length; i++) {
      char c = derivationPath.charAt(i);
      switch (c) {
        case '\'':
          buffer = Index.hard(buffer);
          break;
        case '/':
          currentKey = ckdFunction.deriveChildKey(currentKey, buffer);
          buffer = 0;
          break;
        default:
          buffer *= 10;
          if (c < '0' || c > '9')
            throw new IllegalArgumentException("Illegal character in path: " + c); 
          buffer += c - 48;
          if (Index.isHardened(buffer))
            throw new IllegalArgumentException("Index number too large"); 
          break;
      } 
    } 
    return ckdFunction.deriveChildKey(currentKey, buffer);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\derivation\CharSequenceDerivation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */