package edu.csbsju.nightlyfe;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_homescreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = getIntent().getStringExtra("user");


        TextView mUsernameView = findViewById(R.id.ownerTxt);
        //System.out.println(user);
        mUsernameView.setText(user);

        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+user+"'",null);
        resultSet.moveToFirst();
        id = resultSet.getInt(4);

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

        Button mMyPage = (Button) findViewById(R.id.restaurantBtn);
        mMyPage.setTag(id);
        mMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Restaurant_Page.class);
                goToNextActivity.putExtra("id", (int) view.getTag());
                startActivity(goToNextActivity);
            }
        });

        LinearLayout ll = findViewById(R.id.buttonLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if(resultSet.getInt(2) == 2){
            Button mPremium = new Button(this);
            mPremium.setText("Become a Premium account!");
            ll.addView(mPremium,lp);

            mPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //dummy click listener to register as a premium account
                    Cursor resultSet = mydatabase.rawQuery("Update users set type = " + 4 + " where username = '"+user+"'",null);
                    resultSet.moveToFirst();
                    startActivity(getIntent());
                }
            });
        }
    }
}