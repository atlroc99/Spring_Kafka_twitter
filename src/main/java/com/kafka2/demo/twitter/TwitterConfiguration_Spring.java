package com.kafka2.demo.twitter;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
    This class read properties file in the Springway by annotating by @ConfigurationProperties
* */
@Component
@ConfigurationProperties
@Data
@ToString
public class TwitterConfiguration_Spring {
    @Value("${twitter.api.key}")
    private String apiKey;

    @Value("${twitter.secret.key}")
    private String secretKey;

}
