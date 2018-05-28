package com.orvibo.cloud.connection.server.mq.kafka;

import com.orvibo.cloud.connection.server.mq.MQReceiver;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

/**
 * Created by sunlin on 2017/7/11.
 */
public class KafkaListener implements MessageListener<Integer, String> {

    private MQReceiver mqReceiver;

    private static final Logger logger = LoggerFactory.getLogger(KafkaListener.class);

    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
        String record = consumerRecord.value();
        logger.info("receive message from MQ => {}", record);
        mqReceiver.receiveBaseCommandDTO(record);
    }

    public MQReceiver getMqReceiver() {
        return mqReceiver;
    }

    public void setMqReceiver(MQReceiver mqReceiver) {
        this.mqReceiver = mqReceiver;
    }
}
