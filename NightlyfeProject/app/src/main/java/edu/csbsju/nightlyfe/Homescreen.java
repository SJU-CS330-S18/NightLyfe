package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;


import android.database.sqlite.*;
import android.database.*;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

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

        user = getIntent().getStringExtra("username");

        TextView mUsernameView = findViewById(R.id.userTxt);
        //System.out.println(user);
        mUsernameView.setText(user);

        //access user info from database from passed variable 'user'
        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+user+"'",null);

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
                startActivity(goToNextActivity);
            }
        });

        Button mBars = (Button) findViewById(R.id.barsBtn);
        mBars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goToNextActivity);
            }
        });

        Button mFavorites = (Button) findViewById(R.id.favoriteBtn);
        mFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), FriendsList.class);
                startActivity(goToNextActivity);
            }
        });
    }
}
