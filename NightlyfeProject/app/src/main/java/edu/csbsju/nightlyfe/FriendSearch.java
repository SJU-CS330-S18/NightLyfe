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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button.*;

public class FriendSearch extends AppCompatActivity {

    private String search;
    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);

        //opens database for use
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //gets parameters passed to activity
        search = getIntent().getStringExtra("search");
        user = getIntent().getStringExtra("user");

        //Finds all friends associated with active user
        Cursor resultSet = mydatabase.rawQuery("Select * from users where (username LIKE '%"+search+"%' OR name LIKE '%"+search+"%') AND NOT username = '"+user+"'",null);

        //gets number of friends and moves the cursor to the first entry in ResultSet
        int size = resultSet.getCount();
        resultSet.moveToFirst();

        //pulls linearlayout for search results to appear, and creates a LayoutParams object to dictate entries
        LinearLayout ll = (LinearLayout)findViewById(R.id.searchLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //loops through every friend of the resulting search, adds their information and an add/remove button to page
        for (int i = 0; i < size ; i++) {
            //creates new horizonal LinearLayout for each entry into friendslist
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            //creates LayoutParams object to dictate entries
            LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            //retrieves name of next user found via search
            String name = resultSet.getString(3);
            String user2 = resultSet.getString(0);

            //creates a view to display the search result's name
            TextView mFriendView = new TextView(this);
            mFriendView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            //creates a button to either add or remove friend
            Button mAddFriend = new Button(this);

            //sets the associated friend's username as a tag associated with the button for inner class use
            mAddFriend.setTag(user2);
            //mAddFriend.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            //queries to determine if there is a friend entry with the two users
            Cursor resultSet2 = mydatabase.rawQuery("Select * from friends where user1 = '"+user+"' AND user2 = '"+user2+"'",null);

            //adds a remove button if the user is already a friend, and an add button if they are not
            if(resultSet2.getCount() == 0) {
                mAddFriend.setText("Add");
                mAddFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addFriend((String) view.getTag());
                    }
                });
            }
            else{
                mAddFriend.setText("Remove");
                mAddFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeFriend((String) view.getTag());
                    }
                });
            }

            //sets the attributes of the search result name
            mFriendView.setTextSize(20);
            mFriendView.setTextColor(Color.BLACK);
            mFriendView.setText(name);

            //adds the name and button to a linear layouts
            row.addView(mFriendView, rowp);
            row.addView(mAddFriend, rowp);
            ll.addView(row,lp);

            //moves the resultSet to the next entry
            resultSet.moveToNext();
        }
    }

    /*
    Method to create database connection between active user and a user associated with provided username parameter
    @param String username of added friend
     */
    private void addFriend(String friend){
        mydatabase.execSQL("INSERT INTO friends VALUES ('"+user+"', '"+friend+"', 1);");
        mydatabase.execSQL("INSERT INTO friends VALUES ('"+friend+"', '"+user+"', 1);");
        Intent goToNextActivity = new Intent(getApplicationContext(), FriendsList.class);
        goToNextActivity.putExtra("user", user);
        startActivity(goToNextActivity);
    }

    /*
    Method to remove database connection between active user and a user associated with provided username parameter
    @param String username of removed friend
     */
    private void removeFriend(String friend){
        mydatabase.execSQL("DELETE FROM friends WHERE user1 = '"+user+"' AND user2 =  '"+friend+"';");
        mydatabase.execSQL("DELETE FROM friends WHERE user1 = '"+friend+"' AND user2 =  '"+user+"';");
        Intent goToNextActivity = new Intent(getApplicationContext(), FriendsList.class);
        goToNextActivity.putExtra("user", user);
        startActivity(goToNextActivity);
    }
}
