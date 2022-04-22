package com.xadamus;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {
    public static final Logger log = LoggerFactory.getLogger(ProducerDemo.class);

    public static final String BOOTSTRAP_SERVER = "172.18.33.138:9092";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "id_" + i, "Hello World " + i);
            producer.send(producerRecord, (metadata, exception) -> {
                if (exception == null) {
                    log.info("Received new metadata.\n" +
                            "Topic: " + metadata.topic() + "\n" +
                            "Key:" + producerRecord.key() + "\n" +
                            "Partition: " + metadata.partition() + "\n" +
                            "Offset: " + metadata.offset());
                } else {
                    log.error("Error while producing", exception);
                }
            });
        }

        producer.flush();
        producer.close();

    }
}
