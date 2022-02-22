package io.github.novacrypto.bip32.derivation;

import java.util.HashMap;
import java.util.Map;

public final class CkdFunctionResultCacheDecorator<Key> implements CkdFunction<Key> {
  private final CkdFunction<Key> decoratedCkdFunction;
  
  public static <Key> CkdFunction<Key> newCacheOf(CkdFunction<Key> decorated) {
    return new CkdFunctionResultCacheDecorator<>(decorated);
  }
  
  private final Map<Key, HashMap<Integer, Key>> cache = new HashMap<>();
  
  private CkdFunctionResultCacheDecorator(CkdFunction<Key> decoratedCkdFunction) {
    this.decoratedCkdFunction = decoratedCkdFunction;
  }
  
  public Key deriveChildKey(Key parent, int childIndex) {
    Map<Integer, Key> mapForParent = getMapOf(parent);
    Key child = mapForParent.get(Integer.valueOf(childIndex));
    if (child == null) {
      child = this.decoratedCkdFunction.deriveChildKey(parent, childIndex);
      mapForParent.put(Integer.valueOf(childIndex), child);
    } 
    return child;
  }
  
  private Map<Integer, Key> getMapOf(Key parentKey) {
    HashMap<Integer, Key> mapForParent = this.cache.get(parentKey);
    if (mapForParent == null) {
      mapForParent = new HashMap<>();
      this.cache.put(parentKey, mapForParent);
    } 
    return mapForParent;
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\bip32\derivation\CkdFunctionResultCacheDecorator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */