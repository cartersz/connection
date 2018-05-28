package com.orvibo.cloud.connection.server.mq.kafka;

import com.orvibo.cloud.connection.server.mq.MQReceiver;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * Created by sunlin on 2017/7/12.
 */
public class KafkaAcknowledgeListener implements AcknowledgingMessageListener<Integer, String> {

    private MQReceiver mqReceiver;

    private static final Logger logger = LoggerFactory.getLogger(KafkaAcknowledgeListener.class);

    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
        String record = consumerRecord.value();
        logger.info("receive message from MQ => {}", record);
        try {
            mqReceiver.receiveBaseCommandDTO(record);
        } catch (Exception e) {
            logger.error("mqReceiver process record failed.", e);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    public MQReceiver getMqReceiver() {
        return mqReceiver;
    }

    public void setMqReceiver(MQReceiver mqReceiver) {
        this.mqReceiver = mqReceiver;
    }
}
