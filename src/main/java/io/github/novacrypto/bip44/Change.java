package io.github.novacrypto.bip44;

public final class Change {
  private final Account account;
  
  private final int change;
  
  private final String string;
  
  Change(Account account, int change) {
    this.account = account;
    this.change = change;
    this.string = String.format("%s/%d", new Object[] { account, Integer.valueOf(change) });
  }
  
  public int getValue() {
    return this.change;
  }
  
  public Account getParent() {
    return this.account;
  }
  
  public String toString() {
    return this.string;
  }
  
  public AddressIndex address(int addressIndex) {
    return new AddressIndex(this, addressIndex);
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip44\Change.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */