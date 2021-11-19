package com.example.betterreadsdataloader.author;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends ReactiveCassandraRepository<Author, String> {
}
