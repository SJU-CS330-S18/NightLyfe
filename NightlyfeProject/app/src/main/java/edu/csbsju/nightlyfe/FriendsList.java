package edu.csbsju.nightlyfe;

/*
Class associated with viewing the friends list page of a given user
@author Tom Richmond
 */

import android.database.sqlite.*;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
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
        ImageButton mSearch = (ImageButton) findViewById(R.id.searchBtn);
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

        //button associated with the ability to return home from the friends list
        Button mMap = (Button) findViewById(R.id.mapBtn);
        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), FriendsMap.class);
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
            //creates new horizonal LinearLayout for each entry into friendslist
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            //creates LayoutParams object to dictate entries
            LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            //gets username of entry in resultset
            String name = resultSet.getString(1);

            //creates and formats textview to display friend's username
            TextView mFriendView = new TextView(this);
            mFriendView.setGravity(Gravity.CENTER_VERTICAL);
            mFriendView.setTextSize(20);
            mFriendView.setTextColor(Color.BLACK);

            //sets value of textview
            mFriendView.setText(name);

            //adds the view to layout
            row.addView(mFriendView, rowp);

            Cursor resultSet2 = mydatabase.rawQuery("Select * from users where username = '"+name+"'",null);

            resultSet2.moveToFirst();

            if (resultSet2.getInt(4) != 0){
                ImageButton mMyFriend = new ImageButton(this);
                mMyFriend.setImageResource(android.R.drawable.ic_menu_compass);
                mMyFriend.setBackgroundColor(getResources().getColor(R.color.transparent));

                mMyFriend.setTag(resultSet2.getString(0));

                mMyFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent goToNextActivity = new Intent(getApplicationContext(), MyFriendMap.class);
                        goToNextActivity.putExtra("user", user);
                        goToNextActivity.putExtra("friend", (String) view.getTag());
                        startActivity(goToNextActivity);
                    }
                });
                //adds the view to layout
                row.addView(mMyFriend, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            //adds the new row to the layout
            ll.addView(row, lp);

            //moves cursor to the next entry in the resultset
            resultSet.moveToNext();
        }
    }
}
