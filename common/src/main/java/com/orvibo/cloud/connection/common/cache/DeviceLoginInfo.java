package com.orvibo.cloud.connection.common.cache;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

/**
 * Created by sunlin on 2017/9/20.
 */
@RedisHash("deviceLoginInfos")
public class DeviceLoginInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    private String deviceIp;

    @Indexed
    private String sessionID;

    private String loginServer;

    private String protocolType;

    private String key;

    private Long timestamp;

    private DeviceParameters deviceParameters;

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getLoginServer() {
        return loginServer;
    }

    public void setLoginServer(String loginServer) {
        this.loginServer = loginServer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public DeviceParameters getDeviceParameters() {
        return deviceParameters;
    }

    public void setDeviceParameters(DeviceParameters deviceParameters) {
        this.deviceParameters = deviceParameters;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    @Override
    public String toString() {
        return "DeviceLoginInfo{" +
                "deviceIp='" + deviceIp + '\'' +
                ", sessionID='" + sessionID + '\'' +
                ", loginServer='" + loginServer + '\'' +
                ", protocolType='" + protocolType + '\'' +
                ", key='" + key + '\'' +
                ", timestamp=" + timestamp +
                ", deviceParameters=" + deviceParameters +
                '}';
    }
}

