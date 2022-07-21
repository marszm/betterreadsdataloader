package com.example.betterreadsdataloader.book;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ReactiveCassandraRepository<Book, String>{
}
