package com.thoughtbubble.tom.thoughtbubblejournal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class aids in storage of the user entries into the database.
 */
public class CreateEntry extends AppCompatActivity {

    // Database descriptors
    SQLiteDatabase db;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        // Initialize helper class
        dbHelper = new DbHelper(this);

        // Get Component references.
        Button saveentrybtn = (Button)findViewById(R.id.saveentrybtn);
        final EditText diaryEntry = (EditText)findViewById(R.id.diary_entry);

        // Save to database onClick action
        saveentrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent thisIntent = getIntent();

                // Open DB Connection
                db = openOrCreateDatabase("ThoughtBubble.db",MODE_PRIVATE,null);
                dbHelper.onCreate(db);

                // Get user entry
                String message = diaryEntry.getText().toString();

                // Get user's SHA-1 signature
                String key = dbHelper.getKey(thisIntent.getStringExtra("email"), db);
                String cipherText = "";
                try{

                    // Encrypt entry
                    cipherText = AESCipher.cipher(key,message);
                }catch(Exception e){
                    e.printStackTrace();
                }

                // Save entry on button click
                if(dbHelper.saveEntry(cipherText, thisIntent.getStringExtra("email"), db)) {
                    Toast.makeText(getApplicationContext(), "Diary Saved",
                            Toast.LENGTH_SHORT).show();
                 finish();
                }
            }
        });
    }

}
