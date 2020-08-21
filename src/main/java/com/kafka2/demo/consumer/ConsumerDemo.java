package com.kafka2.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class ConsumerDemo {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ConsumerDemo.class);

        Properties properties = new Properties();
        final String BOOTSTRAP_SERVER = "localhost:9092";
        final String CONSUMER_GROUP_ID= "spring-kafka2";
        final String OFFSET_CONFIG = "earliest";
        final String TOPIC = "simple-topic";


        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER );
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET_CONFIG);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(TOPIC));
        ConsumerRecords<String,String> consumerRecords = null;

        while (true) {
            consumerRecords = consumer.poll(Duration.ofMillis(1000));
            logger.info("Consumer-record conut: " + consumerRecords.count());
            for (ConsumerRecord<String,String> record : consumerRecords) {
                logger.info("Key: " + record.key() + " value: " + record.value()
                        + "\n " + record.topic() 
                        + "\n Offset: " + record.offset()
                        + "\n Partition: " + record.partition()
                        + "\n TimeStamp:" + new Date(record.timestamp()));
            }
        }

    }
}
