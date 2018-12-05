package com.example.android.bookshouse.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookshouse.R;
import com.example.android.bookshouse.data.BooksContract.BooksEntry;

//Allows user to add a new Book or edit an existing one.
public class BooksEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //min and max value for the book quantity
    private final int MINIMUM_QUANTITY_VALUE = 0;
    private final int MAXIMUM_QUANTITY_VALUE = 999;

    // Content URI for the existing book (null if it's a new Book)
    private Uri mCurrentBookUri;

    // Boolean flag that keeps track of whether the book has been edited (true) or not (false)
    private boolean mBookHasChanged = false;

    // Identifier for the Book data loader
    private static final int EXISTING_BOOK_LOADER = 0;

    private String mSupplierContact;

    //reference to all column fields
    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierContactEditText;

    //Add and Subtract button for the quantity
    private Button mAddButton;
    private Button mSubtractButton;

    // OnTouchListener that listens for any user touches on a View, implying that they are modifying
    // the view, and we change the mBookHasChanged boolean to true.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_editor);

        //Get Data if the Book already exists
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        //if book doesn't exist add a book else edit
        if (mCurrentBookUri == null) {
            setTitle(R.string.add_a_book);
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.edit_book);
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        //Find the data which is to be retrieved
        mProductNameEditText = findViewById(R.id.product_name_et);
        mPriceEditText = findViewById(R.id.price_et);
        mQuantityEditText = findViewById(R.id.quantity_et);
        mSupplierNameEditText = findViewById(R.id.supplier_name_et);
        mSupplierContactEditText = findViewById(R.id.supplier_phone_number_et);

        //Add Quantity Button to add by +1
        mAddButton = findViewById(R.id.add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQuantityString = mQuantityEditText.getText().toString();
                int currentQuantityInt;
                //if empty start by 1
                if (currentQuantityString.length() == 0) {
                    currentQuantityInt = 1;
                    mQuantityEditText.setText(String.valueOf(currentQuantityInt));
                } else { //else add +1 to previous till max limit
                    currentQuantityInt = Integer.parseInt(currentQuantityString) + 1;
                    if (currentQuantityInt <= MAXIMUM_QUANTITY_VALUE) {
                        mQuantityEditText.setText(String.valueOf(currentQuantityInt));
                    }
                }
            }
        });

        //subtract quantity on button click by -1
        mSubtractButton = findViewById(R.id.subtract);
        mSubtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQuantityString = mQuantityEditText.getText().toString();
                int currentQuantityInt;
                //if empty fill 0
                if (currentQuantityString.length() == 0) {
                    currentQuantityInt = 0;
                    mQuantityEditText.setText(String.valueOf(currentQuantityInt));
                } else {  // else subtract -1 to previous till min value
                    currentQuantityInt = Integer.parseInt(currentQuantityString) - 1;
                    if (currentQuantityInt >= MINIMUM_QUANTITY_VALUE) {
                        mQuantityEditText.setText(String.valueOf(currentQuantityInt));
                    }
                }
            }
        });

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mProductNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierContactEditText.setOnTouchListener(mTouchListener);
        mAddButton.setOnTouchListener(mTouchListener);
        mSubtractButton.setOnTouchListener(mTouchListener);
    }


    //insert into database
    private void insertData() {
        String productName, supplierName, supplierContact;
        int price, quantity;
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
        if (productName.length() == 0) {
            //Display Toast when Product Name empty
            Toast.makeText(this, R.string.enter_product_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (price == 0) {
            // Display a Toast if price 0
            Toast.makeText(this, R.string.enter_correct_price, Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantity == 0) {
            // Display Toast if quantity 0
            Toast.makeText(this, R.string.zero_quantity, Toast.LENGTH_SHORT).show();
            return;
        }
        if (supplierName.length() == 0) {
            // Display Toast if Supplier Name empty
            Toast.makeText(this, R.string.enter_supplier_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (supplierContact.length() < 10) {
            // Toast when contact is less than 10 in length
            Toast.makeText(this, R.string.not_a_valid_contact, Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and books attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(BooksEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(BooksEntry.COLUMN_PRICE, price);
        values.put(BooksEntry.COLUMN_QUANTITY, quantity);
        values.put(BooksEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierContact);

        // Determine whether it is new or existing book data
        if (mCurrentBookUri == null) {
            Uri newUri = getContentResolver().insert(BooksEntry.CONTENT_URI, values);
            if (newUri == null)
                Toast.makeText(this, R.string.error_saving_book_data, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, R.string.book_data_saved, Toast.LENGTH_SHORT).show();
        } else {    // else edit and update the Book
            int rowAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
            if (rowAffected == 0)
                Toast.makeText(this, R.string.error_updating_book, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, R.string.book_updated, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu in BooksEditorActivity
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // if this is a new Book hide the delete and contact menu actions
        if (mCurrentBookUri == null) {
            MenuItem menuItem;
            menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
            menuItem = menu.findItem(R.id.action_contact_supplier);
            menuItem.setVisible(false);
        }
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

            //respond with intent to any dialer
            case R.id.action_contact_supplier:
                callSupplier();
                return true;

            //respond with a confirmation dialog then delete accordingly
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;

            //respond for Up button action
            case android.R.id.home:
                // if nothing changed continue to parent activity
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(BooksEditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(BooksEditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Action to take On Back Pressed
    @Override
    public void onBackPressed() {
        // check if there is no change
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        DialogInterface.OnClickListener discardButtonClickListener =
                // User clicked "Discard" button, close the current activity.
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all book attributes, define a projection that contains
        // all columns from the books table
        String[] projection = {
                BooksEntry._ID,
                BooksEntry.COLUMN_PRODUCT_NAME,
                BooksEntry.COLUMN_PRICE,
                BooksEntry.COLUMN_QUANTITY,
                BooksEntry.COLUMN_SUPPLIER_NAME,
                BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                mCurrentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //return early is condition true
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        if (cursor.moveToFirst()) {

            // Find the columns of book attributes
            int productNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_NAME);
            int supplierContactColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            // Extract out the value from the Cursor for the given column index
            String productName = cursor.getString(productNameColumnIndex);
            int productPrice = cursor.getInt(priceColumnIndex);
            int productQuantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            mSupplierContact = cursor.getString(supplierContactColumnIndex);

            // Update the views with the values from the database
            mProductNameEditText.setText(productName);
            mPriceEditText.setText(String.valueOf(productPrice));
            mQuantityEditText.setText(String.valueOf(productQuantity));
            mSupplierNameEditText.setText(String.valueOf(supplierName));
            mSupplierContactEditText.setText(String.valueOf(mSupplierContact));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mProductNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierContactEditText.setText("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        //Create an Alert for Unsaved Changed
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_and_quit_editing);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null)
                    dialogInterface.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Prompt the user to confirm that they want to delete this book
    private void showDeleteConfirmationDialog() {
        //Create an Alert for delete action
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked the "Delete" button, so delete the book
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialogInterface != null)
                    dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void callSupplier() {
        //Intent to a Dialer in device
        Intent supplierContactIntent = new Intent(Intent.ACTION_DIAL);
        //send the contact info with the intent
        supplierContactIntent.setData(Uri.parse("tel:" + mSupplierContact));
        startActivity(supplierContactIntent);
    }

    private void deleteBook() {
        // Only perform the delete if this is an existing book
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the book at the given content Uri
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.error_in_deleting_book, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.book_deleted, Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    }
}
