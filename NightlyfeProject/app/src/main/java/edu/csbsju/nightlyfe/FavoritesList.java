package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavoritesList extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        //opens nightlyfe database
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //receives active user's username from previous page
        user = getIntent().getStringExtra("user");


        //receives resultSet for all favorites associated with active user
        Cursor resultSet = mydatabase.rawQuery("Select * from businesses where name in (SELECT location from favorites where user = '"+user+"')",null);

        //gets size of resultset and moves the cursor to the first entry
        int size = resultSet.getCount();
        resultSet.moveToFirst();

        //finds linearlayout to display friends and creates LayoutParams object
        LinearLayout ll = (LinearLayout)findViewById(R.id.favLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //loops through each entry in favorites list and displays them
        for (int i = 0; i < size ; i++) {
            //gets username of entry in resultset
            String name = resultSet.getString(0);

            //creates and formats textview to display friend's username
            TextView mFavView = new TextView(this);
            mFavView.setTextSize(20);
            mFavView.setTextColor(Color.BLACK);

            //sets value of textview
            mFavView.setText(name);

            //adds the view to layout
            ll.addView(mFavView, lp);

            //moves cursor to the next entry in the resultset
            resultSet.moveToNext();
        }
    }
}

