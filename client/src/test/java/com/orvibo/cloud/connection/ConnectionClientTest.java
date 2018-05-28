package com.orvibo.cloud.connection;

import com.orvibo.cloud.connection.client.ConnectionClient;
import org.junit.Test;

/**
 * Created by sunlin on 2017/9/18.
 */
public class ConnectionClientTest {

    @Test
    public void sendCommandTest() throws Exception {
        ConnectionClient.sendCommand("/127.0.0.1:61715", 0, "json_payload_1");
    }
}
