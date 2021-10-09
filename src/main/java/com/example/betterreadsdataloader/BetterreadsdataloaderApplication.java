package com.example.betterreadsdataloader;

import com.example.betterreadsdataloader.author.Author;
import com.example.betterreadsdataloader.author.AuthorRepository;
import com.example.betterreadsdataloader.connection.DataStaxAstraProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
@Slf4j
public class BetterreadsdataloaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BetterreadsdataloaderApplication.class, args);
    }

    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.works}")
    private String worksDumpLocation;

    @Value("datastax.astra.secure-connection-bundle")
    private String secureConnectionBundle;

    @Autowired
    AuthorRepository authorRepository;

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer() {
        return cqlSessionBuilder -> cqlSessionBuilder.withCloudSecureConnectBundle(Paths.get(secureConnectionBundle));
    }

    private void initAuthors(){
        Path path = Paths.get(authorDumpLocation);
        try {
            Stream<String> lines = Files.lines(path);
            lines.limit(5).forEach(line -> {
                String jsonString = line.substring(line.indexOf("{"));
                JSONObject jsonObject = new JSONObject();
                try {
                     jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Author author = new Author();
                author.setName(jsonObject.optString("name"));
                author.setPersonalName(jsonObject.optString("personal_name"));
                author.setId(jsonObject.optString("key").replace("/authors/", ""));
                authorRepository.save(author);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initWorks(){

    }

    @PostConstruct
    public void run() {
        log.info("start");
        long start = System.currentTimeMillis();
        initAuthors();
        long stop = System.currentTimeMillis();
        log.info("stop");
        log.info("data loaded in: "+ (stop - start));
    }
}
