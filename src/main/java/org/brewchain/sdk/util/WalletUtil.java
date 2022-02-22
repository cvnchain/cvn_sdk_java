package org.brewchain.sdk.util;

import com.brewchain.sdk.crypto.KeyPairs;
import com.brewchain.sdk.crypto.KeyPairs;
import com.develop.mnemonic.KeyPairUtils;
import com.develop.mnemonic.MnemonicUtils;
import org.brewchain.core.crypto.cwv.keystore.KeyStoreFile;
import org.brewchain.core.crypto.cwv.keystore.KeyStoreHelper;
import org.spongycastle.util.encoders.Hex;


public final class WalletUtil {

    public static void main(String[] args) throws Exception {
//        System.out.println(getMnemonic());
//        String mnemonic = getMnemonic();
//        String mnemonic = "fragile since guard embody forget casual peanut flavor path typical collect blur";
//        KeyPairs kp = getKeyPair(mnemonic);
//        System.out.println(kp);

        String words = "gas twist liar foster crunch arrow brush market author knee fit frown";
        String ksJson = WalletUtil.genKeyStoreFromMnemonic(words,"password");

        System.out.println(ksJson);
        ksJson = "{\"ksType\":\"aes\",\"params\":{\"dklen\":256,\"c\":128,\"l\":269,\"salt\":\"27fed0c293b7a94f\"},\"pwd\":\"b68fe43f0d1a0d7aef123722670be50268e15365401c442f8806ef83b612976b\",\"cipher\":\"cbc\",\"cipherParams\":{\"iv\":\"1740afac7276ea76c23eee63f548ed43\"},\"cipherText\":\"3d80d77d9c9ddac1c44da6d8d78e6334100a60591d5338ae07ecf536b7e759692a1ac8d433ed9f0c528ac5fd9c25c38d0fb224069d835786972f6d475252f438624206b42b69b17d8d3a96ffec144ca6476520946953fc3e1c7533d0301609c72f12fd409c0d275ea8976077030212752596c26d207fba9a840aa12f9e89351be3b48d7e17ecb3eed3a8ea329b28212cf509c6b70a6f24528dc819d44b1852046382b8182804bdeb76005bf1bf8fe663fbeedfcfb93413b7bd1f103623f15ee573fade7c4dd12fc1122d41052b97598b71f66026461dbd07d66217286848eae5215d7679e9bdfd84951ca38c401af94a5ac49b51719a5dabaa3e87546e3c2d07cbd67f17afc2d4cc9497193d45aa5e74\"}";
        KeyPairs kp2 = WalletUtil.restoreFromKeyStore(ksJson,"password");
        System.out.println(kp2.getAddress());

//        //生成助记词
//        String words = WalletUtil.getMnemonic();
//        System.out.println(words);
//
//        //助记词生成公私钥地址对
//        KeyPairs kp = WalletUtil.getKeyPair(words);
//        System.out.println("地址:"+kp.getAddress());
//        System.out.println("公钥:"+kp.getPubkey());
//        System.out.println("私钥:"+kp.getPrikey());
//
//        //助记词生成keystore文件内容
//        String ksJson = WalletUtil.genKeyStoreFromMnemonic(words,"password");
//        System.out.println("keyStoreJson:"+ksJson);
//
//        //从keystore恢复公私钥
//        KeyPairs kp2 = WalletUtil.restoreFromKeyStore(ksJson,"password");
//        System.out.println("地址:"+kp2.getAddress());
//        System.out.println("公钥:"+kp2.getPubkey());
//        System.out.println("私钥:"+kp2.getPrikey());

        String s = new String(Hex.decode("706172616d6574657220696e76616c69642c2073656e646572206e6f6e6365206973206c61726765207468616e207472616e73616374696f6e206e6f6e6365".getBytes()));
        System.out.println("s:"+s);
    }

    /**
     * To generate Mnemonic Words which is to generate keyPair with function [getKeyPair](#getKeyPair)
     * @return e.g.: "gas twist liar foster crunch arrow brush market author knee fit frown"
     */
    public static String getMnemonic(){
        String words = MnemonicUtils.generateMnemonic();
//        List<String> words = Arrays.asList("apart genre supply plate doctor coach stay anger chimney stable member marble".split(" "));
        return words;
    }

    /**
     * To generate KeyStore File Content ,with Mnemonic Words, which should be encrypted and written into the file :
     * @param mnemonic
     * @param password
     * @return
     */
    public static String genKeyStoreFromMnemonic(String mnemonic,String password){
        byte[] bb = KeyPairUtils.generatePrivateKey(mnemonic, KeyPairUtils.CoinTypes.CWV);
        KeyPairs kp = CryptoUtil.privatekeyToAccountKey(bb);

        KeyStoreHelper ksh = new KeyStoreHelper(CryptoUtil.getCrypto());

        KeyStoreFile keyStoreFile = ksh.generate(kp,password);
        String str = ksh.parseToJsonStr(keyStoreFile);

        return str;
    }

    /**
     * 从私钥获取keystore
     * @param pk
     * @param password
     * @return
     */
    public static String genKeyStoreFromPk(String pk,String password){
        KeyPairs kp = CryptoUtil.privatekeyToAccountKey(pk);

        KeyStoreHelper ksh = new KeyStoreHelper(CryptoUtil.getCrypto());

        KeyStoreFile keyStoreFile = ksh.generate(kp,password);
        String str = ksh.parseToJsonStr(keyStoreFile);

        return str;
    }


    /**
     * To restore Mnemonic Words from KeyStore content with correct password.
     * @param ksJson
     * @param pwd
     * @return
     */
    public static KeyPairs restoreFromKeyStore(String ksJson,String pwd){
        KeyStoreHelper ksh = new KeyStoreHelper(CryptoUtil.getCrypto());
        return CryptoUtil.privatekeyToAccountKey(ksh.getKeyStore(ksJson,pwd).getPrikey());
    }

    /**
     * To generate KeyPair which consists of address,privateKey,publicKey.
     * @param mnemonic
     * @return
     */
    public static KeyPairs getKeyPair(String mnemonic){
        byte[] bb = KeyPairUtils.generatePrivateKey(mnemonic, KeyPairUtils.CoinTypes.CWV);
        KeyPairs kp = CryptoUtil.privatekeyToAccountKey(bb);
        return kp;
    }
    /**
     * 从私钥生成公私钥
     * @param pk 私钥
     * @return
     */
    public static KeyPairs getKeyPairFromPk(String pk){
        KeyPairs kp = CryptoUtil.privatekeyToAccountKey(pk);
        return kp;
    }

}
