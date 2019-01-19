package com.kafkainaction.chapter2;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Producer {
    public static void main(String[] args){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        org.apache.kafka.clients.producer.Producer<String, String> producer = new KafkaProducer<String, String>(props);
        ProducerRecord producerRecord = new ProducerRecord("helloworld",null, "hello world again!");
        producer.send(producerRecord);
        producer.close();
    }
}
