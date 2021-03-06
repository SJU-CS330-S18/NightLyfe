package edu.csbsju.nightlyfe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

import android.database.sqlite.*;
import android.database.*;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.*;

public class OwnerHomescreen extends AppCompatActivity {

    String user;
    int id;
    SQLiteDatabase mydatabase;

    /**
     * Initializes the owner homescreen page
     * refer to appCompatActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_homescreen);
        user = getIntent().getStringExtra("user");

        TextView mUsernameView = findViewById(R.id.ownerTxt);
        //System.out.println(user);
        mUsernameView.setText(user);

        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+user+"'",null);
        resultSet.moveToFirst();
        id = resultSet.getInt(4);

        //Brings user back to LoginActivity page
        Button mLogout = (Button) findViewById(R.id.logoutBtn);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goToNextActivity);
            }
        });

        Cursor resultSet2 = mydatabase.rawQuery("Select * from business where id = "+id,null);
        resultSet2.moveToFirst();

        TextView mName = findViewById(R.id.establishmentTxt);
        mName.setText(resultSet2.getString(1));

        //gets the number of users who say they are attending the bar tonight, and adjusts the attendence number accordingly
        Cursor estimateResultSet = mydatabase.rawQuery("Select distinct username from users where destination = "+id+" and NOT username = '"+user+"'",null);
        estimateResultSet.moveToFirst();
        TextView mEstimate = findViewById(R.id.attendanceTxt);
        mEstimate.setText(estimateResultSet.getCount()+"");

        //Sends the owner to the owned restaurant's page
        Button mMyPage = (Button) findViewById(R.id.restaurantBtn);
        mMyPage.setTag(id);
        mMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Restaurant_Page.class);
                goToNextActivity.putExtra("key", (int) view.getTag());
                goToNextActivity.putExtra("user",user);
                startActivity(goToNextActivity); }
        });

/*
Code previously used for verification
 */
//        Button claimR = (Button) findViewById(R.id.claimBtn);
//        claimR.setTag(id)
//        claimR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView claimText = (TextView) findViewById(R.id.claimTxt);
//                int verifyClaim = Integer.valueOf(claimText.getText().toString());
//                Cursor resultSet3 = mydatabase.rawQuery("Select * from business where id = "+id, null);
//                resultSet3.moveToFirst();
 //               int restaurantOwn = resultSet3.getInt(8);
//                if(verifyClaim != restaurantOwn) {
//                    claimText.setError("Invalid owner ID");
//                }
//                else if(verifyClaim == restaurantOwn){
//                    Cursor resultSet4 = mydatabase.rawQuery("Update users set type = " + 4 + " where username = '"+user+"'", null);
//                    resultSet4.moveToFirst();
//                    Intent goToNextActivity = new Intent(getApplicationContext(), OwnerHomescreen.class);
//                    startActivity(getIntent());
//                    Context context = getApplicationContext();
//                    Toast toastClaim = Toast.makeText(context,"Ownership Claimed Successfully", Toast.LENGTH_LONG);
//                    toastClaim.show();
//                }
//            }
//        });

        RelativeLayout rl = findViewById(R.id.ownerLayout);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        if(resultSet.getInt(2) == 2){
            //Submits a request to admins for the owner to become a premium owner
            Button mPremium = new Button(this);
            mPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cursor updateType = mydatabase.rawQuery("Update users set type = " + 5 + " where username = '"+user+"'", null);
                    updateType.moveToFirst();
                    Intent goToNextActivity = new Intent(getApplicationContext(), OwnerHomescreen.class);
                    goToNextActivity.putExtra("user", user);
                    startActivity(goToNextActivity);
                    Context context = getApplicationContext();
                    Toast toastClaim = Toast.makeText(context,"Your request for premium ownership is being processed", Toast.LENGTH_LONG);
                    toastClaim.show();
                }
            });
            //sets the attributes of the search result name
            //mPremium.setTextSize(20);
            //mPremium.setTextColor(Color.BLACK);
            mPremium.setText("Become a Premium Member!");
            rp.addRule(RelativeLayout.BELOW, R.id.restaurantBtn);
            rl.addView(mPremium, rp);

        }

    }




}