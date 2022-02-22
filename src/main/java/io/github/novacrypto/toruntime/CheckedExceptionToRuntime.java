package io.github.novacrypto.toruntime;

public final class CheckedExceptionToRuntime {
  public static <T> T toRuntime(Func<T> function) {
    try {
      return function.run();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
  }
  
  public static void toRuntime(Action function) {
    try {
      function.run();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
  }
  
  public static interface Action {
    void run() throws Exception;
  }
  
  public static interface Func<T> {
    T run() throws Exception;
  }
}


/* Location:              C:\Users\Administrator\Desktop\mnemonic-sdk-1.0.0.jar!\io\github\novacrypto\toruntime\CheckedExceptionToRuntime.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */