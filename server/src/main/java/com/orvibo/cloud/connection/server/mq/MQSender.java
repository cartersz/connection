package com.orvibo.cloud.connection.server.mq;


import com.orvibo.cloud.connection.common.protocol.BaseCommandDTO;

/**
 * Created by sunlin on 2017/9/5.
 */
public interface MQSender {

    public void sendBaseCommandDTO(BaseCommandDTO baseCommandDTO);
}
