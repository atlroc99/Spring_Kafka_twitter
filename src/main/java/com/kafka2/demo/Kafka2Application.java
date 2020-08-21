package com.kafka2.demo;

import com.kafka2.demo.twitter.TwitterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Kafka2Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Kafka2Application.class, args);
    }

}
