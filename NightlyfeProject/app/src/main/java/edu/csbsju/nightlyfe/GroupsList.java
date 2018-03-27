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

public class GroupsList extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);

        user = getIntent().getStringExtra("user");
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        Button mCreate = (Button) findViewById(R.id.createBtn);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), NewGroupPage.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        Button mHome = (Button) findViewById(R.id.homeBtn);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        //receives resultSet for all friends associated with active user
        Cursor resultSet = mydatabase.rawQuery("Select DISTINCT g.groupName, g.groupID from friendgroups g, groupmember m where m.username = '"+user+"' AND g.groupID = m.groupID",null);

        //gets size of resultset and moves the cursor to the first entry
        int size = resultSet.getCount();
        resultSet.moveToFirst();

        //finds linearlayout to display friends and creates LayoutParams object
        LinearLayout ll = (LinearLayout)findViewById(R.id.groupsLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //loops through each entry in friends list and displays them
        for (int i = 0; i < size ; i++) {

            //creates new horizonal LinearLayout for each entry into friendslist
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            //creates LayoutParams object to dictate entries
            LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            //gets username of entry in resultset
            String name = resultSet.getString(0);
            String id = resultSet.getString(1);

            //creates a button to either add or remove friend
            Button mVisitGroup = new Button(this);

            //sets the associated group's ID as a tag associated with the button for inner class use
            mVisitGroup.setTag(id);
            mVisitGroup.setText("Visit");

            mVisitGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), Group.class);
                    goToNextActivity.putExtra("id", Integer.parseInt((String) view.getTag()));
                    goToNextActivity.putExtra("user", user);
                    startActivity(goToNextActivity);
                }
            });

            //creates and formats textview to display friend's username
            TextView mGroupView = new TextView(this);
            mGroupView.setTextSize(20);
            mGroupView.setTextColor(Color.BLACK);

            //sets value of textview
            mGroupView.setText(name);


            //adds the name and button to a linear layouts
            row.addView(mGroupView, rowp);
            row.addView(mVisitGroup, rowp);
            ll.addView(row,lp);

            //moves the resultSet to the next entry
            resultSet.moveToNext();
        }
    }
}