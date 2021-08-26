package ru.androidschool.sqliteexample.data.db;

import android.content.ContentValues;
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
        if (oldVersion < 2) {
            sqLiteDatabase.execSQL("alter table " + BooksDbSchema.BooksTable.NAME + " ADD " + BooksDbSchema.BooksTable.Cols.COUNT + " integer");
            ContentValues contentValues = new ContentValues();
            contentValues.put(BooksDbSchema.BooksTable.Cols.COUNT, 1);
            sqLiteDatabase.update(BooksDbSchema.BooksTable.NAME, contentValues, null, null);
        }
    }
}