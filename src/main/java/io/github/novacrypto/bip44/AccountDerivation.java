package io.github.novacrypto.bip44;

import io.github.novacrypto.bip32.Index;
import io.github.novacrypto.bip32.derivation.CkdFunction;
import io.github.novacrypto.bip32.derivation.Derivation;

final class AccountDerivation implements Derivation<Account> {
  public <Node> Node derive(Node root, Account account, CkdFunction<Node> ckdFunction) {
    CoinType coinType = account.getParent();
    Purpose purpose = coinType.getParent();
    return (Node)ckdFunction.deriveChildKey(ckdFunction
        .deriveChildKey(ckdFunction
          .deriveChildKey(root, 
            Index.hard(purpose.getValue())), 
          Index.hard(coinType.getValue())), 
        Index.hard(account.getValue()));
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip44\AccountDerivation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */