package com.example.betterreadsdataloader;

import com.example.betterreadsdataloader.author.Author;
import com.example.betterreadsdataloader.author.AuthorRepository;
import com.example.betterreadsdataloader.connection.DataStaxAstraProperties;
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
        System.out.println("start");
        long start = System.currentTimeMillis();
        initAuthors();
        long stop = System.currentTimeMillis();
        System.out.println("stop");
        System.out.println("time between start and stop: "+ (stop - start));
    }
}
