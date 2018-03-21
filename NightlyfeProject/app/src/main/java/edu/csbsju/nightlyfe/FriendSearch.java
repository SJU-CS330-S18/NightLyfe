package edu.csbsju.nightlyfe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+search+"'",null);

        int size = resultSet.getCount();
        resultSet.moveToFirst();

        //pulls linearlayout for search results to appear
        LinearLayout ll = (LinearLayout)findViewById(R.id.searchLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < size ; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            //retrieves name of next user found via search
            String name = resultSet.getString(3);
            String user2 = resultSet.getString(0);

            //creates a view to display the search result's name
            TextView mFriendView = new TextView(this);

            mFriendView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            //creates a button to either add or remove friend
            Button mAddFriend = new Button(this);
            //mAddFriend.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            Cursor resultSet2 = mydatabase.rawQuery("Select * from friends where user1 = '"+user+"' AND user2 = '"+user2+"'",null);

            //checks if the given user is already a friend or not
            if(resultSet2.getCount() == 0) {
                mAddFriend.setText("Add");
            }
            else{
                mAddFriend.setText("Remove");
            }

            //sets the attributes of the search result name
            mFriendView.setTextSize(20);
            mFriendView.setTextColor(Color.BLACK);
            mFriendView.setText(name);

            //adds the name and button to a linear layout
            row.addView(mFriendView, rowp);
            row.addView(mAddFriend, rowp);
            ll.addView(row,lp);
            resultSet.moveToNext();
        }
    }
}
