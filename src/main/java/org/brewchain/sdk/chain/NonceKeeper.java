package org.brewchain.sdk.chain;

import org.brewchain.sdk.HiChain;
import org.brewchain.sdk.https.RequestBuilder;
import org.brewchain.sdk.util.RegexUtil;
import org.brewchain.sdk.https.OKHttpExecutor;
import org.brewchain.sdk.model.ChainRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 *
 * nonce维护器。sdk使用前时初始化。获取nonce，串行加一。
 */
@Slf4j
public final class NonceKeeper {

//    private static AtomicInteger nonce = new AtomicInteger(0);

    private static ConcurrentHashMap<String,AtomicInteger> nonces = new ConcurrentHashMap<>();
    private final static Pattern nonce_pattern = Pattern.compile("(?<=\"nonce\": )[0-9]*");;

    /**
     * get the nonce of current address,which must be get before transaction.
     * @param addr
     * @return
     */
    public static int getNonce(String addr){
        if(nonces.get(addr) == null){
            log.error("地址："+addr+" 的nonce不在本地缓存，重新拉取账户nonce");
            initNonce(addr);
        }
        return nonces.get(addr).get();
    }

    public static void decrNonce(String addr){
        nonces.get(addr).decrementAndGet();
    }

    /**
     * increase the nonce value of the address
     * @param addr
     */
    public static int incrNonce(String addr){
        return incrNonce(addr,1);
    }
    public static int incrNonce(String addr, int incr){
        return nonces.get(addr).addAndGet(incr);
    }

    /**
     * refresh local nonce of the address from the blockchain
     * @param address
     * @return
     */
    public static int refreshNonce(String address){
        log.info("address:"+address+" nonce is refreshing");

        String result = HiChain.getUserInfo(address);
        String nonceS = RegexUtil.getValue(nonce_pattern,result);
        if(nonceS != null){
            int i = 0;
            i = Integer.parseInt(nonceS);
            AtomicInteger nonce = new AtomicInteger(i);
            nonces.put(address,nonce);//替换为新nonce
            log.info("address:"+address+" nonce now is:{}",i);
            return nonce.get();
        }else{
            throw new IllegalArgumentException("address:"+address+" can not get nonce!");
        }
    }

    public static void initNonce(String address){
        int i = -1;
        nonces.put(address, new AtomicInteger(i));
        String result = HiChain.getUserInfo(address);
        String nonceS = RegexUtil.getValue(nonce_pattern,result);
        i = Integer.parseInt(nonceS);
        nonces.get(address).compareAndSet(-1,i);
    }



    public static void main(String[] args) throws InterruptedException {

//        System.out.println("nonce is "+NonceKeeper.getNonce("8e37db7d580b84fc096ea04a1c047b48b07a8958"));
//        System.out.println("nonce is "+NonceKeeper.getNonce("8e37db7d580b84fc096ea04a1c047b48b07a8958"));

        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                int n = NonceKeeper.getNonce("3c1ea4aa4974d92e0eabd5d024772af3762720a0");
                System.out.println(n);

            }).start();
        }

        Thread.sleep(3000);
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                int n = NonceKeeper.getNonce("d8cfb85da53678f348e30c1fc4f792a418a1f443");
                System.out.println(n);

            }).start();
        }

    }
}
