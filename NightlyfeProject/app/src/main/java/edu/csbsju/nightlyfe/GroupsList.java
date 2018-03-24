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
            //gets username of entry in resultset
            String name = resultSet.getString(0);
            String id = resultSet.getString(1);

            //creates and formats textview to display friend's username
            TextView mGroupView = new TextView(this);
            mGroupView.setTextSize(20);
            mGroupView.setTextColor(Color.BLACK);

            //sets value of textview
            mGroupView.setText(name);

            //adds the view to layout
            ll.addView(mGroupView, lp);

            //moves cursor to the next entry in the resultset
            resultSet.moveToNext();
        }

    }
}
