package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mydatabase = openOrCreateDatabase("NightLyfe", MODE_PRIVATE, null);
        user = getIntent().getStringExtra("username");
        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '" + user + "'", null);
        resultSet.moveToFirst();
        System.out.println(resultSet.getCount());
        //String name = resultSet.getString(1);
        TextView mUserHeader = findViewById(R.id.mUserHeader);
        //mUserHeader.setText(name);

        /*
        Button mChangeName = (Button) findViewById(R.id.changeNameBtn);
        mChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView mSubmitName = findViewById(R.id.fChangeName);
                changeName(mSubmitName.getText().toString());
            }
        });

        Button mChangeLocation = (Button) findViewById(R.id.changeLocationBtn);
        mChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView mSubmitLocation = findViewById(R.id.fChangeLocation);
                changeLocation(mSubmitLocation.getText().toString());
            }
        });

        Button mChangePassword = (Button) findViewById(R.id.changePasswordBtn);
        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView mSubmitPassword = findViewById(R.id.fChangePassword);
                changePassword(mSubmitPassword.getText().toString());
            }
        });
        */
    }
    /*
    private void changeName(String name) {
        Cursor resultSet = mydatabase.rawQuery("Update users set name = '" + name + "' where username = '"+user+"'",null);
        resultSet.moveToFirst();
    }
    private void changeLocation(String location) {
        Cursor resultSet = mydatabase.rawQuery("Update users set name = '" + location + "' where username = '"+user+"'",null);
        resultSet.moveToFirst();
    }
    private void changePassword(String password) {
        Cursor resultSet = mydatabase.rawQuery("Update users set password = '" + password + "' where username = '"+user+"'",null);
        resultSet.moveToFirst();
    }
    */
}