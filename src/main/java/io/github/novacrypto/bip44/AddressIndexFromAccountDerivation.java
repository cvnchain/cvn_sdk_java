package io.github.novacrypto.bip44;

import io.github.novacrypto.bip32.derivation.CkdFunction;
import io.github.novacrypto.bip32.derivation.Derivation;

final class AddressIndexFromAccountDerivation implements Derivation<AddressIndex> {
  public <Node> Node derive(Node accountNode, AddressIndex addressIndex, CkdFunction<Node> ckdFunction) {
    Change change = addressIndex.getParent();
    return (Node)ckdFunction.deriveChildKey(ckdFunction
        .deriveChildKey(accountNode, change
          .getValue()), addressIndex
        .getValue());
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip44\AddressIndexFromAccountDerivation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */