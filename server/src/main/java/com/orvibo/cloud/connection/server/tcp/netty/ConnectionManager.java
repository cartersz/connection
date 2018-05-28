package com.orvibo.cloud.connection.server.tcp.netty;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sunlin on 2017/7/26.
 */
public class ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private ConcurrentHashMap<String, DeviceConnection> connections = new ConcurrentHashMap<String, DeviceConnection>();

    public void putConnection(String ip, ChannelHandlerContext connection) {

        if (StringUtils.isEmpty(ip) || connection == null) {
            throw new IllegalArgumentException("ip or connection should not be empty");
        }

        DeviceConnection dc = new DeviceConnection();
        dc.setContext(connection);
        connections.put(ip, dc);
    }

    public void setKeyForConnection(String ip, String key){
        if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("ip or key should not be empty");
        }
        DeviceConnection dc = connections.get(ip);
        dc.setKey(key);
    }

    public ChannelHandlerContext getConnection(String ip) {
        if (ip == null) {
            logger.error("ip should not be null");
            return null;
        }
        DeviceConnection cp = connections.get(ip);
        if (cp == null) {
            logger.info("the device {} have no connection.", ip);
            return null;
        }
        return connections.get(ip).getContext();
    }

    public void handleReadIdle(String deviceIp) {
        connections.remove(deviceIp);
        notifyDeviceLogout(deviceIp);
    }

    public void handleWriteIdle(String deviceIp) {
        connections.remove(deviceIp);
        notifyDeviceLogout(deviceIp);
    }

    /**
     * TODO
     * @param deviceIp
     */
    private void notifyDeviceLogout(String deviceIp) {

    }

}
