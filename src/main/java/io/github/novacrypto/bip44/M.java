package io.github.novacrypto.bip44;

public final class M {
  private final Purpose PURPOSE_44 = new Purpose(this, 44);
  
  private final Purpose PURPOSE_49 = new Purpose(this, 49);
  
  public Purpose purpose(int purpose) {
    switch (purpose) {
      case 44:
        return this.PURPOSE_44;
      case 49:
        return this.PURPOSE_49;
    } 
    return new Purpose(this, purpose);
  }
  
  public Purpose purpose44() {
    return this.PURPOSE_44;
  }
  
  public Purpose purpose49() {
    return this.PURPOSE_49;
  }
  
  public String toString() {
    return "m";
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip44\M.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */