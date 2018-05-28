package com.orvibo.cloud.connection.server.tcp.netty.second;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.server.tcp.netty.ConnectionManager;
import com.orvibo.cloud.connection.server.tcp.netty.PackageEncoder;
import com.orvibo.cloud.connection.server.tcp.netty.PackageInboundHandler;
import com.orvibo.cloud.connection.server.tcp.netty.TestCloseHandler;
import com.orvibo.cloud.connection.utils.IPUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunlin on 2017/11/8.
 */
public class NettyTCPServer {


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

    //这个方法不加try catch是为了让netty启动出错后阻止整个spring容器的初始化
    public void start() throws Exception{
        int port = 10010;
        logger.info("Starting TCP Server...");
        IPUtil.initialLocalServerIp();
        TestCloseHandler handler = new TestCloseHandler();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("loggingHandler", new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(READER_IDLE_TIME_SECONDS, WRITER_IDLE_TIME_SECONDS, ALL_IDLE_TIME_SECONDS));
                        ch.pipeline().addLast("PackageEncoder", new PackageEncoder());
                        ch.pipeline().addLast("Package2ObjectDecoder", new Package2ObjectDecoder());
//                        ch.pipeline().addLast("PackageInboundHandler", packageInboundHandler);
                        ch.pipeline().addLast("PackageInboundHandler", handler);
                    }
                }).option(ChannelOption.SO_BACKLOG, OPTION_SO_BACKLOG_VALUE)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        b.bind(port).syncUninterruptibly();
        logger.info("start TCP server {} successfully on port {}", IPUtil.getLocalSeverIp(), port);
    }

//    public void start() {
//        int port = 10010;
//        logger.info("Starting TCP Server...");
//        try {
//            IPUtil.initialLocalServerIp();
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        public void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(READER_IDLE_TIME_SECONDS, WRITER_IDLE_TIME_SECONDS, ALL_IDLE_TIME_SECONDS));
//                            ch.pipeline().addLast("PackageEncoder", new PackageEncoder());
//                            ch.pipeline().addLast("Package2ObjectDecoder", new Package2ObjectDecoder());
//                            ch.pipeline().addLast("PackageInboundHandler", packageInboundHandler);
//                        }
//                    }).option(ChannelOption.SO_BACKLOG, OPTION_SO_BACKLOG_VALUE)
//                    .childOption(ChannelOption.SO_KEEPALIVE, true);
//            ChannelFuture f = b.bind(port).syncUninterruptibly();
//            logger.info("start TCP server {} successfully on port {}", IPUtil.getLocalSeverIp(), port);
//
//        } catch (UnknownHostException e) {
//            logger.error("start tcp server failed, can not get local server ip.", e);
//        } catch (Exception e){
//            logger.error("start TCP server {} failed on port {}. ", IPUtil.getLocalSeverIp(), port, e);
//            stop();
//        }
//    }

    public void stop() {
        logger.info("Stopping TCP Server on port {}...", SERVER_PORT);
        Future<?> future = bossGroup.shutdownGracefully();
        future.syncUninterruptibly();
        future = workerGroup.shutdownGracefully();
        future.syncUninterruptibly();

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
