package com.orvibo.cloud.connection.server.mq.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;

/**
 * Created by sunlin on 2017/7/11.
 */
public class KafkaProducerListener implements ProducerListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerListener.class);

    public void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {
        logger.info("========== kafka发送数据成功（日志开始）==========");
        logger.info("----------topic:"+topic);
        logger.info("----------partition:"+partition);
        logger.info("----------key:"+key);
        logger.info("----------value:"+value);
        logger.info("----------RecordMetadata:"+recordMetadata);
        logger.info("========== kafka发送数据成功（日志结束）==========");
    }

    public void onError(String topic, Integer partition, Object key, Object value, Exception e) {
        logger.info("~~~~~~~~~~ kafka发送数据错误（日志开始）~~~~~~~~~~");
        logger.info("----------topic:{}", topic);
        logger.info("----------partition:{}", partition);
        logger.info("----------key:{}", key);
        logger.info("----------value:{}", value);
        logger.error("----------Exception:{}", e);
        logger.info("~~~~~~~~~~ kafka发送数据错误（日志结束）~~~~~~~~~~");
    }

    public boolean isInterestedInSuccess() {
        logger.info("isInterestedInSuccess execute!!");
        return true;
    }
}
