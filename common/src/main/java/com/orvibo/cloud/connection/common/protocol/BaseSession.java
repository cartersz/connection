package com.orvibo.cloud.connection.common.protocol;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by sunlin on 2017/9/21.
 */
public class BaseSession {
    /**
     * 终端设备的IP
     */
    @JSONField(name = "deviceIp")
    private String deviceIp;

    /**
     * 登录服务器IP
     */
    @JSONField(name = "loginServer")
    private String loginServer;

    /**
     *  32个字节的的字符串。服务器分配sessionId的时候使用Uuid来创建
     */
    @JSONField(name="sessionID")
    private String sessionID;

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getLoginServer() {
        return loginServer;
    }

    public void setLoginServer(String loginServer) {
        this.loginServer = loginServer;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public String toString() {
        return "BaseSession{" +
                "deviceIp='" + deviceIp + '\'' +
                ", loginServer='" + loginServer + '\'' +
                ", sessionID='" + sessionID + '\'' +
                '}';
    }
}
