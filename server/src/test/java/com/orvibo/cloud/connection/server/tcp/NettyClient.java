package com.orvibo.cloud.connection.server.tcp;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.server.tcp.netty.PackageEncoder;
import com.orvibo.cloud.connection.server.tcp.netty.second.Package2ObjectDecoder;
import com.orvibo.cloud.connection.utils.IPUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunlin on 2017/6/28.
 */
public class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private Channel channel;

    public void connect(String host, int port) throws Exception{
        IPUtil.initialLocalServerIp();
        //配置客户端线程组
        EventLoopGroup group=new NioEventLoopGroup();
        //配置客户端启动辅助类
        Bootstrap b=new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("PackageEncoder", new PackageEncoder());
                        ch.pipeline().addLast("PackageDecoder", new Package2ObjectDecoder());
                        ch.pipeline().addLast("ClientHandler", new ClientPackageInboundHandler());
                    }
                });
        //发起异步服务器连接请求 同步等待成功
        channel = b.connect(host,port).syncUninterruptibly().channel();
        //制定本地ip和端口连接远程服务器
//        channel = b.connect(new InetSocketAddress(host, port), new InetSocketAddress(IPUtil.getLocalSeverIp(), 1000)).sync().channel();
    }

    public void sendMessage(CommandPackage commandPackage) {
        logger.info("Channel Send message.....");
        channel.writeAndFlush(commandPackage);
        logger.info("Channel Send message finished.");
    }

}
