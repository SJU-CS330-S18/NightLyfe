package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;


import android.database.sqlite.*;

import android.database.Cursor;

public class Homescreen extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        super.onCreate(savedInstanceState);
//            @Override
        setContentView(R.layout.activity_homescreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = getIntent().getStringExtra("user");

        TextView mUsernameView = findViewById(R.id.userTxt);
        mUsernameView.setText(user);

        //access user info from database from passed variable 'user'
        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+user+"'",null);
        resultSet.moveToFirst();

        TextView mDestinationView = findViewById(R.id.destinationTxt);

        Cursor resultSet2 = mydatabase.rawQuery("Select * from business where id = "+resultSet.getInt(4),null);
        resultSet2.moveToFirst();
        //System.out.println(resultSet2.getCount());

        int dest = resultSet.getInt(4);
        if(dest != 0) {
            mDestinationView.setText(resultSet2.getString(1));
        }

        Button mLogout = (Button) findViewById(R.id.logoutBtn);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goToNextActivity);
            }
        });

        Button mFriends = (Button) findViewById(R.id.friendsBtn);
        mFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), FriendsList.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        Button mBars = (Button) findViewById(R.id.barsBtn);
        mBars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), RestaurantList.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        Button mFavorites = (Button) findViewById(R.id.favoriteBtn);
        mFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), FavoritesList.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        Button mGroups = (Button) findViewById(R.id.groupsBtn);
        mGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), GroupsList.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        Button mUser = (Button) findViewById(R.id.userBtn);
        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), AccountActivity.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });
    }
}
