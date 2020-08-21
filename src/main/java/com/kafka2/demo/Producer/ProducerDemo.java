package com.kafka2.demo.Producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

public class ProducerDemo {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ProducerDemo.class);

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "ClientID_1");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        String topic = "simple-topic";
        String msg = "producer-msg-from-intelliJ-2";
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, "p1", msg);
        Future<RecordMetadata> metadata = producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e == null) {
                    logger.info("\n\t\t\t\t******************************************");
                    logger.info("\n\t\t\t\tTopic : " + recordMetadata.topic() +
                            "\n\t\t\t\tPartition: " + recordMetadata.partition() +
                            "\n\t\t\t\tOffset: " + recordMetadata.offset() +
                            "\n\t\t\t\tTime-stamp:" + new Date(recordMetadata.timestamp()));
                    logger.info("\n******************************************\n");
                } else {
                    e.printStackTrace(System.err);
                }
            }
        });

        producer.flush();
        producer.close();
    }
}
