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

    }
}
