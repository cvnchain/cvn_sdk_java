package org.brewchain.sdk.util;

import org.brewchain.sdk.model.ICrypto;

public class LocalCrypto implements ICrypto {
    
    private static LocalCrypto instance;
    
    static{
        instance = new LocalCrypto();
    }
    
    public static LocalCrypto getInstance(){
        return instance;
    }

    private LocalCrypto() {
    }

    @Override
    public byte[] sign(String privateKey, byte[] body) {

        return CryptoUtil.sign(privateKey, body);
        
    }

    public static void main(String[] args) {
        LocalCrypto.getInstance().sign("1111111111111111111111111111111111111111111111111111111111111111",new byte[]{1});
        
        
    }
}
