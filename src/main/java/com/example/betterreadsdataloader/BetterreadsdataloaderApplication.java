package com.example.betterreadsdataloader;

import com.example.betterreadsdataloader.connection.DataStaxAstraProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
@Slf4j
@EnableScheduling
public class BetterreadsdataloaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BetterreadsdataloaderApplication.class, args);
    }
    }