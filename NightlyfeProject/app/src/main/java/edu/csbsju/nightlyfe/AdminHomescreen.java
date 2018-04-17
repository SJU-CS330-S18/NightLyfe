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

/*
Class associated with viewing the homescreen of an admin account

 */
public class AdminHomescreen extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;

    /**
     * Initializes the admin homescreen
     * See appCompatActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homescreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = getIntent().getStringExtra("user");

        TextView mUsernameView = findViewById(R.id.userTxt);
        //System.out.println(user);
        mUsernameView.setText(user);

        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+user+"'",null);

        //Logs the user out and redirects to LoginActivity
        Button mLogout = (Button) findViewById(R.id.logoutBtn);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goToNextActivity);
            }
        });
    }
}
