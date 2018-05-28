package com.orvibo.cloud.connection.common.protocol;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by sunlin on 2017/6/19.
 */
public class CommandPackage {
//    /**
//     *  终端设备的IP
//     */
//    @JSONField(name = "deviceIp")
//    private String deviceIp;

    /**
     * 数据包固定开始字符，固定填写hd的ASCII码
     */
    private String head = "hd";

    /**
     *  数据包长度
     */
    private int length;

    /**
     *  协议类型 两个字节
     */
    @JSONField(name="pt")
    private String protocolType;

    /**
     *  CRC校验位 四个字节
     */
    private String crc;


    /**
     *  32个字节的的字符串。服务器分配sessionId的时候使用Uuid来创建
     */
    @JSONField(name="sessionID")
    private String sessionID;


    /**
     *  有效数据， 采用json格式，经过AES128解密
     */
    @JSONField(name="payload")
    private String payload; // 有效数据

    public String getHead() {
        return head;
    }

//    public void setHead(String head) {
//        this.head = head;
//    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

//    public String getDeviceIp() {
//        return deviceIp;
//    }
//
//    public void setDeviceIp(String deviceIp) {
//        this.deviceIp = deviceIp;
//    }

    public String toString() {
        return String.format("head= {%s}, length= {%d}, protocolType= {%s}, crc= {%s}, sessionID= {%s}, payload= {%s}",
                this.head, this.length,this.protocolType,this.crc,this.sessionID,this.payload);
    }

}
