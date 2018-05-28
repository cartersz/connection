package com.orvibo.cloud.connection.server.tcp.netty;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.server.constants.Constants;
import com.orvibo.cloud.connection.utils.AESCodec;
import com.orvibo.cloud.connection.utils.ByteUtil;
import com.orvibo.cloud.connection.utils.CRCUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunlin on 2017/6/19.
 */
public class CommandParser {

    /**
     * 前两个字节是固定填写hd的ASCII码
     */
    private static final int Head[] = {0x68, 0x64};

    /**
     * Head长度
     */
    private static final int HEAD_LENGTH = 2;

    /**
     * Len长度
     */
    private static final int LEN_LENGTH = 2;

    /**
     * Protocol type 长度
     */
    private static final int PROTOCOL_TYPE_LENGTH = 2;

    /**
     * CRC 长度
     */
    private static final int CRC_LENGTH = 4;

    /**
     * SessionID 长度
     */
    private static final int SESSIONID_LENGTH = 32;

    /**
     * 数据包Header部分总长度
     */
    private static final int HEADER_TOTAL_LENGTH = HEAD_LENGTH + LEN_LENGTH + PROTOCOL_TYPE_LENGTH + CRC_LENGTH + SESSIONID_LENGTH;

    private static final Logger logger = LoggerFactory.getLogger(CommandParser.class);

    /**
     * 解析数据包
     * @param data   Note：data封装的数据从Protocol Type数据开始的，没有包含Head和Len字段数据
     * @param length 整个数据包的总长度
     * @return
     */
    public static CommandPackage parseByte2CommandPackage(byte[] data, int length) {
        if (data == null || data.length == 0) {
            return null;
        }
        CommandPackage commandPackage = new CommandPackage();
        commandPackage.setLength(length);
        logger.info("the total data length is {}", length);

        int readerIndex = 0;
        commandPackage.setProtocolType(parseByte2ProtocolType(data[readerIndex], data[readerIndex + PROTOCOL_TYPE_LENGTH - 1]));
        logger.info("readerIndex=>{}, protocol type is {}", readerIndex, commandPackage.getProtocolType());

        byte[] crcBytes = new byte[CRC_LENGTH];
        readerIndex = readerIndex + PROTOCOL_TYPE_LENGTH;
        System.arraycopy(data, readerIndex, crcBytes, 0, CRC_LENGTH);
        commandPackage.setCrc(Hex.encodeHexString(crcBytes).toUpperCase());
        logger.info("readerIndex=>{}, crc string is {}", readerIndex, commandPackage.getCrc());

        byte[] sessionIDBytes = new byte[SESSIONID_LENGTH];
        readerIndex = readerIndex + CRC_LENGTH;
        System.arraycopy(data, readerIndex, sessionIDBytes, 0, SESSIONID_LENGTH);
        try {
            String sessionID = parseByte2SessionID(sessionIDBytes);
            commandPackage.setSessionID(sessionID);
            logger.info("readerIndex=>{}, the session ID is {}", readerIndex, sessionID);
        } catch (Exception e) {
            logger.error("parse bytes to session id string failed.", e);
            return null;
        }

        byte[] payloadBytes = new byte[length - HEADER_TOTAL_LENGTH];
        readerIndex = readerIndex + SESSIONID_LENGTH;
        System.arraycopy(data, readerIndex, payloadBytes, 0, payloadBytes.length);
        logger.info("readerIndex=>{}, payloadBytes.length=>{}, parse load bytes", readerIndex, payloadBytes.length);
        //先检查CRC
        if (!CRCUtil.checkCRC(commandPackage.getCrc(), payloadBytes)){
            logger.error("payload crc check failed. crc=>{}, payloadBytes={}", commandPackage.getCrc(), payloadBytes);
            return null;
        }
        try {
            String payload = parsePayload(getKey(commandPackage.getSessionID()), payloadBytes);
            commandPackage.setPayload(payload);
        }catch (Exception e) {
            logger.error("parse payload to string failed.", e);
            return null;
        }
        return commandPackage;
    }

    public static ByteBuf parseCommand2ByteBuf(CommandPackage commandPackage, String key) {
        try {
            byte[] payloadBytes = AESCodec.encrypt(commandPackage.getPayload(), key, Constants.CHARSET_UTF_8);
            int totalLength = HEADER_TOTAL_LENGTH + payloadBytes.length;
            commandPackage.setLength(totalLength);
            String payloadCRC = CRCUtil.getCRC32String(payloadBytes);
            logger.info("payload crc string => {}", payloadCRC);
            ByteBuf buf = Unpooled.buffer(totalLength);
            buf.writeBytes(commandPackage.getHead().getBytes());
            buf.writeBytes(ByteUtil.itob(totalLength, 2));
            buf.writeBytes(commandPackage.getProtocolType().getBytes());
            buf.writeBytes(ByteUtil.hexStringToBytes(payloadCRC));
            buf.writeBytes(ByteUtil.StringToBytes(commandPackage.getSessionID() == null ? "" : commandPackage.getSessionID(), 32));
            buf.writeBytes(payloadBytes);
            logger.info("payload.length=> {}, send payload byte is {}, bytebuf.length=>{}", payloadBytes.length, payloadBytes, buf.readableBytes());
            return buf;
        } catch (Exception e) {
            logger.error("parse command object to bytebuf failed. param commandPackage={}, key = {}", commandPackage, key, e);
        }
        return null;
    }

    public static String parsePayload(String key, byte[] payloadBytes) throws Exception{
        if ("".equals(key)) {
            return new String(payloadBytes, Constants.CHARSET_UTF_8);
        }
        return AESCodec.decrypt2String(payloadBytes,key, Constants.CHARSET_UTF_8);
    }

//    private static  String parseByte2Head(byte b0, byte b1) {
//        return (char) b0 + "" + (char) b1;
//    }

    private static int parseByte2Len(byte b2, byte b3) {
        return (b2 << 8 & 0x0000FF00) | (b3 & 0x000000FF);
    }

    private static String parseByte2ProtocolType(byte b4, byte b5) {

        return (char) b4 + "" + (char) b5;
    }

    private static String parseByte2SessionID(byte[] b) throws Exception{
        return ByteUtil.bytesToString(b);
    }

    private static String getKey(String sessionID) {
        return Constants.PUBLIC_KEY;
    }

//    public static CommandPackage parseBytebuf2Command(ByteBuf buf, String key) {
//        byte[] headBytes = new byte[HEAD_LENGTH];
//        byte[] lenBytes = new byte[LEN_LENGTH];
//        byte[] protocolBytes = new byte[PROTOCOL_TYPE_LENGTH];
//        byte[] crcBytes = new byte[CRC_LENGTH];
//        byte[] sessionIDBytes = new byte[SESSIONID_LENGTH];
//        byte[] payloadBytes = new byte[buf.readableBytes() - HEADER_TOTAL_LENGTH];
//
//        buf.readBytes(headBytes);
//        buf.readBytes(lenBytes);
//        buf.readBytes(protocolBytes);
//        buf.readBytes(crcBytes);
//        buf.readBytes(sessionIDBytes);
//        buf.readBytes(payloadBytes);
//
//        CommandPackage commandPackage = new CommandPackage();
//        commandPackage.setLength(parseByte2Len(lenBytes[0], lenBytes[1]));
//        commandPackage.setProtocolType(parseByte2ProtocolType(protocolBytes[0], protocolBytes[1]));
//
//        commandPackage.setCrc(Hex.encodeHexString(crcBytes).toUpperCase());
//        if (!CRCUtil.checkCRC(commandPackage.getCrc(), payloadBytes)){
//            logger.error("payload crc check failed. crc=>{}, payloadBytes={}", commandPackage.getCrc(), payloadBytes);
//            return null;
//        }
//        try {
//            String sessionID = parseByte2SessionID(sessionIDBytes);
//            commandPackage.setSessionID(sessionID);
//        } catch (Exception e) {
//            logger.error("parse bytes to session id string failed.", e);
//            return null;
//        }
//        try {
//            String payload = parsePayload(key, payloadBytes);
//            commandPackage.setPayload(payload);
//        }catch (Exception e) {
//            logger.error("parse payload to string failed.", e);
//            return null;
//        }
//        return commandPackage;
//    }

}
