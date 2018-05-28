package com.orvibo.cloud.connection.utils;

import com.orvibo.cloud.connection.server.constants.Constants;

import java.nio.ByteBuffer;

/**
 * Created by sunlin on 2017/6/24.
 */
public class ByteUtil {
    /**
     * 将int型的i转换成长度为len的byte数组，高字节在前
     *
     * @param i
     * @param len
     * @return
     */
    public static byte[] itob(int i, int len) {
        byte[] buf = new byte[len];
        for (int j = 0; j < len; j++) {
            buf[len - j - 1] = (byte) (i >> 8 * j);
        }
        return buf;
    }

    /**
     * 将int型的i转换成长度为len的byte数组，低字节在前
     *
     * @param i
     * @param len
     * @return
     */
    public static byte[] itoReverseb(int i, int len) {
        byte[] buf = new byte[len];
        for (int j = 0; j < len; j++) {
            buf[j] = (byte) (i >> 8 * j);
        }
        return buf;
    }

    /**
     * 将十六进制字符串转换成byte[]
     *
     * @param hexString
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c
     *                char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 将字符串name转换成len个字节长度的byte，name的字节长度不足len字节的话，右边补空格。name的长度超过len字节的话，
     * 支取前面len字节
     *
     * @param name
     * @param len
     * @return
     */
    public static byte[] StringToBytes(String name, int len) {
        ByteBuffer buf = ByteBuffer.allocate(len);
        byte[] b = null;
        try {
            b = name.getBytes(Constants.CHARSET_UTF_8);
        } catch (Exception e) {

            b = name.getBytes();
        }
        if (b.length <= len) {
            buf.put(b);

            for (int i = b.length; i < len; i++) {
                buf.put((byte) 0x20);
            }
        } else {
            buf.put(b, 0, len);
        }

        byte[] out = new byte[len];
        buf.flip();
        buf.get(out);
        return out;
    }

    /**
     * 将二进制流转换成字符串
     * @param b
     * @return
     */
    public static String bytesToString(byte[] b) throws Exception{
        return new String(b, Constants.CHARSET_UTF_8).trim();
    }

    public static int parseByte2Len(byte b2, byte b3) {
        return (b2 << 8 & 0x0000FF00) | (b3 & 0x000000FF);
    }

}
