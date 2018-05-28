package com.orvibo.cloud.connection.server.tcp.netty;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunlin on 2017/12/21.
 */
@ChannelHandler.Sharable
public class TestCloseHandler extends SimpleChannelInboundHandler<CommandPackage> {

    private static final Logger logger = LoggerFactory.getLogger(TestCloseHandler.class);

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CommandPackage commandPackage) throws Exception {
        logger.info("channel read  and then close context");
        channelHandlerContext.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("Server channel--register");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("Server channel--unregistered");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Server channel--inactive");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Server channel--active");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String ip = ctx.channel().remoteAddress().toString();
            switch (event.state()) {
                case READER_IDLE:
                    logger.info("Server reader idle");
                case WRITER_IDLE:
                    logger.info("Server write idle");
                case ALL_IDLE:
                    logger.info("{} connection all idle.", ip);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("PackageInboundHandler catch a exception", cause);
    }
}
