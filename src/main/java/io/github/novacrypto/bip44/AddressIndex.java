package io.github.novacrypto.bip44;

import io.github.novacrypto.bip32.Index;
import io.github.novacrypto.bip32.derivation.Derivation;

public final class AddressIndex {
  public static final Derivation<AddressIndex> DERIVATION = new AddressIndexDerivation();
  
  public static final Derivation<AddressIndex> DERIVATION_FROM_ACCOUNT = new AddressIndexFromAccountDerivation();
  
  private final Change change;
  
  private final int addressIndex;
  
  private final String string;
  
  AddressIndex(Change change, int addressIndex) {
    if (Index.isHardened(addressIndex))
      throw new IllegalArgumentException(); 
    this.change = change;
    this.addressIndex = addressIndex;
    this.string = String.format("%s/%d", new Object[] { change, Integer.valueOf(addressIndex) });
  }
  
  public int getValue() {
    return this.addressIndex;
  }
  
  public Change getParent() {
    return this.change;
  }
  
  public String toString() {
    return this.string;
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip44\AddressIndex.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */