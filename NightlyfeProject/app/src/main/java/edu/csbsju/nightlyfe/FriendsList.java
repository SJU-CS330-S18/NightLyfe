package edu.csbsju.nightlyfe;

/*
Class associated with viewing the friends list page of a given user
 */

import android.database.sqlite.*;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.widget.*;
import android.widget.LinearLayout.*;

import android.support.v7.app.AppCompatActivity;
import android.content.*;

import android.os.Bundle;
import android.view.View;

public class FriendsList extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        //opens nightlyfe database
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //receives active user's username from previous page
        user = getIntent().getStringExtra("user");

        //button associated with searching for a term in the user database
        Button mSearch = (Button) findViewById(R.id.searchBtn);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //retrieves the field for the search term
                TextView mSearchTerm = (TextView) findViewById(R.id.searchTxt);
                String searchText =  mSearchTerm.getText().toString();
                Intent goToNextActivity = new Intent(getApplicationContext(), FriendSearch.class);
                //passes search term to next activity
                goToNextActivity.putExtra("search",searchText);
                //passes user session variable to next activity
                goToNextActivity.putExtra("user",user);
                startActivity(goToNextActivity);
            }
        });

        //button associated with the ability to return home from the friends list
        Button mHome = (Button) findViewById(R.id.listBtn);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        //receives resultSet for all friends associated with active user
        Cursor resultSet = mydatabase.rawQuery("Select * from friends where user1 = '"+user+"'",null);

        //gets size of resultset and moves the cursor to the first entry
        int size = resultSet.getCount();
        resultSet.moveToFirst();

        //finds linearlayout to display friends and creates LayoutParams object
        LinearLayout ll = (LinearLayout)findViewById(R.id.friendsLayout);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        //loops through each entry in friends list and displays them
        for (int i = 0; i < size ; i++) {
            //gets username of entry in resultset
            String name = resultSet.getString(1);

            //creates and formats textview to display friend's username
            TextView mFriendView = new TextView(this);
            mFriendView.setTextSize(20);
            mFriendView.setTextColor(Color.BLACK);

            //sets value of textview
            mFriendView.setText(name);

            //adds the view to layout
            ll.addView(mFriendView, lp);

            //moves cursor to the next entry in the resultset
            resultSet.moveToNext();
        }
    }
}
