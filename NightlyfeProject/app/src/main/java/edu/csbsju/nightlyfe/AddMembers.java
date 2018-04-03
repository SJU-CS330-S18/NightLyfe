package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddMembers extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;
    public int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        //opens database for use
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //gets parameters passed to activity
        user = getIntent().getStringExtra("user");
        id = getIntent().getIntExtra("id", 0);

        Button mHome = (Button) findViewById(R.id.listBtn);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        //Finds all friends associated with active user
        Cursor resultSet = mydatabase.rawQuery("Select f.user2 from friends f where f.user1 = '"+user+"' AND NOT EXISTS (Select * from groupmember g where g.groupID = "+id+" AND g.username = f.user2)",null);

        //gets number of friends and moves the cursor to the first entry in ResultSet
        int size = resultSet.getCount();
        System.out.println(size);
        resultSet.moveToFirst();

        //pulls linearlayout for search results to appear, and creates a LayoutParams object to dictate entries
        LinearLayout ll = (LinearLayout)findViewById(R.id.addLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //loops through every friend of the resulting search, adds their information and an add/remove button to page
        for (int i = 0; i < size ; i++) {
            //creates new horizonal LinearLayout for each entry into friendslist
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            //creates LayoutParams object to dictate entries
            LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            //retrieves name of next user found via search
            String username = resultSet.getString(0);

            //creates a view to display the search result's name
            TextView mFriendView = new TextView(this);
            mFriendView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //sets the attributes of the search result name
            mFriendView.setTextSize(20);
            mFriendView.setTextColor(Color.BLACK);
            mFriendView.setText(username);

            //creates a button to either add or remove friend
            Button mAddFriend = new Button(this);

            //sets the associated friend's username as a tag associated with the button for inner class use
            mAddFriend.setTag(username);

            //adds an add button for friends not in group
            mAddFriend.setText("Add");
            mAddFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addFriend((String) view.getTag());
                    }
                });

            //adds the name and button to a linear layouts
            row.addView(mFriendView, rowp);
            row.addView(mAddFriend, rowp);
            ll.addView(row,lp);

            //moves the resultSet to the next entry
            resultSet.moveToNext();
        }
    }
    public void addFriend(String username){
        mydatabase.execSQL("INSERT INTO groupmember VALUES ("+id+", '"+username+"');");

        Intent goToNextActivity = new Intent(getApplicationContext(), Group.class);
        goToNextActivity.putExtra("user", user);
        goToNextActivity.putExtra("id", id);
        startActivity(goToNextActivity);
    }
}
