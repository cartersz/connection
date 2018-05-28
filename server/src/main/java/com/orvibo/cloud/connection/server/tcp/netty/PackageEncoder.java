package com.orvibo.cloud.connection.server.tcp.netty;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.server.constants.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunlin on 2017/6/27.
 */
//@Sharable
public class PackageEncoder extends MessageToByteEncoder<CommandPackage> {

    private static final Logger logger = LoggerFactory.getLogger(PackageEncoder.class);

    protected void encode(ChannelHandlerContext channelHandlerContext, CommandPackage commandPackage, ByteBuf byteBuf) throws Exception {
        logger.info("PackageEncoder start encode command package....");
        String key = getKey(commandPackage.getSessionID());
        ByteBuf bb = CommandParser.parseCommand2ByteBuf(commandPackage, key);
        if (bb == null) {
            logger.info("command package object is null");
            return;
        }
        byteBuf.writeBytes(bb);
        logger.info("byteBuf.readableBytes() = {}", byteBuf.readableBytes());
        logger.info("PackageEncoder finish encode command package....");
    }

    private String getKey(String sessionID) {
        return Constants.PUBLIC_KEY;
    }
}
