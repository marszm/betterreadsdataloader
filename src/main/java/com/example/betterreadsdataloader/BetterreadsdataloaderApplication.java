package com.example.betterreadsdataloader;

import com.example.betterreadsdataloader.connection.DataStaxAstraProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.nio.file.Paths;


@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BetterreadsdataloaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BetterreadsdataloaderApplication.class, args);
    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties dataStaxAstraProperties) {
        return cqlSessionBuilder -> cqlSessionBuilder.withCloudSecureConnectBundle(Paths.get("src/main/resources/secure-connect.zip"));
    }
}
