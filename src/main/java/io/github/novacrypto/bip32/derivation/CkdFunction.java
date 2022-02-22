package io.github.novacrypto.bip32.derivation;

public interface CkdFunction<KeyNode> {
  KeyNode deriveChildKey(KeyNode paramKeyNode, int paramInt);
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\derivation\CkdFunction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */