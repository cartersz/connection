package com.orvibo.cloud.connection.server.tcp.command;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sunlin on 2017/7/3.
 */
public class CommandJsonReaderTest {
    @Test
    public void getJsonStringTest() throws Exception{
        String fileName = "RequestKey.json";
        System.out.println(CommandJsonReader.getJsonString(fileName));
    }

    @Test
    public void getCommandPackageTesst() throws Exception {
        String fileName = "RequestKey.json";
        CommandPackage cp = CommandJsonReader.getCommandPackage(fileName);
        System.out.println(cp);
        JSONObject jsonObject = JSON.parseObject(cp.getPayload());
        Assert.assertEquals("pt is not equal", "pk", cp.getProtocolType());
        Assert.assertEquals("sessionID is not equal", "10000", cp.getSessionID());
        Assert.assertEquals("cmd is not equal", "rk", jsonObject.getString("cmd"));
        Assert.assertEquals("serial is not equal", "100", jsonObject.getString("serial"));
        Assert.assertEquals("source is not equal", "S20", jsonObject.getString("source"));
        Assert.assertEquals("softwareVersion is not equal", "v1.0.0", jsonObject.getString("softwareVersion"));
        Assert.assertEquals("sysVersion is not equal", "iOS 8.2", jsonObject.getString("sysVersion"));
        Assert.assertEquals("hardwareVersion is not equal", "hardware 1.0", jsonObject.getString("hardwareVersion"));
        Assert.assertEquals("language is not equal", "chinese", jsonObject.getString("language"));
    }
}