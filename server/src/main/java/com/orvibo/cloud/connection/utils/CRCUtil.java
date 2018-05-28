package com.orvibo.cloud.connection.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.zip.CRC32;

/**
 * Created by sunlin on 2017/6/22.
 */
public class CRCUtil {

    private static final Logger logger = LoggerFactory.getLogger(CRCUtil.class);

    public static boolean checkCRC(String crcString, byte[] data) {
        if (StringUtils.isEmpty(crcString) || data.length < 2) {
            return false;
        }

        String payloadCRC32 = null;
        try {
            payloadCRC32 = getCRC32String(data);
            logger.info("when check CRC, the passed crc string is {}, the calculated crc string is {}, payload byte = {}", crcString, payloadCRC32, data);
        } catch (Exception e) {
            logger.error("check CRC failed, because get CRC 32 string failed. param crcString={}, data={}", crcString, data, e);
            return false;
        }
        if (crcString.equals(payloadCRC32)) {
            return true;
        }
        return false;
    }

    public static byte[] getCRC32Byte(byte[] bytes) throws Exception{
        logger.info("old bytes=> {}", bytes);
        String hexString = getCRC32String(bytes);
        byte[] hexBytes = ByteUtil.hexStringToBytes(hexString);
        logger.info("crc byte => {}", hexBytes);
        return hexBytes;
    }

    public static String getCRC32String(byte[] bytes) throws Exception {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        String str = Long.toHexString(crc32.getValue()).toUpperCase();
        return getCRCString(str);
    }

// 不抛出异常
//    public static String getCRC32(byte[] b) {
//        try {
//            CRC32 crc32 = new CRC32();
//            crc32.update(b);
//            String str = Long.toHexString(crc32.getValue()).toUpperCase();
//            return getCRCString(str);
//        } catch (Exception e) {
//            return "";
//        }
//
//    }

    private static String getCRCString(String str) {
        int len = str.length();
        if (len == 1) {
            str = "0000000" + str;
        }
        if (len == 2) {
            str = "000000" + str;
        }
        if (len == 3) {
            str = "00000" + str;
        }
        if (len == 4) {
            str = "0000" + str;
        }
        if (len == 5) {
            str = "000" + str;
        }
        if (len == 6) {
            str = "00" + str;
        }
        if (len == 7) {
            str = "0" + str;
        }
        return str;
    }
}
