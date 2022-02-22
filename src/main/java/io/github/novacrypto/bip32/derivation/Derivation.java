package io.github.novacrypto.bip32.derivation;

public interface Derivation<Path> {
  <Key> Key derive(Key paramKey, Path paramPath, CkdFunction<Key> paramCkdFunction);
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\derivation\Derivation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */