package com.thoughtbubble.tom.thoughtbubblejournal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * This class performs the menu operations by switching activities based on user events.
 */
public class Menu extends AppCompatActivity {

    // Database descriptors
    SQLiteDatabase db;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Open DB Connection
        db = openOrCreateDatabase("ThoughtBubble.db",MODE_PRIVATE,null);
        dbHelper = new DbHelper(this);
        dbHelper.onCreate(db);

        // Get context references
        final Button newentrybtn = (Button)findViewById(R.id.newentrybutn);
        Button readdiarybtn = (Button)findViewById(R.id.readbtn);
        Button logoutbtn = (Button)findViewById(R.id.logoutbtn);

        // Event to log out user
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
            }
        });

        // Event to create new entry
        newentrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newentryIntent =
                        new Intent(getApplicationContext(),CreateEntry.class);
                Intent thisIntent = getIntent();
                newentryIntent.putExtra("email",thisIntent.getStringExtra("email"));
                startActivity(newentryIntent);
            }
        });


        // Event to read entries
        readdiarybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = openOrCreateDatabase("ThoughtBubble.db",MODE_PRIVATE,null);
                if(dbHelper.getEntryList(getIntent().getStringExtra("email"),db) != null) {
                    Intent viewEntryIntent =
                            new Intent(getApplicationContext(), ViewEntries.class);
                    Intent thisIntent = getIntent();
                    viewEntryIntent.putExtra("email", thisIntent.getStringExtra("email"));
                    startActivity(viewEntryIntent);
                }else{
                    Toast.makeText(getApplicationContext(), "You have no diary entries",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
