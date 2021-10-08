package com.example.betterreadsdataloader;

import com.example.betterreadsdataloader.author.Author;
import com.example.betterreadsdataloader.author.AuthorRepository;
import com.example.betterreadsdataloader.connection.DataStaxAstraProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;


@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BetterreadsdataloaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BetterreadsdataloaderApplication.class, args);
    }

    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.works}")
    private String worksDumpLocation;

    @Autowired
    AuthorRepository authorRepository;

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties dataStaxAstraProperties) {
        return cqlSessionBuilder -> cqlSessionBuilder.withCloudSecureConnectBundle(Paths.get("src/main/resources/secure-connect.zip"));
    }

    @PostConstruct
    public void run() {
        Author author = new Author();
        author.setId("1");
        author.setName("Mario");
        author.setPersonalName("0K0");
        authorRepository.save(author);
    }
}
