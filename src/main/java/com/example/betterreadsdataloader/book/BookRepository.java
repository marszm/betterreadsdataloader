package com.example.betterreadsdataloader.book;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface BookRepository extends ReactiveCassandraRepository<Book, String> {
}
