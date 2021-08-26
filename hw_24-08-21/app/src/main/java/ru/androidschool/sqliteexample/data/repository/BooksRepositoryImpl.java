package ru.androidschool.sqliteexample.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import ru.androidschool.sqliteexample.db.BooksDbHelper;
import ru.androidschool.sqliteexample.db.BooksDbSchema;
import ru.androidschool.sqliteexample.domain.BooksRepository;
import ru.androidschool.sqliteexample.domain.model.Book;

public class BooksRepositoryImpl implements BooksRepository {
    private Context mContext;

    public BooksRepositoryImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void initRepository() {
        SQLiteDatabase db = new BooksDbHelper(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BooksDbSchema.BooksTable.Cols.UUID, "12309038493");
        values.put(BooksDbSchema.BooksTable.Cols.TITLE, "Война и мир - Том 1");
        values.put(BooksDbSchema.BooksTable.Cols.AUTHOR, "Л.Н.Толстой");
        values.put(BooksDbSchema.BooksTable.Cols.SHELF_NUMBER, 1);
        long newRowId = db.insert(BooksDbSchema.BooksTable.NAME, null, values);
    }

    @Override
    public List<Book> books() {
        SQLiteDatabase db = new BooksDbHelper(mContext).getReadableDatabase();
        List<Book> books = new ArrayList<>();
        try (Cursor cursor = db.query(
                BooksDbSchema.BooksTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        )) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(
                        cursor.getColumnIndex(BooksDbSchema.BooksTable.Cols.TITLE));
                String UUID = cursor.getString(
                        cursor.getColumnIndex(BooksDbSchema.BooksTable.Cols.UUID));
                String author = cursor.getString(
                        cursor.getColumnIndex(BooksDbSchema.BooksTable.Cols.AUTHOR));
                int shelf_number = cursor.getInt(
                        cursor.getColumnIndex(BooksDbSchema.BooksTable.Cols.SHELF_NUMBER));
                long bookId = cursor.getLong(
                        cursor.getColumnIndex(BaseColumns._ID));
                books.add(new Book(bookId, UUID, title, author, shelf_number));
            }
        }
        return books;
    }
}
