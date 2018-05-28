package com.orvibo.cloud.connection.server.tcp.netty;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.common.protocol.DTOBuilder;
import com.orvibo.cloud.connection.server.mq.MQSender;
import com.orvibo.cloud.connection.utils.IPUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunlin on 2017/6/27.
 */
@Sharable
public class PackageInboundHandler extends SimpleChannelInboundHandler<CommandPackage> {

    private MQSender mqSender;

    private ConnectionManager connectionManager;

    private static final Logger logger = LoggerFactory.getLogger(PackageInboundHandler.class);

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CommandPackage commandPackage) throws Exception {
        logger.info("Server Receive CommandPackage => {}", commandPackage);
        logger.info("Send Response Object to MQ");
        mqSender.sendBaseCommandDTO(DTOBuilder.buildBaseCommandDTO(IPUtil.getLocalSeverIp(), getClientIp(channelHandlerContext), commandPackage));
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
        String ip = getClientIp(ctx);
        logger.info("Device IP : => {}", ip);
        connectionManager.putConnection(ip, ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

         if (evt instanceof IdleStateEvent) {
             IdleStateEvent event = (IdleStateEvent) evt;
             String ip = ctx.channel().remoteAddress().toString();
             switch (event.state()) {
                 case READER_IDLE:
                     connectionManager.handleReadIdle(ip);
                 case WRITER_IDLE:
                     connectionManager.handleWriteIdle(ip);
                 case ALL_IDLE:
                     logger.info("{} connection all idle.", ip);
             }
         }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("PackageInboundHandler catch a exception", cause);
    }

    public MQSender getMqSender() {
        return mqSender;
    }

    public void setMqSender(MQSender mqSender) {
        this.mqSender = mqSender;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private String getClientIp(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress().toString().split(":")[0];
    }
}
