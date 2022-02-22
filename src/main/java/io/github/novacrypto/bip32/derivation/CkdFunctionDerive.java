package io.github.novacrypto.bip32.derivation;

public final class CkdFunctionDerive<Node> implements Derive<Node> {
  private final CkdFunction<Node> standardCkdFunction;
  
  private final Node rootNode;
  
  public CkdFunctionDerive(CkdFunction<Node> standardCkdFunction, Node rootNode) {
    this.standardCkdFunction = standardCkdFunction;
    this.rootNode = rootNode;
  }
  
  public Node derive(CharSequence derivationPath) {
    return derive(derivationPath, CharSequenceDerivation.INSTANCE);
  }
  
  public <Path> Node derive(Path derivationPath, Derivation<Path> derivation) {
    return derivation.derive(this.rootNode, derivationPath, this.standardCkdFunction);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\derivation\CkdFunctionDerive.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */