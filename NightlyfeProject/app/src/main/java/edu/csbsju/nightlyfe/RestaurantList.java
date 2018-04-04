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

public class RestaurantList extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;
    public String business;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_restaurant_list);

            user = getIntent().getStringExtra("user");
            mydatabase = openOrCreateDatabase("NightLyfe", MODE_PRIVATE, null);

        Button mHome = (Button) findViewById(R.id.listBtn);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        //receives resultSet for all businesses associated with active user--will be location in future
       Cursor resultSet = mydatabase.rawQuery("Select * from business", null);

        //gets size of resultset and moves the cursor to the first entry
        int size = resultSet.getCount();
        resultSet.moveToFirst();



        //finds linearlayout to display restaurants and creates LayoutParams object
        LinearLayout ll = (LinearLayout) findViewById(R.id.restLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //loops through each entry in friends list and displays them
        for (int i = 0; i < size; i++) {

            //creates new horizonal LinearLayout for each entry into friendslist
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            //creates LayoutParams object to dictate entries
            LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            //gets username of entry in resultset
            String name = resultSet.getString(1);

            //creates and formats textview to display friend's username
            TextView mBusinessView = new TextView(this);
            mBusinessView.setTextSize(20);
            mBusinessView.setTextColor(Color.BLACK);

            //creates a button to visit a business
            Button mVisitBusiness = new Button(this);

            //gets the key relating to the business for a tag
            int key = resultSet.getInt(0);

            //sets the associated restaurant ID as a tag associated with the button for inner class use
            mVisitBusiness.setTag(key);
            mVisitBusiness.setText("Visit Page");

            mVisitBusiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), Restaurant_Page.class);
                    goToNextActivity.putExtra("key", (int) view.getTag());
                    startActivity(goToNextActivity);
                }
            });

            //sets value of textview
            mBusinessView.setText(name);

            //adds the view to layout
           // ll.addView(mBusinessView, lp);

            //adds the name and button to a linear layouts
            row.addView(mBusinessView, rowp);
            row.addView(mVisitBusiness, rowp);
            ll.addView(row,lp);

            //moves cursor to the next entry in the resultset
            resultSet.moveToNext();
        }

    }




}
