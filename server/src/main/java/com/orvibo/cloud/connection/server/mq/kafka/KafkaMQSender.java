package com.orvibo.cloud.connection.server.mq.kafka;

import com.alibaba.fastjson.JSON;
import com.orvibo.cloud.connection.common.protocol.BaseCommandDTO;
import com.orvibo.cloud.connection.server.constants.Constants;
import com.orvibo.cloud.connection.server.mq.MQSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Created by sunlin on 2017/9/5.
 */
public class KafkaMQSender implements MQSender {

    private KafkaTemplate kafkaTemplate;

    private static final Logger logger = LoggerFactory.getLogger(KafkaAcknowledgeListener.class);

    public void sendBaseCommandDTO(BaseCommandDTO baseCommandDTO) {
        if (baseCommandDTO == null) {
            logger.error("BaseCommandDTO is null");
            return;
        }
        String json = JSON.toJSONString(baseCommandDTO);
        logger.info("send message BaseCommandDTO json string => {}", json);
        try {
            kafkaTemplate.send(Constants.CONNECTION_TEST_TOPIC, json);
        } catch (Exception e) {
            logger.error("send kafka mq failed.", e);
        }
        return;
    }

    public KafkaTemplate getKafkaTemplate() {
        return kafkaTemplate;
    }

    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

}
