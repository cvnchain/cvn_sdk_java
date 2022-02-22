package org.brewchain.sdk.util;

import com.brewchain.sdk.model.Model.*;
import com.brewchain.sdk.model.Transaction.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.brewchain.sdk.Config;

import java.math.BigInteger;

@Slf4j
public class TransactionBuilder {


    public static String build(SendTransaction pb){
        TransactionInfo.Builder ti = TransactionInfo.newBuilder();
        TransactionBody.Builder tb = TransactionBody.newBuilder();
        tb.setChainId(Config.chainId);
        tb.setAddress(ByteString.copyFrom(CryptoUtil.hexStrToBytes(AccountUtil.cvnFiler(pb.getAddress()))));

        tb.setInnerCodetype(pb.getInnerCodeTypeValue());
        tb.setCodeData(pb.getCodeData());


        if (isNotBlank(pb.getExData())) {
            byte[] datas = new byte[0]; 
            try {
                datas = pb.getExData().getBytes("utf-8");
            } catch (Exception e) {
                log.error("exdata 编码错误",e);
            }
            tb.setExtData(ByteString.copyFrom(datas));
        }
        
        tb.setNonce(pb.getNonce());
        
        tb.setFeeHi(pb.getFeeHi());
        tb.setFeeLow(pb.getFeeLow());

        if (pb.getTimestamp() == 0) {
            tb.setTimestamp(System.currentTimeMillis());
        } else {
            tb.setTimestamp(pb.getTimestamp());
        }

        for (SendTransactionOutput output : pb.getOutputsList()) {
            TransactionOutput.Builder oTransactionOutput = TransactionOutput.newBuilder();
            oTransactionOutput.setAddress(ByteString.copyFrom(CryptoUtil.hexStrToBytes(AccountUtil.cvnFiler(output.getAddress()))));
            if(isNotBlank(output.getAmount())){
                oTransactionOutput.setAmount(
                        ByteString.copyFrom(bigIntegerToBytes(new BigInteger(output.getAmount()))));
            }
            if (output.getCryptoTokenCount() > 0) {
                for (String cryptoToken : output.getCryptoTokenList()) {
                    oTransactionOutput.addCryptoToken(ByteString.copyFrom(CryptoUtil.hexStrToBytes(cryptoToken)));
                }
            }
            if (isNotBlank(output.getSymbol())) {
                oTransactionOutput.setSymbol(ByteString.copyFrom(output.getSymbol().getBytes()));
            }
            if (isNotBlank(output.getToken())) {
                oTransactionOutput.setToken(ByteString.copyFrom(output.getToken().getBytes()));
                oTransactionOutput.setTokenAmount(ByteString
                        .copyFrom(bigIntegerToBytes(new BigInteger(output.getTokenAmount()))));
            }
            tb.addOutputs(oTransactionOutput);
        }

        ti.setBody(tb);
//        log.debug("任务签名执行中...", tb.build());
//        log.debug("签名前==》\n{}",new JsonFormat().printToString(ti.build()));
        ti.setSignature(ByteString
                .copyFrom(
                        LocalCrypto.getInstance().sign(AccountUtil.cvnFiler(pb.getPrivateKey()),tb.build().toByteArray())
//                        CryptoUtil.sign(pb.getPrivateKey(), tb.build().toByteArray())
                ));
//        log.debug("任务签名执行完毕");
//        log.debug("签名后==》\n{}",new JsonFormat().printToString(ti.build()));
        return CryptoUtil.bytesToHexStr(ti.build().toByteArray());

    }


    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static byte[] bigIntegerToBytes(BigInteger value) {
        if (value == null) {
            return null;
        } else {
            byte[] data = value.toByteArray();
            if (data.length != 1 && data[0] == 0) {
                byte[] tmp = new byte[data.length - 1];
                System.arraycopy(data, 1, tmp, 0, tmp.length);
                data = tmp;
            }

            return data;
        }
    }

    public static boolean isAnyBlank(final CharSequence... css) {
        if (css == null || css.length == 0) {
            return false;
        }
        for (final CharSequence cs : css){
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isNoneBlank(final CharSequence... css) {
        return !isAnyBlank(css);
    }
    
}
