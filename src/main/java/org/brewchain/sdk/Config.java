package org.brewchain.sdk;

import com.brewchain.sdk.crypto.KeyPairs;
import org.brewchain.sdk.https.OKHttpExecutor;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public final class Config {
    private Config(){}
    
    private static KeyPairs kp = null;
    
//    开发环境
    /**主链地址*/
    public static String host = "http://43.254.1.176:8000";
    public static int chainId = 168;

    //是否是生产环境
    @Deprecated
    public static boolean isProd = false;

    /**是否使用多队列交易执行器，默认不启用*/
    public static boolean isMultiQueueExecUsed = false;
    /**交易并行队列数目，isMultiQueueExecUsed=true时才有用*/
    public static int MultiExecQueueNum = 20;
    /**是否开启串行化交易发送，即单个账户并发发起交易，默认不启用*/
    public static boolean isSerialSendTx = false;


    public static String[] MainHosts = {
            "http://localhost:8000",
            "http://localhost:8000",
            "http://localhost:8000",
            "http://localhost:8000",
            "http://localhost:8000"
    };
    
    static  Random  rand= new Random();
    public static String getMainHost(){
        int size = MainHosts.length;
        int n = rand.nextInt(size);
        return MainHosts[n];
    }
    
    protected static void setKeyPairs(KeyPairs kp){
        
        Config.kp = kp;
    }
    
    public static String getAddress(){
        return kp.getAddress();
    }
    public static String getPrivateKey(){
        return kp.getPrikey();
    }
    public static String getPublicKey(){
        return kp.getPubkey();
    }

    public static void init(String newHost){
        Config.host = newHost;
    }
    public static void init(String newHost, int chainId){
        Config.host = newHost;
        Config.chainId = chainId;
    }

    public static void setTimeOut(long timeOut, TimeUnit timeUnit){
        OKHttpExecutor.setTimeOut(timeOut,timeUnit);
    }
}
