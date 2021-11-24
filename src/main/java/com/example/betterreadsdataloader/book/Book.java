package com.example.betterreadsdataloader.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Table(value = "book_by_id")
public record Book(
        @Id
        @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        String id,

        @Column("book_name")
        @CassandraType(type = CassandraType.Name.TEXT)
        String name,

        @Column("book_description")
        @CassandraType(type = CassandraType.Name.TEXT)
        String description,

        @Column("personal_name")
        @CassandraType(type = CassandraType.Name.DATE)
        LocalDate publishedDate,

        @Column("cover_ids")
        @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
        List<String> coverIds,

        @Column("author_names")
        @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
        List<String> authorNames,

        @Column("author_id")
        @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
        List<String> authorId) {
}
