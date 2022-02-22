package org.brewchain.sdk.util;


import com.brewchain.sdk.crypto.ICryptoHandler;
import com.brewchain.sdk.crypto.impl.EncInstance;
import lombok.Getter;
import org.brewchain.core.crypto.cwv.util.BytesHelper;
import com.brewchain.sdk.crypto.KeyPairs;
import org.spongycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;

public class CryptoUtil {

    @Getter
    private static ICryptoHandler crypto;
    static {
        crypto = new EncInstance();
        try {
            ((EncInstance) crypto).startup();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 可兼容以"0x"开头的16进制字符串的转换
     */
    public static byte[] hexStrToBytes(String hexStr){
//        return crypto.hexStrToBytes(hexStr);
        return BytesHelper.hexStringToBytes(hexStr);
    }

    public static String bytesToHexStr(byte[] bytes){
//        return crypto.bytesToHexStr(bytes);
        return BytesHelper.toHexString(bytes);
    }

    public static String hexEncodeWithPrefix(byte[] bytes){
//        return crypto.bytesToHexStr(bytes);
        return "0x"+BytesHelper.toHexString(bytes);
    }

    public static byte[] sha3(byte[] contentBytes){
        return crypto.sha3(contentBytes);
    }

    public static KeyPairs privatekeyToAccountKey(byte[] pkBytes){
        return crypto.privatekeyToAccountKey(pkBytes);
    }
    public static KeyPairs privatekeyToAccountKey(String pk){
        return privatekeyToAccountKey(Hex.decode(pk));
    }

    public static byte[] sign(String privateKey,byte[] contentBytes){
        return crypto.sign(Hex.decode(privateKey),contentBytes);
    }
    public static String signHex(String privateKey,byte[] contentBytes){
        return bytesToHexStr(sign(privateKey,contentBytes));
    }

    public static boolean verifySign(String pubKey,byte[] cont,String sign){
        return crypto.verify(Hex.decode(pubKey),cont,hexStrToBytes(sign));
    }

    public static String privateKeyToAddress(byte[] pkBytes){
        return privatekeyToAccountKey(pkBytes).getAddress();
    }

    public static String privateKeyToPublicKey(byte[] pkBytes){
        return privatekeyToAccountKey(pkBytes).getPubkey();
    }

    public static KeyPairs getRandomKP(){
        return crypto.genAccountKey();
    }


    public static String add0X(String s) {
        if (s == null)
            return null;
        return s.startsWith("0x") ? s : "0x" + s;
    }

    public static String del0X(String s) {
        if (s == null)
            return null;
        return s.startsWith("0x") ? s.substring(2) : s;
    }

    public static void main(String[] args) {

        KeyPairs keyPairs = getRandomKP();
        System.out.println("address="+keyPairs.getAddress());
        String address = crypto.privatekeyToAccountKey(Hex.decode(keyPairs.getPrikey())).getAddress();
        System.out.println("address="+address);

    }
}
