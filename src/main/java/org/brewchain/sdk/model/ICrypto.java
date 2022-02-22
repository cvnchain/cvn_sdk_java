package org.brewchain.sdk.model;

public interface ICrypto {

    /**
     * 生成交易签名
     * @param privateKey  私钥
     * @param body 待签名body
     * @return
     */
    byte[] sign(String privateKey,byte[] body);
}
