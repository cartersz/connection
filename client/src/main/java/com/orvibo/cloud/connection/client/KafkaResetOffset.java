package com.orvibo.cloud.connection.client;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * Created by sunlin on 2017/9/27.
 */
public class KafkaResetOffset implements ConsumerRebalanceListener{

    private KafkaConsumer<String, String> consumer;

    public static void main(String[] args) {
        KafkaResetOffset kafkaResetOffset = new KafkaResetOffset();
        String topic = "connection-test";
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.2.201:9092,192.168.2.202:9092,192.168.2.192:9092");
        props.put("group.id", "0");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaResetOffset.setConsumer(new KafkaConsumer<String, String>(props));
        List<PartitionInfo> partitionInfos = kafkaResetOffset.getConsumer().partitionsFor(topic);
        System.out.printf("partitionInfos.length = %d", partitionInfos.size());
        System.out.println("");
        kafkaResetOffset.getConsumer().subscribe(Arrays.asList(topic), kafkaResetOffset);
//        for (PartitionInfo pi : partitionInfos) {
//            System.out.printf("topic = %s, partition = %d", pi.topic(), pi.partition());
//            System.out.println("");
//            consumer.seekToEnd(Collections.singleton(new TopicPartition(pi.topic(), pi.partition())));
//        }
//
        while (true) {
            ConsumerRecords<String, String> records = kafkaResetOffset.getConsumer().poll(100);
//            System.out.printf("records.length =  %d", records.count());
            System.out.println("");
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("###offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
                System.out.println("");
            }
        }
//        Thread.sleep(1000 * 60);
    }

    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
        System.out.println("call onPartitionsRevoked");
        for (TopicPartition tp : collection) {
            System.out.printf("topic = %s, partition = %d", tp.topic(), tp.partition());
            System.out.println("");
        }
    }

    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        System.out.println("call onPartitionsAssigned");
        for (TopicPartition tp : collection) {
            System.out.printf("topic = %s, partition = %d", tp.topic(), tp.partition());
            System.out.println("");
            this.consumer.seekToEnd(Collections.singleton(tp));
        }
    }

    public void setConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    public KafkaConsumer<String, String> getConsumer() {
        return this.consumer;
    }
}
