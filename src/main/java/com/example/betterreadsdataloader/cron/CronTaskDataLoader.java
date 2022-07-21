package com.example.betterreadsdataloader.cron;

import com.example.betterreadsdataloader.author.Author;
import com.example.betterreadsdataloader.author.AuthorRepository;
import com.example.betterreadsdataloader.book.Book;
import com.example.betterreadsdataloader.book.BookRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
@AllArgsConstructor
@EnableAsync
@NoArgsConstructor
public class CronTaskDataLoader {

    @Value("${datadump.location.author}")
    private String authorDumpLocation;

    @Value("${datadump.location.works}")
    private String worksDumpLocation;

    @Value("${datastax.astra.secure-connection-bundle}")
    private String secureConnectionBundle;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer() {
        return cqlSessionBuilder -> cqlSessionBuilder.withCloudSecureConnectBundle(Paths.get(secureConnectionBundle));
    }

    private void initAuthors() {
        Path path = Paths.get(authorDumpLocation);
        try {
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> {
                String jsonString = line.substring(line.indexOf("{"));
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Author author = new Author(jsonObject.optString("key").replace("/authors/", ""),
                        jsonObject.optString("name"),
                        jsonObject.optString("personal_name"));
                log.info("author: " + author);
                authorRepository.insert(author).subscribe();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initWorks() {
        Path path = Paths.get(worksDumpLocation);
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                String jsonString = line.substring(line.indexOf("{"));
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject descriptionObj = jsonObject.getJSONObject("description");
                    JSONObject publishedObj = jsonObject.getJSONObject("created");
                    String dateStr = publishedObj.getString("value");
                    JSONArray coversJSONArr = jsonObject.optJSONArray("covers");
                    List<String> coverIds = new ArrayList<>();
                    if (coversJSONArr != null) {
                        for (int i = 0; i < coversJSONArr.length(); i++) {
                            coverIds.add(coversJSONArr.getString(i));
                        }
                    }
                    JSONArray authorsJSONArr = jsonObject.optJSONArray("authors");
                    List<String> authorsIds = new ArrayList<>();
                    if (authorsJSONArr != null) {
                        for (int i = 0; i < authorsJSONArr.length(); i++) {
                            String authorId = authorsJSONArr.getJSONObject(i)
                                    .getJSONObject("author")
                                    .getString("key").replace("/authors/", "");
                            authorsIds.add(authorId);
                        }
                    }
                    Book book = new Book(
                            jsonObject.getString("key").replace("/works/", ""),
                            jsonObject.optString("title"),
                            descriptionObj.optString("value"),
                            LocalDate.parse(dateStr),
                            coverIds,
                            authorsIds
                    );
                    bookRepository.insert(book).subscribe();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 1000 * 24 * 3600)
    public void run() {
        log.info("start");
        long start = System.currentTimeMillis();
//        initAuthors();
        initWorks();
        long stop = System.currentTimeMillis();
        log.info("stop");
        log.info("data loaded in: " + (stop - start));
    }
}
