package com.thoughtbubble.tom.thoughtbubblejournal;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ViewEntries extends ListActivity {

    // Database descriptors
    SQLiteDatabase db;
    DbHelper dbHelper;

    String entries[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open database connection
        db = openOrCreateDatabase("ThoughtBubble.db",MODE_PRIVATE,null);
        dbHelper = new DbHelper(this);
        dbHelper.onCreate(db);

        Intent thisIntent = getIntent();

        // Get entry list
        entries = dbHelper.getEntryList(thisIntent.getStringExtra("email"),db);
        List<String> stringList = Arrays.asList(entries);

        //Sorting list in decending order
        Collections.reverse(stringList);
        entries = stringList.toArray(entries);
        String[] display = new String[entries.length];
        // Formatting date values to display as list
        for(int i = 0; i < entries.length; i++){
            display[i] = convertTimeStamp2DisplayTime(entries[i]);
        }

        // Add entries to list adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, display);
        setListAdapter(adapter);
    }

    /**
     * This method converts string timestamp to a displayable time stamp.
     * @param ts - TimeStamp as String
     * @return - Returns a String of formatted timestamp
     */
    public static String convertTimeStamp2DisplayTime(String ts){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMM dd, ''yyy 'at' hh:mm:ss a ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String displayTime = null;
        try{
            Date date = dateFormat.parse(ts);
            long d = date.getTime() - (4 * 60 * 60 * 1000) - 3000;
            displayTime = sdf.format(d);
        }catch (Exception e){
            e.printStackTrace();
        }

        return displayTime;
    }

    /**
     * This method is called when an item in the list view is clicked to make a selection
     * @param l - ListView object
     * @param v - View object
     * @param position - position of the clicked item
     * @param id - item id.
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent readEntryIntent =
                new Intent(getApplicationContext(),ReadEntry.class);
        Intent thisIntent = getIntent();
        readEntryIntent.putExtra("email",thisIntent.getStringExtra("email"));
        readEntryIntent.putExtra("timestamp",entries[position]);
        startActivity(readEntryIntent);
        finish();
    }
}
