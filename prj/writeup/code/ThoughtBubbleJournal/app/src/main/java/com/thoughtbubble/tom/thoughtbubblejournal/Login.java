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
 * This class is used to perform login operations.
 */
public class Login extends AppCompatActivity {

    // Database descriptors
    SQLiteDatabase db;
    DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }


        // Initializing DB helper
        dbHelper = new DbHelper(this);

        // Get component references
        Button loginbtn = (Button) findViewById(R.id.login_button);
        Button signup = (Button)findViewById(R.id.l_signup_btn);

        // Go to sign up activity when clicked on sign up
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent =
                        new Intent(getApplicationContext(),SignUp.class);
                startActivity(signupIntent);
                finish();
            }
        });

        // Login User
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Open database connection
                db = openOrCreateDatabase("ThoughtBubble.db",MODE_PRIVATE,null);
                dbHelper.onCreate(db);

                // Get context references
                EditText email = (EditText)findViewById(R.id.l_email);
                EditText pass = findViewById(R.id.l_pwd);

                // Authenticate user
                if(dbHelper.authenticateUser(email.getText().toString(),
                        pass.getText().toString(),db)){
                    Intent menuIntent =
                            new Intent(getApplicationContext(),Menu.class);
                    menuIntent.putExtra("email",email.getText().toString());
                    startActivity(menuIntent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid email or password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
