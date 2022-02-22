package org.brewchain.sdk.https;


import org.brewchain.sdk.Config;
import org.brewchain.sdk.model.ChainRequest;

public final class RequestBuilder {

    public static ChainRequest buildTestReq(){
        return new ChainRequest(
                Config.host+"/helloworld"
                ,"{\"name\":\"张三\",\"age\":\"18\"}");
    }
    
    public static ChainRequest buildGetStorageReq(String body){
        return new ChainRequest(Config.host+"/fbs/cvm/pbgcs.do"
        ,body);
    }
    
    public static ChainRequest buildGetUserInfoReq(String addr){
        return new ChainRequest(
                Config.host+"/fbs/act/pbgac.do"
                ,"{\"address\": \""+addr+"\"}");
    }

    public static ChainRequest buildGetUserRC20InfoReq(String ownerAddr,String rc20Addr){
        return new ChainRequest(
                Config.host+"/fbs/c20/pbqvalue.do"
                ,String.format("{\"token_address\":\"%s\",\"owner_address\":\"%s\"}", rc20Addr.startsWith("0x")?rc20Addr:rc20Addr.substring(2), ownerAddr.startsWith("0x")?ownerAddr.substring(2):ownerAddr));
    }

    public static ChainRequest buildGetBlockByHeightReq(long height){
        return new ChainRequest(
                Config.host+"/fbs/bct/pbgbn.do"
                ,"{\"height\": "+height+",\"type\": 0}");
    }

    public static ChainRequest buildGetLastedBlock(){
        return new ChainRequest(
                Config.host+"/fbs/bct/pbglb.do"
                ,"{}");
    }

    /**
     * 获取节点地址请求 todo
     */
    public static ChainRequest buildGetDynamicDomainsReq(){
        return new ChainRequest("http://localhost:9000"+"/getDomains","");
    }

    public static ChainRequest builtGetTxInfoReq(String hash){
        return new ChainRequest(Config.host+"/fbs/tct/pbgth.do",
                "{\"hash\": \""+hash+"\"}");
    }
    
    

    
    public static ChainRequest buildTransactionReq(String tx){
        return new ChainRequest(
                Config.host+"/fbs/tct/pbmtx.do"
                ,"{\"tx\":\""+tx+"\"}");
    }
    
    
    private static String getDomain(){
        return DomainPool.randomGet();
    }

    public static ChainRequest buildGetTxInfo(String txHash) {
        return new ChainRequest(
                Config.host+"/fbs/tct/pbgth.do"
                ,"{\"hash\":\""+txHash+"\"}");
    }

    public static ChainRequest buildGetRC20InfoReq(String rC20Address) {
        return new ChainRequest(
                Config.host+"/fbs/c20/pbqinfo.do"
                ,"{\"token_address\": \""+rC20Address+"\"}");
    }
}
