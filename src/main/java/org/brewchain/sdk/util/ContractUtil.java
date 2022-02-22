package org.brewchain.sdk.util;

import lombok.extern.slf4j.Slf4j;
import org.brewchain.sdk.contract.abi.ContractJSONParser;
import org.brewchain.sdk.contract.abi.Function;
import org.brewchain.sdk.contract.abi.Tuple;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ContractUtil {
    public static Map<String,String> contractFileMap = new HashMap<String,String>(){{
        this.put("mswap", "MSwap.bin");
    }};
    static String binCodeMSwap ;
    /**
     * 获取合约方法参数编码
     * @param abi 合约abi
     * @param functionName 合约方法
     * @param args 参数
     * uint8  ------->  int
     * bytes  ------->  bytesToHexBytes
     * uint256  ------->   BigInteger
     * string  ------->   String
     * int32  ------->   BigInteger
     * address  ------->BigInteger 16进制
     * uint256[]  ---------->BigInteger[]
     * @return
     */
    public static String getFunctionBinCode(String abi, String functionName, Object... args) {
        Function function = null;
        try {
            List<Function> list = ContractJSONParser.parseFunctions(abi).stream().filter((Function f) -> f.getName().equals(functionName)).collect(Collectors.toList());
            if(!list.isEmpty()) {
                function = list.get(0);
            } else {
                throw new IllegalArgumentException("function does not match abi");
            }
        } catch (ParseException e) {
            log.error("functionMap init error", e);
            throw new IllegalArgumentException("abi error");
        }
        return getFunctionBinCode(function,args);
    }

    /**
     * 获取合约方法参数编码
     * @param function
     * @param args
     * uint8  ------->  int
     * bytes  ------->  bytesToHexBytes
     * uint256  ------->   BigInteger
     * string  ------->   String
     * int32  ------->   BigInteger
     * address  ------->BigInteger 16进制
     * uint256[]  ---------->BigInteger[]
     * @return
     */
    public static String getFunctionBinCode(Function function, Object... args) {
        if(function == null) {
            throw new IllegalArgumentException("function is null");
        }
        if(args.length > 0){
            return new String(Hex.encode(function.encodeCallWithArgs(args).array()));
        } else {
            return function.selectorHex();
        }
    }


    public static String getFunctionBinCodeWithMSwap(MSwapUtil.FunctionEnum fe, Object... args) {
        return new String(Hex.encode(getFunctionWithMSwap(fe).encodeCallWithArgs(args).array()));
    }

    public static Function getFunctionWithMSwap(MSwapUtil.FunctionEnum function) {
        return MSwapUtil.functionMap.get(function.name());
    }

    public static String getContractBinCode(String contractName) {
        String name = contractName.toLowerCase();
        if(!contractFileMap.containsKey(name)) {
            throw new IllegalArgumentException("contract name is not exist ");
        }
        String filePath =ContractUtil.class.getClassLoader().getResource("contract/"+contractFileMap.get(name)).getPath();
        RandomAccessFile raf = null;
        byte[] bytes = new byte[0];
        try {
            raf = new RandomAccessFile(filePath, "r");
            bytes  = new byte[(int) raf.length()];
            int i = raf.read(bytes);
//      int i=0, b;
//      while((b=raf.read())!=-1){
//          bytes[i++]=(byte)b;
//      }
            try {
                raf.close();
            } catch (IOException e) {
                log.error("RandomAccessFile close error",e);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("contract file not found: filePath="+filePath);
        }

        return new String(bytes);
    }


    public static String getContractBinCodeMSwap() {
        //暂不考虑并发
        if(binCodeMSwap == null || binCodeMSwap.equals(""))
            binCodeMSwap = ContractUtil.getContractBinCode("MSwap");
        return binCodeMSwap;
    }


    public static String getFunctionParamsCode(Function f,Object... args){
        Tuple tuple = new Tuple(args);
        return new String(Hex.encode(f.getParamTypes().encode(tuple).array()));
    }

    public static void main(String[] args) {
        log.info("getContractBinCodeMSwap==>\n{}",ContractUtil.getContractBinCodeMSwap());
    }

}
