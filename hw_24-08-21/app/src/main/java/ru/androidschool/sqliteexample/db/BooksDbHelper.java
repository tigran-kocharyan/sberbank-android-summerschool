package ru.androidschool.sqliteexample.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BooksDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BookShelves.db";

    public BooksDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + BooksDbSchema.BooksTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                BooksDbSchema.BooksTable.Cols.UUID + " text, " +
                BooksDbSchema.BooksTable.Cols.TITLE + " text, " +
                BooksDbSchema.BooksTable.Cols.AUTHOR + " text, " +
                BooksDbSchema.BooksTable.Cols.SHELF_NUMBER + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}