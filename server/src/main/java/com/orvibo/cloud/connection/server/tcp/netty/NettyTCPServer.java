package com.orvibo.cloud.connection.server.tcp.netty;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.server.Server;
import com.orvibo.cloud.connection.utils.IPUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by sunlin on 2017/6/27.
 */
public class NettyTCPServer implements Server{

    private static final int OPTION_SO_BACKLOG_VALUE = 1024;

    private static final int READER_IDLE_TIME_SECONDS = 60 * 7;
    private static final int WRITER_IDLE_TIME_SECONDS = 60 * 7;
    private static final int ALL_IDLE_TIME_SECONDS = 0;

    private static final int SERVER_PORT = 10010;

    private static final Logger logger = LoggerFactory.getLogger(NettyTCPServer.class);

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private PackageInboundHandler packageInboundHandler;

    private ConnectionManager connectionManager;

    public static void main(String[] args) {
        new NettyTCPServer().start();
    }

    public void start() {
        logger.info("Starting TCP Server...");
        try {
            IPUtil.initialLocalServerIp();
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(READER_IDLE_TIME_SECONDS, WRITER_IDLE_TIME_SECONDS, ALL_IDLE_TIME_SECONDS));
                            ch.pipeline().addLast("PackageEncoder", new PackageEncoder());
                            ch.pipeline().addLast("PackageDecoder", new PackageDecoder());
                            ch.pipeline().addLast("PackageInboundHandler", packageInboundHandler);
                        }
                    }).option(ChannelOption.SO_BACKLOG, OPTION_SO_BACKLOG_VALUE)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(SERVER_PORT).syncUninterruptibly();
            logger.info("start TCP server {} successfully on port {}", IPUtil.getLocalSeverIp(), SERVER_PORT);
        }catch (UnknownHostException e) {
            logger.error("start tcp server failed, can not get local server ip.", e);
        }catch (Exception e){
            logger.error("start TCP server {} failed on port {}. ", IPUtil.getLocalSeverIp(), SERVER_PORT, e);
            Future<?> future = bossGroup.shutdownGracefully();
            future.syncUninterruptibly();
            future = workerGroup.shutdownGracefully();
            future.syncUninterruptibly();
        }

    }

    public void stop() {
        logger.info("Stopping TCP Server on port {}...", SERVER_PORT);
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public void sendCommand(String deviceIp, CommandPackage commandPackage) {
        ChannelHandlerContext context = connectionManager.getConnection(deviceIp);
        if (context == null) {
            logger.error("device[{}] channel context does not cache", deviceIp);
            return;
        }
        context.writeAndFlush(commandPackage);
    }

    public PackageInboundHandler getPackageInboundHandler() {
        return packageInboundHandler;
    }

    public void setPackageInboundHandler(PackageInboundHandler packageInboundHandler) {
        this.packageInboundHandler = packageInboundHandler;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
