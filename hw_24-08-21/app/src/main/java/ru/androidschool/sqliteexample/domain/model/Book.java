package ru.androidschool.sqliteexample.domain.model;

public class Book {

    public final long bookId;
    public final String uuid;
    public final String title;
    public final String author;
    public final int shelfNumber;

    public Book(long bookId, String uuid, String title, String author, int shelfNumber) {
        this.bookId = bookId;
        this.uuid = uuid;
        this.title = title;
        this.author = author;
        this.shelfNumber = shelfNumber;
    }
}
