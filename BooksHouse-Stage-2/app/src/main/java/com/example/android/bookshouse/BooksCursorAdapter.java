package com.example.android.bookshouse;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookshouse.data.BooksContract.BooksEntry;

//Custom Cursor Adapter for ListView
public class BooksCursorAdapter extends CursorAdapter {
    //public constructor
    public BooksCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        // Inflate a list item view using the layout specified in xml
        return LayoutInflater.from(context).inflate(R.layout.book_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView bookName = view.findViewById(R.id.book_name);
        TextView bookPrice = view.findViewById(R.id.book_price);
        TextView bookQuantity = view.findViewById(R.id.book_quantity);

        // Find the columns of book attributes
        int bookNameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_NAME);
        int bookPriceColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_PRICE);
        int bookQuantityColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_QUANTITY);

        //set book name
        String productName = cursor.getString(bookNameColumnIndex);
        bookName.setText(productName);

        //set price
        int productPrice = cursor.getInt(bookPriceColumnIndex);
        bookPrice.setText(String.valueOf(productPrice));

        //set quantity
        int productQuantity = cursor.getInt(bookQuantityColumnIndex);
        bookQuantity.setText(String.valueOf(productQuantity));

        //get column index for _ID
        int productIdColumnIndex = cursor.getColumnIndex(BooksEntry._ID);

        // Read the book attributes from the Cursor for the current book for "Sale" button
        final long productIdVal = Integer.parseInt(cursor.getString(productIdColumnIndex));
        final int currentProductQuantityVal = cursor.getInt(bookQuantityColumnIndex);

        // Sale button to show purchase
        Button saleButton = view.findViewById(R.id.saleButton);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On Click decrease the value of available quantity of the product
                Uri currentUri = ContentUris.withAppendedId(BooksEntry.CONTENT_URI, productIdVal);
                String updateQuantity = String.valueOf(currentProductQuantityVal - 1);

                // if the available quantity after sale is 0 or above
                if (Integer.parseInt(updateQuantity) >= 0) {
                    ContentValues values = new ContentValues();

                    // update value of the quantity
                    values.put(BooksEntry.COLUMN_QUANTITY, updateQuantity);
                    context.getContentResolver().update(currentUri, values, null, null);

                    // Display this Toast
                    Toast.makeText(context, R.string.purchase_toast, Toast.LENGTH_SHORT).show();
                } else      //Display this Toast when quantity 0
                    Toast.makeText(context, R.string.out_of_stock, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
