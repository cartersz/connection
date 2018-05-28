package com.orvibo.cloud.connection.client;

import com.alibaba.fastjson.JSON;
import com.orvibo.cloud.connection.common.protocol.ResponseCommandDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by sunlin on 2017/9/18.
 */
@Component
public class ClientKafkaMQSender {

    private static final Logger logger = LoggerFactory.getLogger(ClientKafkaMQSender.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendCommandDTO(String topic, ResponseCommandDTO responseCommandDTO) {
        if (StringUtils.isEmpty(topic) || responseCommandDTO == null) {
            throw new IllegalArgumentException("topic and responseCommandDTO should not be empty");
        }
        String json = JSON.toJSONString(responseCommandDTO);
        logger.info("client send mq, topic={}, message=> {}", topic, json);
        try {
            kafkaTemplate.send(topic, json);
        }catch (Exception e){
            logger.error("client send mq failed.", e);
        }
    }

}
