package io.github.novacrypto.bip32.derivation;

public interface Derive<Key> {
  Key derive(CharSequence paramCharSequence);
  
  <Path> Key derive(Path paramPath, Derivation<Path> paramDerivation);
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\derivation\Derive.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */