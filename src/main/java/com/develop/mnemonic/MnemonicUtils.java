package com.develop.mnemonic;

import com.develop.mnemonic.crypto.Hash;
import com.develop.mnemonic.crypto.SecureRandomUtils;
import com.develop.mnemonic.utils.Numeric;
import com.develop.mnemonic.wordlists.English;
import com.develop.mnemonic.wordlists.WordList;
import org.spongycastle.crypto.Digest;
import org.spongycastle.crypto.digests.SHA512Digest;
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;

public class MnemonicUtils {
  private static final int SEED_ITERATIONS = 2048;
  
  private static final int SEED_KEY_SIZE = 512;
  
  private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();
  
  public static String generateMnemonic() {
    return generateMnemonic((WordList)English.INSTANCE);
  }
  
  public static String generateMnemonic(WordList type) {
    byte[] initialEntropy = new byte[16];
    secureRandom.nextBytes(initialEntropy);
    String mnemonic = generateMnemonic(initialEntropy, type);
    return mnemonic;
  }
  
  public static String generateMnemonic(byte[] initialEntropy, WordList wordList) {
    validateInitialEntropy(initialEntropy);
    int ent = initialEntropy.length * 8;
    int checksumLength = ent / 32;
    byte checksum = calculateChecksum(initialEntropy);
    boolean[] bits = convertToBits(initialEntropy, checksum);
    int iterations = (ent + checksumLength) / 11;
    StringBuilder mnemonicBuilder = new StringBuilder();
    for (int i = 0; i < iterations; i++) {
      int index = toInt(nextElevenBits(bits, i));
      mnemonicBuilder.append(wordList.getWord(index));
      boolean notLastIteration = (i < iterations - 1);
      if (notLastIteration)
        mnemonicBuilder.append(" "); 
    } 
    return mnemonicBuilder.toString();
  }
  
  public static byte[] generateSeed(String mnemonic, String passphrase) {
    validateMnemonic(mnemonic);
    passphrase = (passphrase == null) ? "" : passphrase;
    String salt = String.format("mnemonic%s", new Object[] { passphrase });
    PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator((Digest)new SHA512Digest());
    gen.init(mnemonic.getBytes(Charset.forName("UTF-8")), salt.getBytes(Charset.forName("UTF-8")), 2048);
    return ((KeyParameter)gen.generateDerivedParameters(512)).getKey();
  }
  
  private static void validateMnemonic(String mnemonic) {
    if (mnemonic == null || mnemonic.trim().isEmpty())
      throw new IllegalArgumentException("Mnemonic is required to generate a seed");
    if(mnemonic.split(" ").length>12) {
      throw new IllegalArgumentException("The number of spaces must be 11");
    }
  }
  
  private static boolean[] nextElevenBits(boolean[] bits, int i) {
    int from = i * 11;
    int to = from + 11;
    return Arrays.copyOfRange(bits, from, to);
  }
  
  private static void validateInitialEntropy(byte[] initialEntropy) {
    if (initialEntropy == null)
      throw new IllegalArgumentException("Initial entropy is required"); 
    int ent = initialEntropy.length * 8;
    if (ent < 128 || ent > 256 || ent % 32 != 0)
      throw new IllegalArgumentException("The allowed size of ENT is 128-256 bits of multiples of 32"); 
  }
  
  private static boolean[] convertToBits(byte[] initialEntropy, byte checksum) {
    int ent = initialEntropy.length * 8;
    int checksumLength = ent / 32;
    int totalLength = ent + checksumLength;
    boolean[] bits = new boolean[totalLength];
    int i;
    for (i = 0; i < initialEntropy.length; i++) {
      for (int j = 0; j < 8; j++) {
        byte b = initialEntropy[i];
        bits[8 * i + j] = toBit(b, j);
      } 
    } 
    for (i = 0; i < checksumLength; i++)
      bits[ent + i] = toBit(checksum, i); 
    return bits;
  }
  
  private static boolean toBit(byte value, int index) {
    return ((value >>> 7 - index & 0x1) > 0);
  }
  
  private static int toInt(boolean[] bits) {
    int value = 0;
    for (int i = 0; i < bits.length; i++) {
      boolean isSet = bits[i];
      if (isSet)
        value += 1 << bits.length - i - 1; 
    } 
    return value;
  }
  
  private static byte calculateChecksum(byte[] initialEntropy) {
    int ent = initialEntropy.length * 8;
    byte mask = (byte)(255 << 8 - ent / 32);
    byte[] bytes = Hash.sha256(initialEntropy);
    return (byte)(bytes[0] & mask);
  }
  
  public static final void main(String[] args) {
    String mnemonic = generateMnemonic();
    System.out.println("mnemonic = " + mnemonic);
    byte[] seed = generateSeed(mnemonic, "");
    System.out.println("seed = " + Numeric.toHexString(seed));
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\com\develop\mnemonic\MnemonicUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */