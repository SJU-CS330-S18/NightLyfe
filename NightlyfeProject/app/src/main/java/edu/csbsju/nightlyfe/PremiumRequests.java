package edu.csbsju.nightlyfe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.database.sqlite.*;
import android.database.Cursor;

public class PremiumRequests extends AppCompatActivity {
    SQLiteDatabase mydatabase;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_requests);
        // Opens database connection
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);
        user = getIntent().getStringExtra("user");

        Cursor resultSet = mydatabase.rawQuery("Select u.username, b.name from users u, business b where type = "+5+" and u.destination = b.id;", null);
        resultSet.moveToFirst();
        int size = resultSet.getCount();

        LinearLayout mRequests = findViewById(R.id.requestsLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button mBackBtn = findViewById(R.id.backBtn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), AdminHomescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        if(size == 0){
            TextView message = new TextView(this);
            message.setText("There are no premium requests at this time");
            message.setGravity(Gravity.CENTER);
            mRequests.addView(message);
        }

        else {
            //loops through every owner of the resulting search, adds their information and an approve and disapprove button
            for (int i = 0; i < size; i++) {
                //creates new horizonal LinearLayout for each entry into premium requests
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);

                //creates LayoutParams object to dictate entries
                LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

                //retrieves name of next bar requesting a premium account
                String bar = resultSet.getString(1);
                //retrieves name of next owner requesting a premium account
                String name = resultSet.getString(0);

                //creates a view to display the search result's name
                TextView mNameView = new TextView(this);
                mNameView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                //creates a button to either approve or deny request
                Button mApproveBtn = new Button(this);
                Button mDenyBtn = new Button(this);

                mApproveBtn.setText("Approve");
                mDenyBtn.setText("Deny");

                //sets the username as the tag for both approve and deny buttons
                mApproveBtn.setTag(name);
                mDenyBtn.setTag(name);

                mApproveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor updateType = mydatabase.rawQuery("Update users set type = " + 4 + " where username = '"+view.getTag()+"'", null);
                        updateType.moveToFirst();
                        Intent goToNextActivity = new Intent(getApplicationContext(), PremiumRequests.class);
                        goToNextActivity.putExtra("user", user);
                        startActivity(goToNextActivity);
                        Context context = getApplicationContext();
                        Toast toastClaim = Toast.makeText(context,"Approved for premium ownership", Toast.LENGTH_LONG);
                        toastClaim.show();
                    }
                });

                mDenyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor updateType = mydatabase.rawQuery("Update users set type = " + 2 + " where username = '"+view.getTag()+"'", null);
                        updateType.moveToFirst();
                        Intent goToNextActivity = new Intent(getApplicationContext(), PremiumRequests.class);
                        goToNextActivity.putExtra("user", user);
                        startActivity(goToNextActivity);
                        Context context = getApplicationContext();
                        Toast toastClaim = Toast.makeText(context,"Denied premium ownership", Toast.LENGTH_LONG);
                        toastClaim.show();
                    }
                });


                //sets the attributes of the search result name
                mNameView.setTextSize(20);
                mNameView.setTextColor(Color.BLACK);
                mNameView.setText(bar);

                //adds the name and button to a linear layouts
                row.addView(mNameView, rowp);
                row.addView(mApproveBtn, rowp);
                row.addView(mDenyBtn, rowp);
                mRequests.addView(row, lp);

                //moves the resultSet to the next entry
                resultSet.moveToNext();
            }
        }
    }

    /*
    Method to approve the requested premium account and make database changes accordingly
     */
    public void approveRequest(String user){

    }

    /*
    Method to deny the requested premium account and make database changes accordingly
     */
    public void denyRequest(String user){

    }
}
