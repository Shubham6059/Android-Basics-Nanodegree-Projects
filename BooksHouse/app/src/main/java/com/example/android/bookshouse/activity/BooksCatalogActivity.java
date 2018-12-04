package com.example.android.bookshouse.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.bookshouse.R;
import com.example.android.bookshouse.data.BooksContract.BooksEntry;
import com.example.android.bookshouse.data.BooksDbHelper;

public class BooksCatalogActivity extends AppCompatActivity {
    private BooksDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_catalog);

        // FAB to add more Books
        FloatingActionButton fab = findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BooksCatalogActivity.this, BooksEditorActivity.class);
                startActivity(intent);
            }
        });
        // passing context to the public constructor
        mDbHelper = new BooksDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    // Query the database
    private Cursor queryData() {
        // Create or open a database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // projection for which items will be used for query
        String[] projection = {
                BooksEntry._ID,
                BooksEntry.COLUMN_PRODUCT_NAME,
                BooksEntry.COLUMN_PRICE,
                BooksEntry.COLUMN_QUANTITY,
                BooksEntry.COLUMN_SUPPLIER_NAME,
                BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER,
        };

        // Perform a query and return this value
        return database.query(
                BooksEntry.TABLE_NAME,      //table to query
                projection,                 //column to return
                null,              //The columns for the WHERE clause
                null,           //The values for the WHERE clause
                null,               //Don't group the rows
                null,                //Don't filter by row groups
                null,               //The sort order
                null);                 //String limit
    }

    private void displayDatabaseInfo() {

        Cursor cursor = queryData();

        TextView books = findViewById(R.id.books_details);
        try {

            String tableContains = getString(R.string.the_books_table_contains) + " " +
                    cursor.getCount() + " " + getString(R.string.books);
            //set value of above message as TextView
            books.setText(tableContains);

            int _idColumnIndex, productNameColumnIndex, priceColumnIndex, quantityColumnIndex,
                    supplierNameColumnIndex, supplierContactColumnIndex;

            //find the index of each column
            _idColumnIndex = cursor.getColumnIndex(BooksEntry._ID);
            productNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_NAME);
            priceColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_PRICE);
            quantityColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_QUANTITY);
            supplierNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_NAME);
            supplierContactColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            //iterate to all rows
            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(_idColumnIndex);
                String currentProductName = cursor.getString(productNameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierContact = cursor.getString(supplierContactColumnIndex);

                //Display the database in TextView
                books.append("\n" + BooksEntry._ID + ":\t\t" + currentId + "\n" +
                        BooksEntry.COLUMN_PRODUCT_NAME + ":\t\t" + currentProductName + "\n" +
                        BooksEntry.COLUMN_PRICE + ":\t\t" + currentPrice + "\n" +
                        BooksEntry.COLUMN_QUANTITY + ":\t\t" + currentQuantity + "\n" +
                        BooksEntry.COLUMN_SUPPLIER_NAME + ":\t\t" + currentSupplierName + "\n" +
                        BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER + ":\t\t" + currentSupplierContact + "\n");
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
}
