package io.github.smarthawkeye.core.celldb.util;

import cn.hutool.core.util.ObjectUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
public class YaDataUtil {
    /**
     * 将byte类型的arr转换成float
     */
    public static List<Float> byteArrayToFloatList(byte[] bytes) {
        List<Float> d = new ArrayList<>(bytes.length / 8);
        byte[] doubleBuffer = new byte[4];
        for (int j = 0; j < bytes.length; j += 4) {
            System.arraycopy(bytes, j, doubleBuffer, 0, doubleBuffer.length);
            d.add(bytes2Float(doubleBuffer));
        }
        return d;
    }

    /**
     * 将byte类型的arr转换成double
     * @param arr
     */
    public static List<Double> byteArrayToDoubleList(byte[] arr) {
        List<Double> d = new ArrayList<>(arr.length / 8);
        byte[] doubleBuffer = new byte[8];
        for (int j = 0; j < arr.length; j += 8) {
            System.arraycopy(arr, j, doubleBuffer, 0, doubleBuffer.length);
            d.add(bytes2Double(doubleBuffer));
        }
        return d;
    }

    /**
     * 将byte数组数据转换成int
     * @param arr
     */
    public static int bytes2Int(byte[] arr) {
        int accum = 0;
        accum = accum | (arr[0] & 0xff) << 0;
        accum = accum | (arr[1] & 0xff) << 8;
        accum = accum | (arr[2] & 0xff) << 16;
        accum = accum | (arr[3] & 0xff) << 24;
        return accum;
    }
    public static long bytes2Long(byte[] arr) {
        long accum = 0;
        for (int i = 0; i < 8; i++) {
            accum <<= 8;
            accum|= (arr[7-i] & 0xff);
        }
        return accum;
    }
    /**
     * 将byte数组数据转换成float
     * @param arr
     */
    public static float bytes2Float(byte[] arr) {
        return Float.intBitsToFloat(bytes2Int(arr));
    }

    /**
     * 将byte转换成double
     * @param arr
     */
    public static double bytes2Double(byte[] arr) {
        return Double.longBitsToDouble(bytes2Long(arr));
    }
    /**
     * 将double转换成byte
     *
     * @param dval
     */
    public static byte[] double2bytes(Double dval) {
        return long2bytes(Double.doubleToRawLongBits(dval));
    }

    /**
     * 将float转换成byte
     *
     * @param fval
     */
    public static byte[] float2bytes(float fval) {
        return int2bytes(Float.floatToRawIntBits(fval));
    }

    /**
     * 将long转换成byte
     * @param lval
     */
    public static byte[] long2bytes(long lval) {
        byte[] arr = new byte[8];
        arr[0] = (byte) ((lval >> (8 * 0)) & 0xff);
        arr[1] = (byte) ((lval >> (8 * 1)) & 0xff);
        arr[2] = (byte) ((lval >> (8 * 2)) & 0xff);
        arr[3] = (byte) ((lval >> (8 * 3)) & 0xff);
        arr[4] = (byte) ((lval >> (8 * 4)) & 0xff);
        arr[5] = (byte) ((lval >> (8 * 5)) & 0xff);
        arr[6] = (byte) ((lval >> (8 * 6)) & 0xff);
        arr[7] = (byte) ((lval >> (8 * 7)) & 0xff);
        return arr;
    }

    /**
     * 将int转换成byte
     *
     * @param ival
     */
    public static byte[] int2bytes(int ival) {
        byte[] arr = new byte[4];
        arr[0] = (byte) ((ival >> (8 * 0)) & 0xff);
        arr[1] = (byte) ((ival >> (8 * 1)) & 0xff);
        arr[2] = (byte) ((ival >> (8 * 2)) & 0xff);
        arr[3] = (byte) ((ival >> (8 * 3)) & 0xff);
        return arr;
    }

    /**
     * byte数组转String
     * @param bytes
     */
    public static String byte2String(byte[] bytes) {
        byte b = '\0';
        int idx = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == b) {
                idx = i;
                break;
            }
        }
        return new String(Arrays.copyOfRange(bytes, 0, idx));
    }

    /**
     * int[] 转 byte[]
     * @param values
     */
    public static byte[] ints2Bytes(int[] values){
        byte[] bytes = new byte[values.length * 4];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(int2bytes(values[i]),0,bytes,i * 4,4);
        }
        return bytes;
    }
    /**
     * float[] 转 byte[]
     * @param values
     */
    public static byte[] floats2Bytes(float[] values){
        byte[] bytes = new byte[values.length * 4];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(float2bytes(values[i]),0,bytes,i * 4,4);
        }
        return bytes;
    }

    /**
     * long[] 转 byte[]
     * @param values
     */
    public static byte[] longs2Bytes(long[] values){
        byte[] bytes = new byte[values.length * 8];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(long2bytes(values[i]),0,bytes,i * 8,8);
        }
        return bytes;
    }

    /**
     * double[] 转 byte[]
     * @param values
     */
    public static byte[] doubles2Bytes(double[] values){
        byte[] bytes = new byte[values.length * 8];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(double2bytes(values[i]),0,bytes,i * 8,8);
        }
        return bytes;
    }
    /**
     * String转byte数组
     * @param str
     */
    public static byte[] string2Byte(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 字符串拼接
     *
     * @param character
     * @param strs
     */
    public static String stringConcat(String character, String... strs) {
        StringBuilder sb = new StringBuilder();
        if (ObjectUtil.isNotNull(strs) && strs.length > 0) {
            for (int i = 0; i < strs.length; i++) {
                sb.append(strs[i]).append(character);
            }
        }
        return sb.toString();
    }

}
