package io.github.novacrypto.bip44;

import io.github.novacrypto.bip32.Index;
import io.github.novacrypto.bip32.derivation.Derivation;

public final class Account {
  public static final Derivation<Account> DERIVATION = new AccountDerivation();
  
  private final CoinType coinType;
  
  private final int account;
  
  private final String string;
  
  Account(CoinType coinType, int account) {
    if (Index.isHardened(account))
      throw new IllegalArgumentException(); 
    this.coinType = coinType;
    this.account = account;
    this.string = String.format("%s/%d'", new Object[] { coinType, Integer.valueOf(account) });
  }
  
  public int getValue() {
    return this.account;
  }
  
  public CoinType getParent() {
    return this.coinType;
  }
  
  public String toString() {
    return this.string;
  }
  
  public Change external() {
    return new Change(this, 0);
  }
  
  public Change internal() {
    return new Change(this, 1);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip44\Account.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */