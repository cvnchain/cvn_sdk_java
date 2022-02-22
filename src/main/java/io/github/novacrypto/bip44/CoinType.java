package io.github.novacrypto.bip44;

import io.github.novacrypto.bip32.Index;

public final class CoinType {
  private final Purpose purpose;
  
  private final int coinType;
  
  private final String string;
  
  CoinType(Purpose purpose, int coinType) {
    if (Index.isHardened(coinType))
      throw new IllegalArgumentException(); 
    this.purpose = purpose;
    this.coinType = coinType;
    this.string = String.format("%s/%d'", new Object[] { purpose, Integer.valueOf(coinType) });
  }
  
  public int getValue() {
    return this.coinType;
  }
  
  public Purpose getParent() {
    return this.purpose;
  }
  
  public String toString() {
    return this.string;
  }
  
  public Account account(int account) {
    return new Account(this, account);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip44\CoinType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */