package org.brewchain.sdk.https;

import org.brewchain.sdk.Config;
import org.brewchain.sdk.model.ChainException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务节点池。
 */
@Slf4j
public final class DomainPool {
    
    public static ScheduledThreadPoolExecutor schedule = null;
    public static volatile CopyOnWriteArrayList<String> pool= new CopyOnWriteArrayList<>();
    public static Random random = new Random();
    
    static {
//        refreshPool();
    }
    
    public static AtomicBoolean isRunning = new AtomicBoolean(false);
    
    public static void getDynamicDomians(){
        isRunning.compareAndSet(false,true);
        if(isRunning.get()){
            log.debug("Getting Dynamic Domians Is Already Started!");
            return;
        }
        for(String mainHost: Config.MainHosts){
            String url = mainHost+"";
            String body = "";
            try {
                //fixme 去除NativeHttpHandler
                String rel = NativeHttpHandler.post(url,body);
                //todo 解析rel,存入pool常量。

                isRunning.compareAndSet(true,false);
                return;
            } catch (IOException e) {
                log.error("Getting Dynamic Domians Error!",e);
                continue;
            }
        }
        isRunning.compareAndSet(true,false);
        log.error("Could Not Getting Dynamic Domians! Please Check Your Device's Network!");
    }
    
   /* public synchronized static void refreshPool(){
        
//        if(schedule == null) {
//            schedule = new ScheduledThreadPoolExecutor(1);
//
//            schedule.scheduleWithFixedDelay(() -> {
//                Thread.currentThread().setName("DomainPool_Refresh_Task");

                try {
                    //获取请求报文
                    ChainRequest cr = RequestBuilder.buildGetDomainsReq();
                    //发请求，获取响应
                    String result = OKHttpExecutor.execute(cr);
                    //解析，保存，替换
                    pool.clear();
                    pool.addAll(JSON.parseObject(result).getJSONArray("domainList").toJavaList(String.class));
                } catch (Exception e) {
                    log.error("DomainPool refresh task error",e);
                    
                }

//            }, 1, 10, TimeUnit.SECONDS);
//        }else{
//            log.info("DomainPool Is Already Running");
//        }
    }
*/
    /**
     * pool为空时会抛异常
     */
    public static String randomGet(){
        //fixme 感觉做查询不需要真随机。隐患：不均匀
        String domain = "";
        try {
            int size = pool.size();
            int n = random.nextInt(size);
            domain = pool.get(n);
        } catch (Exception e) {//pool为空
            log.error("Domain Pool Is Empty!");
            //这里直接抛出异常，
            throw new ChainException("Domain Pool Is Empty!");
//            while (pool.size()==0){
//                try {
//                    log.info("waiting Domain pool be filled");
//                    Thread.sleep(1000);
//                } catch (InterruptedException e1) {
//                    log.error("should not reach here");
//                    throw new ChainException("never show up exception");
//                }
//            }
//            domain = pool.get(0);
        }
        return domain;
    }

    public static void remove(String target){
        pool.remove(target);
        if(pool.size() == 0){
            log.debug("Domain Pool Is Empty! Starting Fetching It!");
            //动态服务domain删光了，重新获取。
            getDynamicDomians();
        }
    }
    

    public static void main(String[] args) throws InterruptedException {

//        while (true){
//            try {
//                String host = randomGet();
//                System.out.println(host);
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Thread.sleep(1000);
//
//        }

        int n = new Random().nextInt(0);
        System.out.println(n);


    }
    
    
}
