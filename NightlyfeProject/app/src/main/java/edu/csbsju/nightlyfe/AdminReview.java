package edu.csbsju.nightlyfe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class AdminReview extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;
    public String business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_review);

        //Get user variable from recent page
        user = getIntent().getStringExtra("user");

        //Opens database used for NightLyfe application
        mydatabase = openOrCreateDatabase("NightLyfe", MODE_PRIVATE, null);


        //Creates a home button to return to the user home screen
        Button homeBtn = (Button) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        //Receives resultSet for all businesses associated with active user--will be location in future
        Cursor resultSet = mydatabase.rawQuery("Select * from business", null);

        //Gets size of resultset and moves the cursor to the first entry
        int size = resultSet.getCount();
        resultSet.moveToFirst();

        //Finds linearlayout to display restaurants and creates LayoutParams object
        LinearLayout ll = (LinearLayout) findViewById(R.id.businessLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //Loops through each entry in friends list and displays them
        for (int i = 0; i < size; i++) {

            //Creates new horizonal LinearLayout for each entry into friendslist
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            //Creates LayoutParams object to dictate entries
            LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            //Gets username of entry in resultset
            String name = resultSet.getString(1);

            //Creates and formats textview to display friend's username
            TextView mBusinessView = new TextView(this);
            mBusinessView.setTextSize(20);
            mBusinessView.setTextColor(Color.BLACK);
            //mBusinessView.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 2));

            //Creates a button to visit a business
            Button mVisitBusiness = new Button(this);

            //Gets the key relating to the business for a tag
            int key = resultSet.getInt(0);

            //Sets the associated restaurant ID as a tag associated with the button for inner class use
            mVisitBusiness.setTag(key);
            mVisitBusiness.setText("Visit Page");
            //mVisitBusiness.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));

            //Dynamic button to bring user to individual restaurant pages
            //Passes user ID and business key to individual page
            mVisitBusiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), Restaurant_Page.class);
                    goToNextActivity.putExtra("user", user);
                    goToNextActivity.putExtra("key", (int) view.getTag());
                    startActivity(goToNextActivity);
                }
            });

            //sets value of textview
            mBusinessView.setText(name);

            //adds the name and button to the linear layouts
            row.addView(mBusinessView, rowp);
            row.addView(mVisitBusiness, rowp);
            ll.addView(row, lp);

            //moves cursor to the next entry in the resultset
            resultSet.moveToNext();

        }
    }
}
