package org.brewchain.sdk;

import com.brewchain.sdk.crypto.KeyPairs;
import com.brewchain.sdk.model.Block;
import com.brewchain.sdk.model.Model.SendTransaction;
import com.brewchain.sdk.model.Model.SendTransactionOutput;
import com.brewchain.sdk.model.TokensContract20;
import com.brewchain.sdk.model.TransactionImpl;
import com.brewchain.sdk.model.TransactionImpl.TxResult;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.brewchain.core.crypto.cwv.util.BytesHelper;
import org.brewchain.sdk.https.OKHttpExecutor;
import org.brewchain.sdk.https.RequestBuilder;
import org.brewchain.sdk.model.ChainRequest;
import org.brewchain.sdk.model.TransferInfo;
import org.brewchain.sdk.util.CryptoUtil;
import org.brewchain.sdk.util.JsonPBUtil;
import org.brewchain.sdk.util.TransactionBuilder;
import org.spongycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 入口函数
 */
@Slf4j
public final class HiChain {
    static{
        HiChain.init();
    }

    public static void main(String[] args) {

//        List<TransferInfo> tos = new ArrayList<TransferInfo>(){{
//            this.add(new TransferInfo("0x69a0e3390ded433f3b1b292606ac2a782e6336de", BytesHelper.bigDecimal2HexStr(BigDecimal.valueOf(10),18)));
//        }};
//        //RC20发布
//        result = HiChain.rC20Create(address,nonce,priKey,"GOD2","Test token",tos,"发布RC20合约测试");
//        log.info("HiChain.rC20Create result=\n{}", result);
//
//        String rC20Address = "0x082ad3805e1a374f8c91260b5fe3c3a72309f6c0";
//        //查询RC20信息
//        result = HiChain.getRC20Info(rC20Address);
//        log.info("HiChain.getRC20Info result=\n{}", result);
//
//         //RC20增发
//        result = HiChain.rC20Increase(address,nonce,priKey,rC20Address,tos,"增发RC20测试");
//        log.info("HiChain.rC20Increase result=\n{}", result);
//        //RC20转账
//        result = HiChain.rC20Transfer(address,nonce,priKey,rC20Address,tos,"转账RC20合约测试");
//        log.info("HiChain.rC20Transfer result=\n{}", result);
//
//        String ownerAddress = "0x69a0e3390ded433f3b1b292606ac2a782e6336de";
//        //查询用户RC20信息
//        result = HiChain.getUserRC20Info(ownerAddress,rC20Address);
//        log.info("HiChain.getUserRC20Info result=\n{}", result);

//        log.info(ContractUtil.getContractBinCodeMSwap());
//        String codeData = "08031a14ff8a88c5c4701f4308fab0b26e58e54fd753eb8122088ac7230489e800006012";
//        try {
//            TokensContract20.ContractRC20 contractRC20 = TokensContract20.ContractRC20.newBuilder()
//                    .mergeFrom(Hex.decode(codeData)).build();
//            String st = new String(Hex.encode(contractRC20.getTos(0).toByteArray()));
//            log.info(st);
//            String stV = new String(Hex.encode(contractRC20.getValues(0).toByteArray()));
//            BigDecimal b = BytesHelper.hexStr2BigDecimal(stV,18,8);
//            log.info(b.toPlainString());
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 查询交易
     * @param txHash 交易哈希
     * @return
     */
    public static TransactionImpl.TxResult getTxInfo(String txHash) {
        ChainRequest cr = RequestBuilder.buildGetTxInfo(txHash);
        return (TransactionImpl.TxResult) doExecute(cr,TransactionImpl.TxResult.class);
    }

    //凡是使用sdk都要预先做一次初始化
    public static void init(){
        //通过助记词获取用户地址私钥并保存
//        KeyPairs kp = WalletUtil.getKeyPair(mnemonicWords);
//        log.info("Loaded KeyPairs Info, Address:"+kp.getAddress()
//                    +"; pubKey:"+kp.getPubkey());
//        Config.setKeyPairs(kp);

        //初始化domain-pool,它必须先做。完成后再进行下面的操作。
        //DomainPool.getDynamicDomians();
//        PureOkHttpExecutor.init();
//        OKHttpExecutor.init();
//        LocalCrypto.getInstance().sign("1111111111111111111111111111111111111111111111111111111111111111",new byte[]{1});
        log.info("初始化完成！");

    }

    /**
     * 向主链发交易 0普通账户,1=多重签名账户，2=20合约账户，3=721合约账户,4=CVM合约,5=JSVM合约(可并行)
     */
    public static TransactionImpl.TxResult doTransaction(SendTransaction st){
//        SendTransaction stt = st.toBuilder().setNonce( NonceKeeper.getNonce(st.getAddress())).build();
        return sendTx(TransactionBuilder.build(st));
    }

    /**
     * 向主链发交易 0普通账户,1=多重签名账户，2=20合约账户，3=721合约账户,4=CVM合约,5=JSVM合约(可并行)
     */
    public static TransactionImpl.TxResult sendTx(String tx){
        log.info(tx);
        ChainRequest req = RequestBuilder.buildTransactionReq(tx);
        return (TransactionImpl.TxResult) doExecute(req,TransactionImpl.TxResult.class);
    }

    /**
     * 转账CWV
     * @param fromAddr 地址
     * @param nonce 交易序号
     * @param fromPriKey 私钥
     * @param exData 扩展信息
     * @param tos 接收者集合{toAddr:接收地址,amount:金额}
     * @return
     */
    public static TransactionImpl.TxResult transferTo(String fromAddr,int nonce, String fromPriKey, String exData,
                                                      List<TransferInfo> tos) {
        //发交易请求
        return sendTx(getTransferToTx(fromAddr,nonce,fromPriKey,exData,tos));
    }
    public static String getTransferToTx(String fromAddr,int nonce, String fromPriKey, String exData,
                                                      List<TransferInfo> tos) {
        if(tos == null || tos.isEmpty()) {
            throw new RuntimeException("param [tos] should not be null");
        }
        //构造交易参数
        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(fromAddr);
        st.setPrivateKey(fromPriKey);
        st.setNonce(nonce);
        st.setExData(exData);
        st.setTimestamp(System.currentTimeMillis());

        for (TransferInfo to : tos) {
            SendTransactionOutput.Builder sto = SendTransactionOutput.newBuilder();
            sto.setAddress(to.getToAddr());
            sto.setAmount("0x0");
            if(!"".equals(to.getAmount()) && to.getAmount()!=null){
                sto.setAmount(to.getAmount());
            }
            st.addOutputs(sto.build());
        }
        //发交易请求
        return TransactionBuilder.build(st.build());
    }


    /**
     * 发布合约
     * @param fromAddr 账户地址
     * @param fromPriKey 私钥
     * @param codeData 合约编译后的二进制码
     * @param exData 扩展信息
     * @return
     */
    public static TransactionImpl.TxResult contractCreate(String fromAddr,int nonce,  String fromPriKey, String codeData, String exData) {
        //发交易请求
        return sendTx(getContractCreateTx(fromAddr,nonce,fromPriKey,codeData,exData));
    }

    /**
     * 发布合约
     * @param fromAddr 账户地址
     * @param fromPriKey 私钥
     * @param codeData 合约编译后的二进制码
     * @param exData 扩展信息
     * @return
     */
    public static String getContractCreateTx(String fromAddr,int nonce,  String fromPriKey, String codeData, String exData) {
        if(fromAddr == null || "".equals(fromAddr)) {
            throw new IllegalArgumentException("param [fromAddr] should not be null");
        }
        if(fromPriKey == null || "".equals(fromPriKey)) {
            throw new IllegalArgumentException("param [fromPriKey] should not be null");
        }
        if(codeData == null || "".equals(codeData)) {
            throw new IllegalArgumentException("param [codeData] should not be null");
        }
        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(fromAddr)
                .setPrivateKey(fromPriKey)
                .setNonce(nonce)
                .setCodeData(ByteString.copyFrom(Hex.decode(codeData.startsWith("0x")? codeData.substring(2)  :  codeData)))
                .setInnerCodeType(SendTransaction.CodeType.CVM_CONTRACT)
                .setTimestamp(System.currentTimeMillis());
        if(exData !=null && "".equals(exData)) {
            st.setExData(exData);
        }

        //发交易请求
        return TransactionBuilder.build(st.build());
    }
    /**
     * 执行合约
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param contractAddress 合约地址
     * @param codeData 合约方法及参数编译后的二进制码
     * @param exData 扩展信息
     * @return
     */
    public static TransactionImpl.TxResult contractCall(String fromAddr,int nonce, String fromPriKey, String contractAddress, String codeData, String exData) {
        //发交易请求
        return sendTx(getContractCallTx(fromAddr,nonce,fromPriKey,contractAddress,codeData,exData));
    }
    /**
     * 执行合约
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param contractAddress 合约地址
     * @param codeData 合约方法及参数编译后的二进制码
     * @param exData 扩展信息
     * @return
     */
    public static String getContractCallTx(String fromAddr,int nonce, String fromPriKey, String contractAddress, String codeData, String exData) {
        if(fromAddr == null || "".equals(fromAddr)) {
            throw new IllegalArgumentException("param [fromAddr] should not be null");
        }
        if(fromPriKey == null || "".equals(fromPriKey)) {
            throw new IllegalArgumentException("param [fromPriKey] should not be null");
        }
        if(codeData == null || "".equals(codeData)) {
            throw new IllegalArgumentException("param [codeData] should not be null");
        }
        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(fromAddr)
                .setPrivateKey(fromPriKey)
                .setNonce(nonce)
                .setCodeData(ByteString.copyFrom(Hex.decode(codeData.startsWith("0x")? codeData.substring(2)  :  codeData)))
                .setInnerCodeType(SendTransaction.CodeType.CVM_CONTRACT)
                .addOutputs(SendTransactionOutput.newBuilder().setAddress(contractAddress))
                .setTimestamp(System.currentTimeMillis());

        if(exData !=null && "".equals(exData)) {
            st.setExData(exData);
        }

        //发交易请求
        return TransactionBuilder.build(st.build());
    }

    /**
     *  发行RC20
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param symbol RC20标志
     * @param name RC20名称
     * @param tos RC20接收账户信息
     * @param exData 扩展信息
     * @return
     */
    public static TransactionImpl.TxResult rC20Create(String fromAddr,int nonce, String fromPriKey, String symbol, String name, List<TransferInfo> tos, String exData) {
        //发交易请求
        return sendTx(getRC20CreateTx(fromAddr,nonce,fromPriKey,symbol,name,tos,exData));
    }

    /**
     *  发行RC20
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param symbol RC20标志
     * @param name RC20名称
     * @param tos RC20接收账户信息
     * @param exData 扩展信息
     * @return
     */
    public static String getRC20CreateTx(String fromAddr,int nonce, String fromPriKey, String symbol, String name, List<TransferInfo> tos, String exData) {
        if(fromAddr == null || "".equals(fromAddr)) {
            throw new IllegalArgumentException("param [fromAddr] should not be null");
        }
        if(fromPriKey == null || "".equals(fromPriKey)) {
            throw new IllegalArgumentException("param [fromPriKey] should not be null");
        }
        if(tos == null || tos.isEmpty()) {
            throw new IllegalArgumentException("param [tos] should not be empty");
        }
        if(symbol == null || "".equals(symbol)) {
            throw new IllegalArgumentException("param [symbol] should not be null");
        }
        if(name == null || "".equals(name)) {
            throw new IllegalArgumentException("param [name] should not be null");
        }

        TokensContract20.ContractRC20.Builder data = TokensContract20.ContractRC20.newBuilder();
        data.setDecimals(18)
                .setName(name)
                .setFunction(TokensContract20.ContractRC20.Function20.CONSTRUCT_PRINTABLE)
                .setSymbol(symbol);
        for(TransferInfo to : tos) {
            data
                    .addTos(ByteString.copyFrom(BytesHelper.hexStringToBytes(to.getToAddr())))
                    .addValues(ByteString.copyFrom(BytesHelper.hexStringToBytes(to.getAmount())));
        }
        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(fromAddr)
                .setPrivateKey(fromPriKey)
                .setNonce(nonce)
                .setCodeData(ByteString.copyFrom(data.build().toByteArray()))
                .setInnerCodeType(SendTransaction.CodeType.RC20_CONTRACT)
                .setTimestamp(System.currentTimeMillis());
        if(exData !=null && "".equals(exData)) {
            st.setExData(exData);
        }

        //发交易请求
        return TransactionBuilder.build(st.build());
    }

    /**
     * 转账RC20
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param rC20Address RC20地址
     * @param tos RC20接收账户信息
     * @param exData 扩展信息
     * @return
     */
    public static TransactionImpl.TxResult rC20Transfer(String fromAddr,int nonce, String fromPriKey, String rC20Address, List<TransferInfo> tos,String exData) {
        //发交易请求
        return sendTx(getRC20TransferTx(fromAddr,nonce,fromPriKey,rC20Address,tos,exData));
    }

    /**
     * 转账RC20
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param rC20Address RC20地址
     * @param tos RC20接收账户信息
     * @param exData 扩展信息
     * @return
     */
    public static String getRC20TransferTx(String fromAddr,int nonce, String fromPriKey, String rC20Address, List<TransferInfo> tos,String exData) {
        if(fromAddr == null || "".equals(fromAddr)) {
            throw new IllegalArgumentException("param [fromAddr] should not be null");
        }
        if(fromPriKey == null || "".equals(fromPriKey)) {
            throw new IllegalArgumentException("param [fromPriKey] should not be null");
        }
        if(tos == null || tos.isEmpty()) {
            throw new IllegalArgumentException("param [tos] should not be empty");
        }
        if(rC20Address == null || "".equals(rC20Address)) {
            throw new IllegalArgumentException("param [symbol] should not be null");
        }

        TokensContract20.ContractRC20.Builder data = TokensContract20.ContractRC20.newBuilder();
        data.setDecimals(18)
                .setFunction(TokensContract20.ContractRC20.Function20.TRANSFERS);
        for(TransferInfo to : tos) {
            data
                    .addTos(ByteString.copyFrom(BytesHelper.hexStringToBytes(to.getToAddr())))
                    .addValues(ByteString.copyFrom(BytesHelper.hexStringToBytes(to.getAmount())));
        }
        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(fromAddr)
                .setPrivateKey(fromPriKey)
                .setNonce(nonce)
                .setCodeData(ByteString.copyFrom(data.build().toByteArray()))
                .setInnerCodeType(SendTransaction.CodeType.RC20_CONTRACT)
                .setTimestamp(System.currentTimeMillis());
        if(exData !=null && "".equals(exData)) {
            st.setExData(exData);
        }
        SendTransactionOutput.Builder to = SendTransactionOutput.newBuilder();
        to.setAddress(rC20Address);
        st.addOutputs(to);

        //发交易请求
        return TransactionBuilder.build(st.build());
    }

    /**
     * 增发RC20
     * @param fromAddr 账户
     * @param fromPriKey 私钥
     * @param rC20Address RC20地址
     * @param tos RC20接收账户信息
     * @param exData 扩展信息
     * @return
     */
    public static TransactionImpl.TxResult rC20Increase(String fromAddr,int nonce, String fromPriKey, String rC20Address, List<TransferInfo> tos ,String exData) {
        //发交易请求
        return sendTx(getRC20IncreaseTx(fromAddr,nonce,fromPriKey,rC20Address,tos,exData));
    }

    public static String getRC20IncreaseTx(String fromAddr,int nonce, String fromPriKey, String rC20Address, List<TransferInfo> tos ,String exData) {
        if(fromAddr == null || "".equals(fromAddr)) {
            throw new IllegalArgumentException("param [fromAddr] should not be null");
        }
        if(fromPriKey == null || "".equals(fromPriKey)) {
            throw new IllegalArgumentException("param [fromPriKey] should not be null");
        }
        if(tos == null || tos.isEmpty()) {
            throw new IllegalArgumentException("param [tos] should not be empty");
        }
        if(rC20Address == null || "".equals(rC20Address)) {
            throw new IllegalArgumentException("param [rC20Address] should not be null");
        }
        TokensContract20.ContractRC20.Builder data = TokensContract20.ContractRC20.newBuilder();
        data.setDecimals(18)
                .setFunction(TokensContract20.ContractRC20.Function20.PRINT);
        for(TransferInfo to : tos) {
            data.addTos(ByteString.copyFrom(BytesHelper.hexStringToBytes(to.getToAddr())))
                    .addValues(ByteString.copyFrom(BytesHelper.hexStringToBytes(to.getToAddr())));
        }
        SendTransaction.Builder st = SendTransaction.newBuilder();
        st.setAddress(fromAddr)
                .setPrivateKey(fromPriKey)
                .setNonce(nonce)
                .setCodeData(ByteString.copyFrom(data.build().toByteArray()))
                .setInnerCodeType(SendTransaction.CodeType.RC20_CONTRACT)
                .setTimestamp(System.currentTimeMillis());
        if(exData !=null && "".equals(exData)) {
            st.setExData(exData);
        }
        SendTransactionOutput.Builder to = SendTransactionOutput.newBuilder();
        to.setAddress(rC20Address);
        st.addOutputs(to);

        //发交易请求
        return TransactionBuilder.build(st.build());
    }

    /**
     * 查询RC20信息
     * @param rC20Address RC20合约地址
     * @return
     */
    public static String getRC20Info(String rC20Address){
        ChainRequest cr = RequestBuilder.buildGetRC20InfoReq(rC20Address);
        return doExecute(cr);
    }

    /**
     * 查询账户RC20信息
     * @param ownerAddress 账户
     * @param rC20Address RC20合约地址
     * @return
     */
    public static String getUserRC20Info(String ownerAddress,String rC20Address){
        ChainRequest cr = RequestBuilder.buildGetUserRC20InfoReq(ownerAddress, rC20Address);
        return doExecute(cr);
    }

    /**
     * To get account info from an address:
     * @param address
     * @return
     */
    public static String getUserInfo(String address){
        ChainRequest cr = RequestBuilder.buildGetUserInfoReq(address);
        String result = doExecute(cr);
        if(result.indexOf("账户不存在")!=-1) {
            result = "{\"retCode\": 1,\"address\": \""+address+"\",\"nonce\": 0,\"balance\": \"0x00\",\"status\": 0}";
        }
        return result;
    }

    /**
     * To get nonce from an address:
     * @param address
     * @return
     */
    public static long getNonce(String address){
        ChainRequest cr = RequestBuilder.buildGetUserInfoReq(address);
        String result = doExecute(cr);
        if(result.indexOf("账户不存在")!=-1) {
            return 0;
        }
        int fromIndex = result.indexOf("nonce\": ")+"nonce\": ".length();
        String nonceS = result.substring(fromIndex,result.indexOf(",",fromIndex));
        return Long.parseLong(nonceS);
    }

    /**
     * To get latest block info of blockchain:
     * @return
     */
    public static Block.BlockRet getLastedBlock(){
        ChainRequest cr = RequestBuilder.buildGetLastedBlock();
        return (Block.BlockRet)doExecute(cr,Block.BlockRet.class);
    }

    /**
     * To get block info with the exact block height:
     * @param height the height of block
     * @return
     */
    public static Block.BlockRet getBlockByHeight(long height){
        ChainRequest cr = RequestBuilder.buildGetBlockByHeightReq(height);
        return (Block.BlockRet)doExecute(cr,Block.BlockRet.class);
    }


    /**
     * hexString 转换为UTF-8格式的String .
     * 读取交易的exdata使用
     */
    public static String hexStringToUTF8(String hexString) throws UnsupportedEncodingException {
        return new String(CryptoUtil.hexStrToBytes(hexString),"utf-8");
    }
    public static String doExecute(ChainRequest cr){
        return OKHttpExecutor.execute(cr);
    }
    public static Message doExecute(ChainRequest cr, Class<? extends Message> messageClazz){
        String ret = OKHttpExecutor.execute(cr);
        Message.Builder msgB = null;
        try {
            msgB = (Message.Builder)messageClazz.getMethod("newBuilder", null).invoke(null);
            JsonPBUtil.json2PB(ret,msgB);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("messageClazz error:{}", e.getClass().getName());
            msgB.setField(msgB.getDescriptorForType().findFieldByName("retCode"), -2)
                    .setField(msgB.getDescriptorForType().findFieldByName("retMsg"), "sdk error: messageClazz error");
        } catch (JsonFormat.ParseException e) {
            msgB.setField(msgB.getDescriptorForType().findFieldByName("retCode"), -2)
                    .setField(msgB.getDescriptorForType().findFieldByName("retMsg"), "sdk error: chain info parse error");
        }

        return msgB.build();
    }

    public static TxResult sendTx(String tx, String url) {
        log.info(tx);
        ChainRequest req = RequestBuilder.buildTransactionReq(tx);
        req.setUrl(url);
        return (TxResult)doExecute(req, TxResult.class);
    }

    /**
     * String reg = "/^(0x)?CVN[A-Fa-f0-9]{40}/g";
     * @param address
     * @return
     */
    public static boolean validateAddress(String address){
        String reg = "^[A-Fa-f0-9]{40}";
        if(address.startsWith("0x") ) {
            address = address.substring(address.indexOf("0x") + 2);
        }
        if(address.startsWith(KeyPairs.ADDR_PRE)) {
            address = address.substring(address.indexOf(KeyPairs.ADDR_PRE) + KeyPairs.ADDR_PRE.length());
        }
        if(address.length() == 40 && Pattern.matches(reg,address)) {
            return true;
        } else {
            return false;
        }
    }



}
