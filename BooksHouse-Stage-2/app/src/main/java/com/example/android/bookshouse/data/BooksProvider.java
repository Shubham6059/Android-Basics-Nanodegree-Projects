package com.example.android.bookshouse.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bookshouse.data.BooksContract.BooksEntry;

import java.net.URI;

//Content Provider for the app
public class BooksProvider extends ContentProvider {
    // LOG_TAG to display log messages
    private static final String LOG_TAG = BooksProvider.class.getSimpleName();

    private BooksDbHelper mDbHelper;

    // URI matcher code for the content URI for the books table
    private static final int BOOKS = 100;

    // URI matcher code for the content URI for a single book in the books table
    private static final int BOOK_ID = 101;

    // UriMatcher object to match a content URI to a corresponding code.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // static initializer
    static {
        // Map the Content Uri with the Uri Matcher codes
        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH_BOOKS + "/#", BOOK_ID);
    }

    @Override
    public boolean onCreate() {
        // create instance of BooksDbHelper
        mDbHelper = new BooksDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();
        Cursor cursor;

        // Check if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                // Query with the books table
                cursor = sqLiteDatabase.query(BooksEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                // Extract _ID from Uri and query the books table
                selection = BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the books table where the extracted Id return a
                // Cursor containing that row of the table.
                cursor = sqLiteDatabase.query(BooksEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query unknown URI " + uri);
        }

        //setNotificationUri on Cursor so we know what content URI the Cursor was created for
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    //Returns the MIME type of data for the content URI.
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BooksEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BooksEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    //Insert new data into the provider with the given ContentValues.
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion not supported for " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues contentValues) {
        // Perform the check if the data to be inserted is filled corrected
        checkList(contentValues);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new book with the given values
        long id = database.insert(BooksEntry.TABLE_NAME, null, contentValues);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    // TO check if the inserted data is filled correctly or not
    public void checkList(ContentValues contentValues) {
        String productName = contentValues.getAsString(BooksEntry.COLUMN_PRODUCT_NAME);
        if (productName == null) {
            throw new IllegalArgumentException("Product Name can't be empty");
        }

        Integer price = contentValues.getAsInteger(BooksEntry.COLUMN_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Price should be valid");
        }

        Integer quantity = contentValues.getAsInteger(BooksEntry.COLUMN_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Quantity should be valid");
        }

        String supplierName = contentValues.getAsString(BooksEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Supplier Name can't be empty");
        }

        String supplierContact = contentValues.getAsString(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supplierContact.length() < 10) {
            throw new IllegalArgumentException("Phone Number not valid");
        }
    }

    //Perform delete action
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        // Track the number of rows that were deleted
        int rowsDeleted;
        switch (match) {
            case BOOKS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(BooksEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                // Delete a single row given by the ID in the URI
                selection = BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BooksEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        //If deleted then notify all listeners that the data at the given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    //Perform update action for book
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                selection = BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // check for the data values updated are correct and non null
        updateCheckList(values);

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(BooksEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        //return updated rows
        return rowsUpdated;
    }

    // To check if the input values are correct and non null else throw exception
    public void updateCheckList(ContentValues values) {
        if (values.containsKey(BooksEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(BooksEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        if (values.containsKey(BooksEntry.COLUMN_PRICE)) {
            Integer price = values.getAsInteger(BooksEntry.COLUMN_PRICE);
            if (price == null || price < 0) {
                throw new IllegalArgumentException("Price not valid");
            }
        }
        if (values.containsKey(BooksEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(BooksEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Quantity not valid");
            }
        }
        if (values.containsKey(BooksEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(BooksEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Supplier Name can't be empty");
            }
        }
        if (values.containsKey(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER)) {
            String phone = values.getAsString(BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            if (phone.length() < 10) {
                throw new IllegalArgumentException("Supplier Contact not valid");
            }
        }
    }
}
