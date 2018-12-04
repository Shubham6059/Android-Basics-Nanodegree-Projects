package com.example.android.bookshouse.data;

import android.provider.BaseColumns;

// POJO class for storing Table values
public class BooksContract {
    private BooksContract() { // So that no instance can be created outside the class
    }

    public static final class BooksEntry implements BaseColumns {

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
