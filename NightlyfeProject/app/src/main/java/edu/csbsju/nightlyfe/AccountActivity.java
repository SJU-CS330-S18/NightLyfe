package edu.csbsju.nightlyfe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

/**
 * A class that handles all Account functionality (View Account, Edit Account{Edit Name, Edit Location,
 * Edit Password}, Delete Account*)
 *
 * @author dannyfritz3
 */
public class AccountActivity extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //opens the NightLyfe SQLiteDatabase for use
        mydatabase = openOrCreateDatabase("NightLyfe", MODE_PRIVATE, null);

        //pulls user session variable from last activity
        user = getIntent().getStringExtra("user");

        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '" + user + "'", null);
        resultSet.moveToFirst();
        System.out.println(resultSet.getCount());
        String name = resultSet.getString(3);

        TextView mUserHeader = findViewById(R.id.mUserHeader);
        mUserHeader.setText("Hello, " + name + "!");

        AutoCompleteTextView cName = findViewById(R.id.fChangeName);
        //AutoCompleteTextView cLocation = findViewById(R.id.fChangeLocation);
        AutoCompleteTextView cPass = findViewById(R.id.fChangePassword);

        cName.setHint(resultSet.getString(3));
        //cLocation.setHint(resultSet.getString(4));
        cPass.setHint(resultSet.getString(1));

        //Button to submit changes made to name variable
        Button mChangeName = (Button) findViewById(R.id.changeNameBtn);
        mChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView mSubmitName = findViewById(R.id.fChangeName);
                changeName(mSubmitName.getText().toString());
                Intent goToNextActivity = new Intent(getApplicationContext(), AccountActivity.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
                Context context = getApplicationContext();
                Toast toastName = Toast.makeText(context,"Name Changed Successfully", Toast.LENGTH_LONG);
                toastName.show();
            }
        });

        //Button to submit changes made to location variable
        Button mChangeLocation = (Button) findViewById(R.id.changeLocationBtn);
        mChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView mSubmitLocation = findViewById(R.id.fChangeLocation);
                changeLocation(mSubmitLocation.getText().toString());
                Intent goToNextActivity = new Intent(getApplicationContext(), AccountActivity.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
                Context context = getApplicationContext();
                Toast toastLocation = Toast.makeText(context,"Name Location Successfully", Toast.LENGTH_LONG);
                toastLocation.show();
            }
        });

        //Button to submit changes made to password variable
        Button mChangePassword = (Button) findViewById(R.id.changePasswordBtn);
        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView mSubmitPassword = findViewById(R.id.fChangePassword);
                changePassword(mSubmitPassword.getText().toString());
                Intent goToNextActivity = new Intent(getApplicationContext(), AccountActivity.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
                Context context = getApplicationContext();
                Toast toastPassword = Toast.makeText(context,"Password Changed Successfully", Toast.LENGTH_LONG);
                toastPassword.show();
            }
        });

        //Button associated with the ability to return to the home screen
        Button mHome = (Button) findViewById(R.id.homeBtn);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        deleteAccount();
                        Toast toastPassword = Toast.makeText(getApplicationContext(),"Deleted: " + user, Toast.LENGTH_LONG);
                        toastPassword.show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        //do nothing
                        break;
                }
            }
        };

        Button mDelete = (Button) findViewById(R.id.deleteBtn);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setMessage("Are you sure you want to delete your account?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    /*
    method to change the name of a user within the database
    @param String name value to change name variable in database
     */
    private void changeName(String name) {
        Cursor resultSet = mydatabase.rawQuery("Update users set name = '" + name + "' where username = '"+user+"'",null);
        resultSet.moveToFirst();
    }

    /*
    method to change the location of a user within the database
    @param String location value to change location variable in database
     */
    private void changeLocation(String location) {
        Cursor resultSet = mydatabase.rawQuery("Update users set name = '" + location + "' where username = '"+user+"'",null);
        resultSet.moveToFirst();
    }

    /*
    method to change the password of a user within the database
    @param String password value to change password variable in database
     */
    private void changePassword(String password) {
        Cursor resultSet = mydatabase.rawQuery("Update users set password = '" + password + "' where username = '"+user+"'",null);
        resultSet.moveToFirst();
    }

    private void deleteAccount() {
        Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
        Cursor resultSet = mydatabase.rawQuery("DELETE FROM users WHERE username = '" + user + "';", null);
        resultSet.moveToFirst();
        startActivity(goToNextActivity);
    }
}