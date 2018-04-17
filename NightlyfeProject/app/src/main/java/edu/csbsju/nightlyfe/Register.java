package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    private EditText mFnameView;
    private EditText mLnameView;
    private EditText mUsernameView;
    private EditText mPasswordView;

    public SQLiteDatabase mydatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //how to create a database
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Finds the field associated with first name
        mFnameView = (EditText) findViewById(R.id.fnameTxt);
        //Finds the field associated with last name
        mLnameView = (EditText) findViewById(R.id.lnameTxt);
        //Finds the field associated with user name
        mUsernameView = (EditText) findViewById(R.id.usernameTxt);
        //Finds the field associated with password
        mPasswordView = (EditText) findViewById(R.id.passwordTxt);

        //Button associated with submitting your information for account registration
        Button mRegisterButton = (Button) findViewById(R.id.registerBtn);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerUser()){
                    Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                    goToNextActivity.putExtra("user", mUsernameView.getText().toString());
                    startActivity(goToNextActivity);
                }
            }
        });
    }

    /*
    method used to register a user to the app and associate it with the database
    @returns boolean representing whether the user was successfully added to the db
     */
    public boolean registerUser(){

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String fname = mFnameView.getText().toString();
        String lname = mLnameView.getText().toString();


        //how to query from the table
        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+username+"'",null);

        //if the username is already found
        if (resultSet.getCount() != 0){
            mUsernameView.setError("This username has already been taken. Please choose another.");
            return false;
        }
        //if the password field is empty
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError("This field is required.");
            return false;
        }
        //if the fname field is empty
        if(TextUtils.isEmpty(fname)) {
            mFnameView.setError("This field is required.");
            return false;
        }
        //if the lname field is empty
        if(TextUtils.isEmpty(lname)) {
            mLnameView.setError("This field is required.");
            return false;
        }
        //if the username field is empty
        if(TextUtils.isEmpty(username)) {
            mUsernameView.setError("This field is required.");
            return false;
        }
        //else, insert information into database
        else{
            mydatabase.execSQL("INSERT INTO users VALUES ('"+username+"', '"+password+"', 1, '"+fname+" "+lname+"', 0);");
            return true;
        }
    }
}
