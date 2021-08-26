package ru.androidschool.sqliteexample.domain;

import java.util.List;

import ru.androidschool.sqliteexample.domain.model.Book;

public interface BooksRepository {

    void initRepository();

    List<Book> books();

}
