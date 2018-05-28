package com.orvibo.cloud.connection.server.tcp.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by sunlin on 2017/9/22.
 */
public class DeviceConnection {
    /**
     * 通讯密钥
     */
    private String key;

    /**
     * 通讯连接
     */
    private ChannelHandlerContext context;

    /**
     * 上次发送数据的时间
     */
    private long lasTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    public long getLasTime() {
        return lasTime;
    }

    public void setLasTime(long lasTime) {
        this.lasTime = lasTime;
    }
}
