package edu.csbsju.nightlyfe;

import android.content.Context;
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

        if(resultSet.getInt(2) != 2) {
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
        }

        Button claimR = (Button) findViewById(R.id.claimBtn);
        claimR.setTag(id);
        claimR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView claimText = (TextView) findViewById(R.id.claimTxt);
                int verifyClaim = Integer.valueOf(claimText.getText().toString());
                Cursor resultSet3 = mydatabase.rawQuery("Select * from business where id = "+id, null);
                resultSet3.moveToFirst();
                int restaurantOwn = resultSet3.getInt(8);
                if(verifyClaim == restaurantOwn){
                    Cursor resultSet4 = mydatabase.rawQuery("Update users set type = " + 4 + " where username = '"+user+"'", null);
                    resultSet4.moveToFirst();
                    Intent goToNextActivity = new Intent(getApplicationContext(), OwnerHomescreen.class);
                    startActivity(getIntent());
                    Context context = getApplicationContext();
                    Toast toastClaim = Toast.makeText(context,"Ownership Claimed Successfully", Toast.LENGTH_LONG);
                    toastClaim.show();
                }
            }
        });

    }

        /*Cursor resultSet3 = mydatabase.rawQuery("Select * from users where destination = "+id+" and NOT user = '"+user+"'",null);
        resultSet3.moveToFirst();

        TextView mEstimate = findViewById(R.id.attendanceTxt);
        mEstimate.setText(resultSet3.getCount());
*/

}