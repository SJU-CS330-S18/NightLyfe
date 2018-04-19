package edu.csbsju.nightlyfe;

import android.content.Context;
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
import android.widget.Toast;

/*
Class to view and vote on polls
@author Andrew McIntyre
 */
public class Poll extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public int id;
    public String user;
    int count = 0;
    int key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //use activity_group layout
        setContentView(R.layout.activity_poll);

        //opens database for use
        mydatabase = openOrCreateDatabase("NightLyfe", MODE_PRIVATE, null);

        //gets parameters passed to activity
        id = getIntent().getIntExtra("id", 0);
        user = getIntent().getStringExtra("user");

        Cursor resultSet = mydatabase.rawQuery("Select * from polls where groupID = '"+id+"'", null);

        //Gets size of resultset and moves the cursor to the first entry
        resultSet.moveToFirst();


        //Finds linearlayout to display restaurants and creates LayoutParams object
        LinearLayout ll = (LinearLayout) findViewById(R.id.restaurantsView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //Loops through each entry in friends list and displays them
        Cursor resultSet2 = mydatabase.rawQuery("Select * from groupmember where groupID = '"+id+"' AND username = '"+user+"'", null);
        resultSet2.moveToFirst();
        int hasVoted = resultSet2.getInt(2);

        if(hasVoted == 0) {
            for (int i = 0; i < 3; i++) {

                //Creates new horizonal LinearLayout for each entry into friendslist
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);

                //Creates LayoutParams object to dictate entries
                LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                count++;
                //Gets username of entry in resultset
                int rID = resultSet.getInt(count);
                Cursor resultSet3 = mydatabase.rawQuery("Select * from business where id = '"+rID+"'", null);
                resultSet3.moveToFirst();
                String name = resultSet3.getString(1);

                //Creates and formats textview to display friend's username
                TextView mOptionsView = new TextView(this);
                mOptionsView.setTextSize(20);
                mOptionsView.setTextColor(Color.BLACK);

                //Creates a button to visit a business
                Button voteBtn = new Button(this);
                //Gets the key relating to the business for a tag
                key = resultSet.getInt(0);

                //Sets the associated restaurant ID as a tag associated with the button for inner class use
                voteBtn.setTag(R.id.key, key);
                voteBtn.setTag(R.id.count, count);
                voteBtn.setText("Vote");

                //Dynamic button to bring user to individual restaurant pages
                //Passes user ID and business key to individual page
                voteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor resultSet = mydatabase.rawQuery("Update polls set votes" + view.getTag(R.id.count) + " = votes" + view.getTag(R.id.count) + " +1 where groupID = '" + id + "'", null);
                        resultSet.moveToFirst();
                        Cursor resultSet2 = mydatabase.rawQuery("Update groupmember set voted = 1  where groupID = '"+id+"' AND username = '"+user+"'",null);
                        resultSet2.moveToFirst();
                        Intent goToNextActivity = new Intent(getApplicationContext(), Poll.class);
                        goToNextActivity.putExtra("user", user);
                        goToNextActivity.putExtra("id", id);
                        startActivity(goToNextActivity);
                    }
                });

                //sets value of textview
                mOptionsView.setText(name);

                //adds the name and button to the linear layouts
                row.addView(mOptionsView, rowp);
                row.addView(voteBtn, rowp);
                ll.addView(row, lp);

            }
        }
        else{
            count =0;
            for (int i = 0; i < 3; i++) {

                //Creates new horizonal LinearLayout for each entry into friendslist
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);

                //Creates LayoutParams object to dictate entries
                LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

                count++;
                //Gets username of entry in resultset
                int votes = resultSet.getInt(count+3);
                int rID = resultSet.getInt(count);
                Cursor resultSet3 = mydatabase.rawQuery("Select * from business where id = '"+rID+"'", null);
                resultSet3.moveToFirst();
                String name1 = resultSet3.getString(1);

                //Creates and formats textview to display friend's username
                TextView mOptionsView = new TextView(this);
                mOptionsView.setTextSize(20);
                mOptionsView.setTextColor(Color.BLACK);

                TextView mResultsView = new TextView(this);
                mResultsView.setTextSize(15);
                mResultsView.setTextColor(Color.BLACK);

                //sets value of textview
                mOptionsView.setText(name1);
                mResultsView.setText(""+votes+"");

                //adds the name and button to the linear layouts
                row.addView(mOptionsView, rowp);
                row.addView(mResultsView, rowp);
                ll.addView(row, lp);
            }
        }

        Button mBack = (Button) findViewById(R.id.backBtn);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Group.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("id", id);
                startActivity(goToNextActivity);
            }
        });
    }
}