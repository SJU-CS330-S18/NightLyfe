package edu.csbsju.nightlyfe;
/*
AdminReview class handles functionality allowing administrators permission to delete inappropriate reviews

@author Grant Salk
 */
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AdminReview extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;
    public String business;
    public String aUser;
    public int aBusiness;

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
                Intent goToNextActivity = new Intent(getApplicationContext(), AdminHomescreen.class);
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

        //System.out.println("Created linear business layout");

        //Loops through each entry in business list and displays them
        for (int i = 0; i < size; i++) {
            //System.out.println("In outer for loop");


            //Gets username of entry in resultset
            String name = resultSet.getString(1);

            //Gets the key relating to the business for a tag
            int key = resultSet.getInt(0);

            Cursor resultSet2 = mydatabase.rawQuery("Select * from REVIEWS where ID = " + key + ";", null);
            int reviewSize = resultSet2.getCount();

            resultSet2.moveToFirst();

            //System.out.println(reviewSize);

            //creates and formats textview to display businesses
            TextView businessView = new TextView(this);
            businessView.setTextSize(30);
            businessView.setTextColor(Color.BLACK);


            //sets text of textview
            businessView.setText(name + " Reviews");
            businessView.setGravity(Gravity.CENTER);
            //businessView.setBackgroundColor(getResources().getColor(R.color.colorAccentOrange));
            businessView.setPaintFlags(businessView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


            //adds the view to layout
            ll.addView(businessView);


            LinearLayout businessLayout = findViewById(R.id.businessLayout);


            for (int j = 0; j < reviewSize; j++) {
                //System.out.println("In inner for loop");

                TextView mName = new TextView(this);
                TextView mReview = new TextView(this);
                Button btnDelete = new Button(this);

                String username = resultSet2.getString(0);
                mName.setText(username);
                mName.setTextSize(15);

                aUser = username;

                mReview.setText("\t\t\t" + resultSet2.getString(3));
                mReview.setTextColor(Color.BLACK);
                mReview.setTextSize(20);
                mReview.setTypeface(null, Typeface.ITALIC);

                //LinearLayout adminReviewWindow = new LinearLayout(this);
                //ScrollView sv = new ScrollView(this);
                //adminReviewWindow.setOrientation(LinearLayout.VERTICAL);
                //sv.addView(businessLayout);

                businessLayout.addView(mName);
                businessLayout.addView(mReview);

                btnDelete.setText("Delete Review");
                btnDelete.setTextColor(getResources().getColor(R.color.dangerRed));
                btnDelete.setTag(j);
                businessLayout.addView(btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteReview();
                    }
                });

                resultSet2.moveToNext();
            }
            aBusiness = i;
            TextView emptyReview = new TextView(this);

            if (reviewSize == 0)
            {
                emptyReview.setText(" Sorry, there are currently no reviews for this business");
                emptyReview.setTextColor(Color.BLACK);
                emptyReview.setTextSize(15);
                emptyReview.setTypeface(null, Typeface.ITALIC);
                emptyReview.setTextColor(getResources().getColor(R.color.colorAccentOldOrange));
                businessLayout.addView(emptyReview);
            }
            //moves cursor to the next entry in the resultset
            resultSet.moveToNext();
        }
    }

    private void deleteReview() {
        Cursor deletion = mydatabase.rawQuery("DELETE FROM reviews WHERE username = '" + aUser + "' AND id = " + aBusiness + ";", null);
        System.out.println(aUser);
        System.out.println(aBusiness);
        deletion.moveToFirst();
        Intent goToNextActivity = new Intent(getApplicationContext(), AdminReview.class);
        goToNextActivity.putExtra("user", user);
        startActivity(goToNextActivity);
    }
}

