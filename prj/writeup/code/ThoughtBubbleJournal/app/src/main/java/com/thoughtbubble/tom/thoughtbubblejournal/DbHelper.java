package com.thoughtbubble.tom.thoughtbubblejournal;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;


/**
 * This class handles all the database operations for the SQLite database.
 */
public class DbHelper extends SQLiteOpenHelper {

    // SQL statements
    String createUserTable = "CREATE TABLE IF NOT EXISTS USER(\n" +
            "email VARCHAR(40) PRIMARY KEY,\n " +
            "passcode VARCHAR(25)\n" +
            ");";

    String createEntryTable = "CREATE TABLE IF NOT EXISTS ENTRIES(\n" +
            "email VARCHAR(40) REFERENCES USER(email) ON UPDATE CASCADE,\n" +
            "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
            "entry TEXT\n" +
            ");";

    public DbHelper(Context context){
        super(context, "ThoughtBubble.db", null, 1);
    }

    /**
     * Creates DB structure
     * @param db - SQLiteDatabase Connection object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserTable);
        db.execSQL(createEntryTable);
    }

    /**
     * This method registers new users
     * @param db - SQLiteDatabase Connection object
     * @param email - user email
     * @param passcode - user passcode
     * @return - Returns true if user is successfully registered.
     */
    protected  boolean registerUser(SQLiteDatabase db, String email,String passcode){
        ContentValues values = new ContentValues();

        String cols[] = {"email"};
        Cursor cursor = db.query("USER",cols,"email = '" + email + "'",
                null,null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            db.close();
            return false;
        }

        StringBuffer hash = new StringBuffer();
        try{

            // Get SHA-1 Hash
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(passcode.getBytes());
            byte[] digest = md.digest();

            for (int i=0;i<digest.length;i++) {
                String hex=Integer.toHexString(0xff & digest[i]);
                if(hex.length()==1) hash.append('0');
                hash.append(hex);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        values.put("email", email);
        values.put("passcode",hash.toString() );
        db.insert("USER",null,values);
        db.close();
        return true;
    }

    /**
     * This method authenticates the user by the SHA-1 signatures
     * @param email - user email
     * @param passcode - user passcode
     * @param db - SQLiteDatabase Connection object
     * @return - Returns true if the user is authenticated
     */
    protected boolean authenticateUser(String email, String passcode,SQLiteDatabase db){

        String auth = "SELECT passcode " +
                "FROM USER " +
                "WHERE email = '" + email + "';";
        String cols[] = {"passcode"};
        Cursor cursor = db.query("USER",cols,"email = '" + email + "'",
                null,null,null,null,null);
        try {

            // Get SHA-1 signature
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(passcode.getBytes());
            byte[] digest = md.digest();
            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xff & digest[i]);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }

            cursor.moveToFirst();

            //Compare signatures
            if(cursor.getString(cursor.getColumnIndex("passcode")).equals(hash.toString())){
                db.close();
                return true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        db.close();
        return false;
    }

    /**
     * This method saves the entries into the database
     * @param entryEncrypted - The AES-256 encrypted entry
     * @param email - user email
     * @param db - SQLiteDatabase Connection object
     * @return
     */
    protected boolean saveEntry(String entryEncrypted, String email,SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("entry",entryEncrypted);
        if(db.insert("ENTRIES",null,values) > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    /**
     * This method gets the user's SHA-1 signature for the passcode
     * @param email - user email
     * @param db - SQLiteDatabase Connection object
     * @return - Returns SHA-1 signature
     */
    protected String getKey(String email,SQLiteDatabase db){
        String auth = "SELECT passcode " +
                "FROM USER " +
                "WHERE email = '" + email + "';";
        String cols[] = {"passcode"};
        Cursor cursor = db.query("USER",cols,"email = '" + email + "'",
                null,null,null,null,null);
        cursor.moveToFirst();
        String key = cursor.getString(cursor.getColumnIndex("passcode"));
        db.close();
        return key;
    }

    /**
     * This method returns all the entries associated with the email address.
     * @param email - user email
     * @param db - SQLiteDatabase Connection object
     * @return - Returns a list of entries associated with the email address
     */
    protected String[] getEntryList(String email, SQLiteDatabase db){
        String getEntries = "SELECT timestamp \n" +
                "FROM ENTRIES \n" +
                "WHERE email = '" + email + "';";
        String cols[] = {"timestamp"};
        Cursor cursor = db.query("ENTRIES",cols, "email = '" + email + "'",
                null,null,null,null,null);

        String entries[] = new String[cursor.getCount()];
        int i = 0;
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                entries[i] = cursor.getString(cursor.getColumnIndex("timestamp"));
                i++;
            } while (cursor.moveToNext());
            db.close();
            return entries;
        }
        db.close();
        return null;

    }

    /**
     * This method returns the entry associated with the email and the timestamp
     * @param email - user email
     * @param timestamp - timestamp clicked from the list
     * @param key - user passcode
     * @param db - SQLiteDatabase Connection object
     * @return - Returns an encrypted entry assoicated with email and timestamp.
     */
    protected String getEntry(String email, String timestamp, String key, SQLiteDatabase db){
        String query = "SELECT entry FROM ENTRIES WHERE email = '"+ email + "' AND timestamp = '" + timestamp +"'";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        String cipherText = cursor.getString(cursor.getColumnIndex("entry"));

        db.close();
        return cipherText;
    }

    /**
     * This method deletes the entries associated with the email and passcode from the database.
     * @param timestamp - timestamp of the entry
     * @param db - SQLiteDatabase Connection object
     * @return - Returns true if successfully deleted.
     */
    protected  boolean deleteEntry(String timestamp, SQLiteDatabase db){
        if(db.delete("ENTRIES","timestamp = '" + timestamp + "'",null) > 0){
            return true;
        }
        db.close();
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

