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

/*
Class associated with viewing the group page of a specified group
@author Tom Richmond
 */
public class Group extends AppCompatActivity {

    //must still implement the group chat function
    
    //local sqllite db
    public SQLiteDatabase mydatabase;
    public int id;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //use activity_group layout
        setContentView(R.layout.activity_group);

        //opens database for use
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //gets parameters passed to activity
        id = getIntent().getIntExtra("id", 0);
        user = getIntent().getStringExtra("user");

        //return user home when they touch the home button
        Button mHome = (Button) findViewById(R.id.listBtn);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        //add members to group button
        Button mAdd = (Button) findViewById(R.id.addBtn);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), AddMembers.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("id", id);
                startActivity(goToNextActivity);
            }
        });

        //When user touches leave button, they are removed from the group
        Button mLeave = (Button) findViewById(R.id.leaveBtn);
        mLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydatabase.execSQL("DELETE FROM groupmember WHERE username = '"+user+"' AND groupID = "+id+";");
                Intent goToNextActivity = new Intent(getApplicationContext(), GroupsList.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        //button to bring user to the chat screen
        Button mChat = (Button) findViewById(R.id.chatBtn);
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), GroupChat.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("id", id);
                startActivity(goToNextActivity);
            }
        });

        //Finds all data associated with group ID, including name and all members
        Cursor resultSet = mydatabase.rawQuery("Select * from friendgroups where groupID = "+id,null);
        Cursor resultSet2 = mydatabase.rawQuery("Select * from groupmember where groupID = "+id,null);
        resultSet.moveToFirst();

        //finds linearlayout to display friends and creates LayoutParams object
        TextView mGroupName = (TextView)findViewById(R.id.groupNameTxt);
        mGroupName.setText(resultSet.getString(1));

        int size = resultSet2.getCount();
        resultSet2.moveToFirst();

        //finds linearlayout to display friends and creates LayoutParams object
        LinearLayout ll = (LinearLayout)findViewById(R.id.membersView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < size; i++){
            //gets username of entry in resultset
            String name = resultSet2.getString(1);

            //creates and formats textview to display friend's username
            TextView mMemberView = new TextView(this);
            mMemberView.setTextSize(20);
            mMemberView.setTextColor(Color.BLACK);

            //sets value of textview
            mMemberView.setText(name);

            //adds the view to layout
            ll.addView(mMemberView, lp);

            //moves cursor to the next entry in the resultset
            resultSet2.moveToNext();
        }
    }
}
