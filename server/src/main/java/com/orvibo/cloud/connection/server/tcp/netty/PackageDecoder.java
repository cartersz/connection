package com.orvibo.cloud.connection.server.tcp.netty;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.server.constants.Constants;
import com.orvibo.cloud.connection.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by sunlin on 2017/6/27.
 */
//@Sharable
public class PackageDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(PackageDecoder.class);

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        logger.info("PackageDecoder start decode bytebuf...");
        logger.info("byteBuf.readableBytes() = {}", byteBuf.readableBytes());
        try {
            if(byteBuf.readableBytes() < 4){
                logger.info("byteBuf data  readable bytes length = 0 ");
                return;
            }
            byteBuf.markReaderIndex();
            if (!isStartPosition(byteBuf)) {
                channelHandlerContext.close();
                logger.info("bytebuf can not found hd field");
                return;
            }
            byte[] lengthBytes = new byte[2];
            byteBuf.readBytes(lengthBytes);
            //数据包总长度
            int length = ByteUtil.parseByte2Len(lengthBytes[0], lengthBytes[1]);
            logger.info("receive bytebuf length = {}", length);
            if (length - 4 < 0) {
                channelHandlerContext.close();
                logger.info("bytebuf data length - 4 < 0");
                return;
            }

            //数据包长度不够
            if (byteBuf.readableBytes() < length - 4) {
                //还原读索引位置
                byteBuf.resetReaderIndex();
                logger.info("bytebuf length less than package length ");
                return;
            }

            //读取数，协议里定义的len是整个数据包的长度，而程序已经读到第四个字节才取出长度所以需要减去4
            byte[] data = new byte[length - 4];
            byteBuf.readBytes(data);
            CommandPackage commandPackage = CommandParser.parseByte2CommandPackage(data, length);
            logger.info("byteBuf.readableBytes() = {}", byteBuf.readableBytes());
            if (commandPackage == null){
                logger.error("ByteBuf data parse to CommandPackage object is null. param bytebuf = {}", data);
                return;
            }
//            commandPackage.setDeviceIp(channelHandlerContext.channel().remoteAddress().toString());
            list.add(commandPackage);
        } catch (Exception e) {
            logger.error("parse bytebuf failed. ", e);
            return;
        }
        logger.info("PackageDecoder finish decode bytebuf successfully。");
    }

    private boolean isStartPosition(ByteBuf byteBuf) {
        byte[] hd = new byte[2];
        byteBuf.readBytes(hd);
        if(hd[0] != Constants.HEAD[0] || hd[1] != Constants.HEAD[1]){
            logger.error("find wrong hd = {}", (char) hd[0] + "" + hd[1]);
            return false;
        }
        return true;
    }

}
