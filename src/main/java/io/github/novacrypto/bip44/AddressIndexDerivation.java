package io.github.novacrypto.bip44;

import io.github.novacrypto.bip32.Index;
import io.github.novacrypto.bip32.derivation.CkdFunction;
import io.github.novacrypto.bip32.derivation.Derivation;

final class AddressIndexDerivation implements Derivation<AddressIndex> {
  public <Node> Node derive(Node root, AddressIndex addressIndex, CkdFunction<Node> ckdFunction) {
    Change change = addressIndex.getParent();
    Account account = change.getParent();
    CoinType coinType = account.getParent();
    Purpose purpose = coinType.getParent();
    return (Node)ckdFunction.deriveChildKey(ckdFunction
        .deriveChildKey(ckdFunction
          .deriveChildKey(ckdFunction
            .deriveChildKey(ckdFunction
              .deriveChildKey(root, 
                Index.hard(purpose.getValue())), 
              Index.hard(coinType.getValue())), 
            Index.hard(account.getValue())), change
          .getValue()), addressIndex
        .getValue());
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip44\AddressIndexDerivation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */