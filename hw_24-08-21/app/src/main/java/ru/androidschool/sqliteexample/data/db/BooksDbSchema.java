package ru.androidschool.sqliteexample.data.db;

public class BooksDbSchema {
    public static final class BooksTable {
        public static final String NAME = "books";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String AUTHOR = "author";
            public static final String SHELF_NUMBER = "shelf_number";
            public static final String COUNT = "count";
        }
    }
}