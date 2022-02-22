package com.develop.mnemonic;

import io.github.novacrypto.bip32.ExtendedPrivateKey;
import io.github.novacrypto.bip32.Network;
import io.github.novacrypto.bip32.networks.Bitcoin;
import io.github.novacrypto.bip44.AddressIndex;
import io.github.novacrypto.bip44.BIP44;

public class KeyPairUtils {
  public static class CoinTypes {
    public static final int BTC = 0;
    
    public static final int BTCTEST = 1;
    
    public static final int LTC = 2;
    
    public static final int ETH = 60;
    
    public static final int EOS = 194;
    
    public static final int CWV = 386;
  }
  
  public static byte[] generatePrivateKey(String mnemonic, int coinType) {
    byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
    return generatePrivateKey(seed, coinType, (Network)Bitcoin.MAIN_NET);
  }
  
  public static byte[] generatePrivateKey(byte[] seed, int coinType) {
    return generatePrivateKey(seed, coinType, (Network)Bitcoin.MAIN_NET);
  }
  
  public static byte[] generatePrivateKey(byte[] seed, int coinType, Network network) {
    AddressIndex addressIndex = BIP44.m().purpose44().coinType(coinType).account(0).external().address(0);
    ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(seed, network);
    ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
    byte[] privateKeyBytes = childPrivateKey.getKey();
    return privateKeyBytes;
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\com\develop\mnemonic\KeyPairUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */