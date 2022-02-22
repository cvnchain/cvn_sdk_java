package org.brewchain.core.crypto.cwv.util;

import com.google.protobuf.ByteString;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BytesHelper {


    public static byte[] appendByte(byte[] bytes, byte b) {
        byte[] result = Arrays.copyOf(bytes, bytes.length + 1);
        result[result.length - 1] = b;
        return result;
    }

    public static byte[] appendBytes(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
    

    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        if (b == null)
            return null;
        byte[] bytes = new byte[numBytes];
        byte[] biBytes = b.toByteArray();
        int start = (biBytes.length == numBytes + 1) ? 1 : 0;
        int length = Math.min(biBytes.length, numBytes);
        System.arraycopy(biBytes, start, bytes, numBytes - length, length);
        return bytes;
    }

    public static byte[] bigIntegerToBytes(BigInteger value) {
        if (value == null)
            return null;

        byte[] data = value.toByteArray();

        if (data.length != 1 && data[0] == 0) {
            byte[] tmp = new byte[data.length - 1];
            System.arraycopy(data, 1, tmp, 0, tmp.length);
            data = tmp;
        }
        return data;
    }

    public static String oneByteToHexString(byte value) {
        String retVal = Integer.toString(value & 0xFF, 16);
        if (retVal.length() == 1)
            retVal = "0" + retVal;
        return retVal;
    }

    public static String toHexString(byte[] data) {
        return data == null ? "" : Hex.toHexString(data);
    }
    
    public static byte[] hexStringToBytes(String data) {
        if (data == null)
            return new byte[0];
        if (data.startsWith("0x"))
            data = data.substring(2);
        if (data.length() % 2 != 0)
            data = "0" + data;
        return Hex.decode(data);
    }

    public static byte[] intToBytes(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }
    public static int byteArrayToInt(byte[] b) {
        if (b == null || b.length == 0)
            return 0;
        return new BigInteger(1, b).intValue();
    }


    /**
     * @param arrays
     *            - arrays to merge
     * @return - merged array
     */
    public static byte[] merge(byte[]... arrays) {
        int arrCount = 0;
        int count = 0;
        for (byte[] array : arrays) {
            arrCount++;
            count += array.length;
        }

        // Create new array and copy all array contents
        byte[] mergedArray = new byte[count];
        int start = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, mergedArray, start, array.length);
            start += array.length;
        }
        return mergedArray;
    }

    public static ByteString bigIntegerToByteString(BigInteger value) {
        return ByteString.copyFrom(bigIntegerToBytes(value));
    }

    /**
     * 16进制字符串转BigDecimal
     * @param hexValue 16进制数据(包含0x)
     * @param divideCount 扣减精度 18
     * @param resultPrecision 结果精度设定
     * @return
     */
    public static BigDecimal hexStr2BigDecimal(String hexValue, int divideCount, int resultPrecision) {
        hexValue = hexValue.startsWith("0x")? hexValue.substring(2) : hexValue;
        BigDecimal bdValue = new BigDecimal(new BigInteger(hexValue,16).toString());
        BigDecimal oldValue = bdValue.divide(new BigDecimal(Math.pow(10D, divideCount)));
        return oldValue.divide(new BigDecimal(1), resultPrecision, BigDecimal.ROUND_DOWN);
    }

    /**
     * BigDecimal转16进制字符串（包含0x）
     * @param bigDecimal
     * @param multiplyCount 增加精度 18
     * @return
     */
    public static String bigDecimal2HexStr(BigDecimal bigDecimal, int multiplyCount) {
        bigDecimal = bigDecimal.multiply(BigDecimal.valueOf(Math.pow(10D, multiplyCount)));
        return bigDecimal.toBigInteger().toString(16);
    }


}
