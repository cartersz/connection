package com.orvibo.cloud.connection.server.tcp;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.server.tcp.command.CommandJsonReader;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by sunlin on 2017/6/28.
 */
public class NettyTCPServerTest {

    private NettyClient nc;
    @Before
    public void startClient() throws Exception {
        nc = new NettyClient();
        nc.connect("127.0.0.1", 10010);
    }

    @Test
    public void sendRequestKeyCommandTest() throws Exception {
        CommandPackage cp = CommandJsonReader.getCommandPackage("RequestKey.json");
        System.out.println("Start send RequestKeyCommand...");
        nc.sendMessage(cp);
        System.out.println("End send RequestKeyCommand...");

        Thread.sleep(1000 * 60 *2);
    }
    @Test
    public void sendDeviceLoginCommandTest() throws Exception {
        CommandPackage cp = CommandJsonReader.getCommandPackage("DeviceLogin.json");
        System.out.println("Start send RequestKeyCommand...");
        nc.sendMessage(cp);
        System.out.println("End send RequestKeyCommand...");

        Thread.sleep(1000 * 60 *2);
    }

    @Test
    public void startClientTest() throws Exception{
        Thread.sleep(1000 * 60 *2);
    }

    private static String getUUID() {
        String key = UUID.randomUUID().toString();
        // 去掉“-”,replaceAll 性能不好
        String[] keys = key.split("-");
        StringBuffer sb = new StringBuffer();
        for (String k : keys) {
            sb.append(k);
        }
        return sb.toString();
    }


}

