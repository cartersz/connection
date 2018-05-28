package com.orvibo.cloud.connection.server.mq;

/**
 * Created by sunlin on 2017/9/6.
 */
public interface MQReceiver {
    public void receiveBaseCommandDTO(String jsonContent);
}
