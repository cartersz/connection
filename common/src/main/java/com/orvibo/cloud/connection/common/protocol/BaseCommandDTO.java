package com.orvibo.cloud.connection.common.protocol;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by sunlin on 2017/9/21.
 */
public class BaseCommandDTO extends BaseSession {

    /**
     *  协议类型 两个字节
     */
    @JSONField(name="pt")
    private String protocolType;

    /**
     *  有效数据， 采用json格式
     */
    @JSONField(name="payload")
    private String payload; // 有效数据

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    @Override
    public String toString() {
        return "BaseCommandDTO{" +
                "payload='" + payload + '\'' +
                "， protocolType= " + protocolType +'\'' +
                ", BaseSession{" + super.toString() + '\'' +
                '}';
    }
}
