package com.example.android.bookshouse.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bookshouse.data.BooksContract.BooksEntry;

//Custom BooksDbHelper class for creating database
public class BooksDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "books.db";

    // public constructor
    public BooksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Creating SQLite Database referencing values from the BooksContract
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BooksEntry.TABLE_NAME + " ("
                + BooksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BooksEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + BooksEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + BooksEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + BooksEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " INTEGER NOT NULL );";

        // Execute above statement
        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Not required as database is still in version 1
    }
}
