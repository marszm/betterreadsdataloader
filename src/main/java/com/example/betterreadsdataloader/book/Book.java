package com.example.betterreadsdataloader.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Table(value = "book_by_id")
public record Book(
        @Id
        @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        String id,
        @Column("author_name")
        @CassandraType(type = CassandraType.Name.TEXT)
        String name,
        @Column("personal_name")
        @CassandraType(type = CassandraType.Name.DATE)
        LocalDate publishedDate) {
}
