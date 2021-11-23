package com.example.betterreadsdataloader.connection;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;

@ConfigurationProperties(prefix = "datastax.astra")
@Component
@Getter
@Setter
@Configuration
public class DataStaxAstraProperties {

    private File secureConnectBundle;

}
