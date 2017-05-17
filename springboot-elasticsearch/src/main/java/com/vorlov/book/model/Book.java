package com.vorlov.book.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;

@Document(indexName = "vorlov", type = "books")
public class Book {

    @Id
    private String id;

    private String title;

    private String author;

    private String releaseDate;

    private LocalDate localDate;

    public Book() {
    }

    public Book(String title, String author, String releaseDate) {
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.localDate = LocalDate.now();
    }

    public Book(String id, String title, String author, String releaseDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
    }

    public Book(String id, String title, String author, String releaseDate, LocalDate localDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.localDate = localDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
