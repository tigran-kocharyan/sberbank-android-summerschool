package ru.androidschool.sqliteexample.domain;

import java.util.List;

import ru.androidschool.sqliteexample.domain.model.Book;

public class BooksInteractor {

    private final BooksRepository mRepository;

    public BooksInteractor(BooksRepository repository) {
        mRepository = repository;
    }

    public List<Book> getBooks() {
        mRepository.initRepository();
        return mRepository.books();
    }
}
