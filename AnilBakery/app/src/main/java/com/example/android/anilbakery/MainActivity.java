package com.example.android.anilbakery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView address = findViewById(R.id.address);
        final String s1 = address.getText().toString();
        /*Following code gets address from TextView and using Geo Coordinates
         * of address and Intent transfers the action to open location in Google Maps App*/
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("geo:26.216648,81.253874?q=" + Uri.encode(s1));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
