package com.thoughtbubble.tom.thoughtbubblejournal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class manages and reads the entries from the database.
 */
public class ReadEntry extends AppCompatActivity {

    // Database descriptors
    SQLiteDatabase db;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_entry);

        // Open DB Connections
        db = openOrCreateDatabase("ThoughtBubble.db",MODE_PRIVATE,null);
        dbHelper = new DbHelper(this);
        dbHelper.onCreate(db);

        // Get context references
        TextView textView = (TextView)findViewById(R.id.reading_area);
        Button back = (Button)findViewById(R.id.gobackbtn);
        Button delete = (Button)findViewById(R.id.deletebtn);


        Intent thisIntent = getIntent();

        // Get user key signature
        String key = dbHelper.getKey(thisIntent.getStringExtra("email"),db);
        db = openOrCreateDatabase("ThoughtBubble.db",MODE_PRIVATE,null);

        // Get entry from the database using email and timestamp
        String entry = dbHelper.getEntry(thisIntent.getStringExtra("email"),
                thisIntent.getStringExtra("timestamp"), key, db);

        String s="";
        try {

            // Decipher data
            s = AESCipher.decipher(key,entry);
        }catch (Exception e){
            e.printStackTrace();
        }

        // Display deciphered data
        textView.setText(s.toCharArray(),0,s.length());

        // Reopen database as the connection is reset by dbHelper.
        db = openOrCreateDatabase("ThoughtBubble.db",MODE_PRIVATE,null);

        // Event to go back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Event to delete entry
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent thisIntent = getIntent();
                if(dbHelper.deleteEntry(thisIntent.getStringExtra("timestamp"),db)){
                    Toast.makeText(getApplicationContext(),"Entry deleted",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }

}
