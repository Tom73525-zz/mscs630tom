package com.thoughtbubble.tom.thoughtbubblejournal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class performs the sign up operation for the user
 */
public class SignUp extends AppCompatActivity {

    // Database descriptors
    SQLiteDatabase db;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initilize DB Helper
        dbHelper = new DbHelper(this);

        // Get context references
        Button signup = findViewById(R.id.signup_btn);

        // Event to sign in
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Open database connection
                db = openOrCreateDatabase("ThoughtBubble.db",MODE_PRIVATE,null);
                dbHelper.onCreate(db);

                // Get context references
                EditText email = findViewById(R.id.s_email);
                EditText pwd1 = findViewById(R.id.s_pwd1);
                EditText pwd2 = findViewById(R.id.s_pwd2);

                String emailId = email.getText().toString();
                String pass1 = pwd1.getText().toString();
                String pass2 = pwd2.getText().toString();
                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

                // Validate email
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(emailId);
                if(!matcher.matches()){
                    Toast.makeText(getApplicationContext(),"Email not valid",
                            Toast.LENGTH_SHORT).show();
                }
                else if(!pass1.equals(pass2)){
                    // Validate passwords
                    Toast.makeText(getApplicationContext(),"Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                }
                else {

                    // Register user
                    if(dbHelper.registerUser(db, emailId,pass1)) {
                        Toast.makeText(getApplicationContext(), "You are now Signed Up",
                                Toast.LENGTH_SHORT).show();
                        Intent loginIntent =
                                new Intent(getApplicationContext(),Login.class);
                        startActivity(loginIntent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Email already exists, try logging in",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}
