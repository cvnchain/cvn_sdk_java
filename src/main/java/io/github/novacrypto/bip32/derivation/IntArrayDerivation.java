package io.github.novacrypto.bip32.derivation;

public enum IntArrayDerivation implements Derivation<int[]> {
  INSTANCE;
  
  public <Node> Node derive(Node rootKey, int[] path, CkdFunction<Node> ckdFunction) {
    Node currentKey = rootKey;
    for (int childIndex : path)
      currentKey = ckdFunction.deriveChildKey(currentKey, childIndex); 
    return currentKey;
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\derivation\IntArrayDerivation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */