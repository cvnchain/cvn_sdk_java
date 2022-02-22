package org.brewchain.sdk.util;

import lombok.extern.slf4j.Slf4j;
import org.brewchain.sdk.contract.abi.ContractJSONParser;
import org.brewchain.sdk.contract.abi.Function;
import org.brewchain.sdk.contract.abi.Tuple;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MSwapUtil {
    public static final String abi="[{\"constant\":true,\"inputs\":[],\"name\":\"token0\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"removeLiquid\",\"outputs\":[{\"name\":\"success\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"status\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"mancount\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"const_K\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"poolLiquidity\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"}],\"name\":\"liquidBalances\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"generateLockId\",\"outputs\":[{\"name\":\"lockId\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"requestCount\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"bytes32\"}],\"name\":\"changeReqs\",\"outputs\":[{\"name\":\"proposedNew\",\"type\":\"address\"},{\"name\":\"proposedClear\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"fee_ratio\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"poolBalance0\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"poolBalance1\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"value0\",\"type\":\"uint256\"},{\"name\":\"value1\",\"type\":\"uint256\"}],\"name\":\"addLiquid\",\"outputs\":[{\"name\":\"success\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"to\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transferLiquid\",\"outputs\":[{\"name\":\"success\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"value0\",\"type\":\"uint256\"},{\"name\":\"value1\",\"type\":\"uint256\"}],\"name\":\"initPool\",\"outputs\":[{\"name\":\"success\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"token1\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"value0\",\"type\":\"uint256\"},{\"name\":\"value1\",\"type\":\"uint256\"}],\"name\":\"swap\",\"outputs\":[{\"name\":\"success\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"addLiquidToken\",\"outputs\":[{\"name\":\"success\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"}],\"name\":\"managers\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_token0\",\"type\":\"address\"},{\"name\":\"_token1\",\"type\":\"address\"},{\"name\":\"_mans\",\"type\":\"address[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]";
    public static Map<String,Function> functionMap;
    public enum FunctionEnum{
        changeReqs,transferLiquid,addLiquid,addLiquidToken,const_K,fee_ratio,generateLockId,initPool,
        liquidBalances,managers,mancount,poolBalance0,poolBalance1,poolLiquidity,removeLiquid,requestCount,
        status,swap,token0,token1,construtor
    }
    static {
        try {
            functionMap = ContractJSONParser.parseFunctions(abi).stream().collect(Collectors.toMap(Function::getName, a -> a,(k1,k2)->k1));
            Function f = ContractJSONParser.parseFunction("{\"inputs\":[{\"name\":\"_token0\",\"type\":\"address\"},{\"name\":\"_token1\",\"type\":\"address\"},{\"name\":\"_mans\",\"type\":\"address[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}");
            functionMap.put("construtor",f);
        } catch (ParseException e) {
           log.error("functionMap init error", e);
        }
    }

    public static void main(String[] args) {
        log.info("MSwapUtil.functionMap.size()==>{}",MSwapUtil.functionMap.size());
        Function f = functionMap.get(FunctionEnum.construtor);
//        log.info("constructor={}",f.selectorHex());
        String code = new String(Hex.encode(f.encodeCallWithArgs(new BigInteger("0000000000000000000000000000000041414120",16),new BigInteger("0000000000000000000000000000000042424220", 16),new BigInteger[]{new BigInteger("fda8e7b96c92a9fd75ae77b87bf7fdb4cbaea0d5",16)}).array()));
        Tuple tuple = new Tuple(new BigInteger("0000000000000000000000000000000041414120",16),new BigInteger("0000000000000000000000000000000042424220", 16),new BigInteger[]{new BigInteger("fda8e7b96c92a9fd75ae77b87bf7fdb4cbaea0d5",16)});
        log.info("tuple.e={} .code={}",tuple.size(),code);
        code = new String(Hex.encode(f.getParamTypes().encode(tuple).array()));
        log.info("code={}",code);

    }

}
