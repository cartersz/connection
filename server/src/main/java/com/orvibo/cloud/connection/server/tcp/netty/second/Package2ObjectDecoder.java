package com.orvibo.cloud.connection.server.tcp.netty.second;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.server.constants.Constants;
import com.orvibo.cloud.connection.utils.AESCodec;
import com.orvibo.cloud.connection.utils.ByteUtil;
import com.orvibo.cloud.connection.utils.CRCUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunlin on 2017/11/8.
 */
public class Package2ObjectDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger logger = LoggerFactory.getLogger(Package2ObjectDecoder.class);

    public Package2ObjectDecoder() {
        super(Integer.MAX_VALUE, 2, 2, -4, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        logger.info("Package2ObjectDecoder decode ByteBuf...");
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }

        try {
            return parseBuffer(ctx, frame);
        } finally {
            frame.release();
        }
    }

    private Object parseBuffer(ChannelHandlerContext ctx, ByteBuf in) {
        logger.info("Package2ObjectDecoder parseBuffer...");
        if (!isOurPackage(in)) {
            logger.error("received package is not belong to us. close connection.");
            ctx.close();
            return null;
        }
        String clientIp = ctx.channel().remoteAddress().toString();
        CommandPackage cp =  new CommandPackage();
        //length
        int length = in.readShort();
        cp.setLength(length);
        logger.debug("clientIp = {}, package length = {}", clientIp, length);

        //protocol type
        String protocolType = ((char)in.readByte())+""+ (char)in.readByte();
        cp.setProtocolType(protocolType);

        //crc
        byte[] crcBytes = new byte[4];
        in.readBytes(crcBytes, 0, 4);
        String crcString = Hex.encodeHexString(crcBytes).toUpperCase();
        cp.setCrc(crcString);

        //sessionId
        byte[] sessionBytes = new byte[32];
        in.readBytes(sessionBytes, 0, 32);
        try {
            cp.setSessionID(parseByte2SessionID(sessionBytes));
        } catch (Exception e) {
            logger.error("parse bytes to sessionId failed. clientIp = {}", clientIp, e);
            return null;
        }

        //payload
        byte[] payloadBytes = new byte[length - 2 - 2 - 2 - 4 - 32];
        in.readBytes(payloadBytes);
        //先检查CRC
        if (!CRCUtil.checkCRC(cp.getCrc(), payloadBytes)){
            logger.error("payload crc check failed. clientIp = {}, crc=>{}, payloadBytes={}", clientIp, cp.getCrc(), payloadBytes);
            return null;
        }
        try {
            String payload = parsePayload(getKey(cp.getSessionID()), payloadBytes);
            cp.setPayload(payload);
        }catch (Exception e) {
            logger.error("parse payload to string failed. clientIp = {}", clientIp, e);
            return null;
        }
        return cp;
    }

    private boolean isOurPackage(ByteBuf in) {
        if (Constants.HEAD[0] == in.readByte() && Constants.HEAD[1] == in.readByte()) {
            return true;
        }
        return false;
    }

    private static String parseByte2SessionID(byte[] b) throws Exception{
        return ByteUtil.bytesToString(b);
    }

    private static String parsePayload(String key, byte[] payloadBytes) throws Exception{
        if ("".equals(key)) {
            return new String(payloadBytes, Constants.CHARSET_UTF_8);
        }
        return AESCodec.decrypt2String(payloadBytes,key, Constants.CHARSET_UTF_8);
    }

    private static String getKey(String sessionId) {
        return Constants.PUBLIC_KEY;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("Package2ObjectDecoder throw exception, close the connection", cause);
        ctx.close();
    }

}
