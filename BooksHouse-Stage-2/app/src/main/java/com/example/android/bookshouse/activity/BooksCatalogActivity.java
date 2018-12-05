package com.example.android.bookshouse.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookshouse.BooksCursorAdapter;
import com.example.android.bookshouse.R;
import com.example.android.bookshouse.data.BooksContract.BooksEntry;

public class BooksCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //Adapter for the ListView
    BooksCursorAdapter mCursorAdapter;

    //Empty Default TextView to display when database empty
    TextView emptyView;

    // Identifier for the Book data loader
    private static final int BOOK_LOADER = 0;

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

        //Assign ListView the default TextView
        ListView booksListView = findViewById(R.id.books_listView);
        emptyView = findViewById(R.id.emptyView);
        booksListView.setEmptyView(emptyView);

        //Set up an Adapter to create a list item for each row
        mCursorAdapter = new BooksCursorAdapter(this, null);
        booksListView.setAdapter(mCursorAdapter);

        //Set OnItemClickListener for the List items
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Intent to BooksEditorActivity with the details of that List Item
                Intent intent = new Intent(BooksCatalogActivity.this, BooksEditorActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(BooksEntry.CONTENT_URI, l);
                intent.setData(currentBookUri);
                startActivity(intent);
            }
        });
        //Kick off the loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    //Helper method to delete all Books
    private void deleteAllBooks() {
        // Toast accordingly if the data is deleted
        int rowsDeleted = getContentResolver().delete(BooksEntry.CONTENT_URI, null, null);
        if (rowsDeleted == 0) {
            Toast.makeText(this, R.string.error_in_deleting_book, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.all_books_deleted, Toast.LENGTH_SHORT).show();
        }
    }

    //Prompt an Alert to confirm delete action
    private void showDeleteConfirmationDialog() {

        //check if the list is not already empty
        if (!(emptyView.getVisibility() == View.VISIBLE)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_all_books);

            //Perform delete action
            builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteAllBooks();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (dialogInterface != null)
                        dialogInterface.dismiss();
                }
            });

            //Set AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    //Inflate menu options for this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Respond to Delete option from menu
        if (item.getItemId() == R.id.action_delete_all_entries) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table
        String[] projection = {
                BooksEntry._ID,
                BooksEntry.COLUMN_PRODUCT_NAME,
                BooksEntry.COLUMN_PRICE,
                BooksEntry.COLUMN_QUANTITY,
                BooksEntry.COLUMN_SUPPLIER_NAME,
                BooksEntry.COLUMN_SUPPLIER_PHONE_NUMBER,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                BooksEntry.CONTENT_URI,         // Provider content URI to query
                projection,                     // Columns to include in the resulting Cursor
                null,                  // No selection clause
                null,               // No selection arguments
                null);                 // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //update with this new cursor data
        mCursorAdapter.swapCursor(cursor);
    }

    //callback call when the data is to be deleted
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
