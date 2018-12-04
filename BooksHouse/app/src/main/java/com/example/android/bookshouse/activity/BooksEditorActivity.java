package com.example.android.bookshouse.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookshouse.R;
import com.example.android.bookshouse.data.BooksContract.BooksEntry;
import com.example.android.bookshouse.data.BooksDbHelper;

public class BooksEditorActivity extends AppCompatActivity {

    //reference to all column fields
    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierContactEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_editor);

        mProductNameEditText = findViewById(R.id.product_name_et);
        mPriceEditText = findViewById(R.id.price_et);
        mQuantityEditText = findViewById(R.id.quantity_et);
        mSupplierNameEditText = findViewById(R.id.supplier_name_et);
        mSupplierContactEditText = findViewById(R.id.supplier_phone_number_et);
    }

    //insert into database
    private void insertData() {
        String productName = null, supplierName = null, supplierContact = null;
        int price = 0, quantity = 0;

        try {
            //read input and trim any trailing white spaces
            productName = mProductNameEditText.getText().toString().trim();
            price = Integer.parseInt(mPriceEditText.getText().toString().trim());
            quantity = Integer.parseInt(mQuantityEditText.getText().toString().trim());
            supplierName = mSupplierNameEditText.getText().toString().trim();
            supplierContact = mSupplierContactEditText.getText().toString().trim();
        } catch (Exception ex) {
            //if any EditText if not filled display a Toast
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        if (price == 0) {
            // Display a Toast if price 0
            Toast.makeText(this, R.string.enter_correct_price, Toast.LENGTH_SHORT).show();
            return;
        }
        if (supplierContact.length() < 10) {
            // Toast when contact is less than 10 in length
            Toast.makeText(this, R.string.not_a_valid_contact, Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantity == 0) {
            // Display Toast if quantity 0
            Toast.makeText(this, R.string.zero_quantity, Toast.LENGTH_SHORT).show();
            return;
        }

        //Create database helper
        BooksDbHelper dbHelper = new BooksDbHelper(this);
        // Get the database in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and books attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(BooksEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(BooksEntry.COLUMN_PRICE, price);
        values.put(BooksEntry.COLUMN_QUANTITY, quantity);
        values.put(BooksEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierContact);

        long newRowId = db.insert(BooksEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            Toast.makeText(this, R.string.error_saving_book_data, Toast.LENGTH_SHORT).show();
        } else {
            String saved = getString(R.string.book_data_saved) + " " + newRowId;
            Toast.makeText(this, saved, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu in BooksEditorActivity
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to menu item select
        switch (item.getItemId()) {
            case R.id.action_save:
                //save the database
                insertData();
                finish();
                return true;

            case android.R.id.home:
                //Move Up to parent activity
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
