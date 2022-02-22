package io.github.novacrypto.bip44;

import io.github.novacrypto.bip32.Index;

public final class Purpose {
  private final M m;
  
  private final int purpose;
  
  private final String toString;
  
  Purpose(M m, int purpose) {
    this.m = m;
    if (purpose == 0 || Index.isHardened(purpose))
      throw new IllegalArgumentException(); 
    this.purpose = purpose;
    this.toString = String.format("%s/%d'", new Object[] { m, Integer.valueOf(purpose) });
  }
  
  public int getValue() {
    return this.purpose;
  }
  
  public String toString() {
    return this.toString;
  }
  
  public CoinType coinType(int coinType) {
    return new CoinType(this, coinType);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip44\Purpose.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */