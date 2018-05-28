package com.orvibo.cloud.connection.common.protocol;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by sunlin on 2017/9/21.
 */
public class ResponseCommandDTO extends BaseCommandDTO {
    /**
     * 指令
     */
    @JSONField(name="cmd")
    private int cmd;

    /**
     * payload 加密key
     */
    @JSONField(name="key")
    private String key;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ResponseCommandDTO{" +
                "cmd=" + cmd +
                ", key='" + key + '\'' +
                '}';
    }
}
