package org.brewchain.sdk.util;

import java.math.BigDecimal;
import com.brewchain.sdk.crypto.KeyPairs;

public class AccountUtil {
    
    public static final BigDecimal step = new BigDecimal("1000000000000000000");


    /**
     * 金额入链前需要乘以10^18。
     * @param amount 原始金额
     * @return
     */
    public static String multy18(String amount){
        BigDecimal bd = new BigDecimal(amount);
        return bd.multiply(step).toBigInteger().toString(10);
    }

    /**
     * 金额返回用户前需要除以10^18。
     * @param amount 链上金额
     * @return
     */
    public static String div18(String amount){
        BigDecimal bd = new BigDecimal(amount);
        return bd.divide(step).toPlainString();
    }

    /**
     * 返回商户用户挂卖单的实际交易金额。是加上手续费的。
     * @param dealAmount  订单原始金额。
     * @param rate  手续费。千分之几。
     * @return  乘以 10^18 实际交易的金额
     */
    public static String getDealAmount(String dealAmount, int rate){
       return new BigDecimal(dealAmount).multiply(step).
                divide(new BigDecimal(1000)).multiply(new BigDecimal(1000+rate))
                .toBigInteger().toString(10);
    }

    public static String getDealAmountWithNoM18(String dealAmount, int rate){
        return new BigDecimal(dealAmount).
                divide(new BigDecimal(1000)).multiply(new BigDecimal(1000+rate))
                .toString();
    }

    public static String fixAddr(String addr){
        addr = "0000000000000000000000000000000000000000"+addr;
        return addr.substring(addr.length()-40);
    }

    public static void main(String[] args) {
//        String amount = "1000.00";
//        System.out.println(multy18(amount));
//        System.out.println(div18(multy18(amount)));
//
//        System.out.println(getDealAmount("500",5));
        
        String amount = "0.0000001";
        String ss = multy18(amount);
        System.out.println(ss);

        System.out.println(div18(ss));
        
    }

    public static String cvnFiler(String address){
        if(address.startsWith("0x") ) {
            address = address.substring(address.indexOf("0x") + 2);
        }
        if(address.startsWith(KeyPairs.ADDR_PRE)) {
            address = address.substring(address.indexOf(KeyPairs.ADDR_PRE) + KeyPairs.ADDR_PRE.length());
        }
        return address;
    }
}
