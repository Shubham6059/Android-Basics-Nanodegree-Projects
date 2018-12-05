package com.example.android.bookshouse.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

// POJO class for storing Table values
public class BooksContract {
    private BooksContract() { // So that no instance can be created outside the class
    }

    // Create the base of all URI's which apps will use to contact the content provider.
    public static final String CONTENT_AUTHORITY = "com.example.android.bookshouse";

    //Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Possible path for the Uri
    public static final String PATH_BOOKS = "books";

    public static final class BooksEntry implements BaseColumns {
        // The content URI to access the pet data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        //For a List of Book data
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        //For a single book data
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        // books.db data fields
        public static final String TABLE_NAME = "books";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }
}
