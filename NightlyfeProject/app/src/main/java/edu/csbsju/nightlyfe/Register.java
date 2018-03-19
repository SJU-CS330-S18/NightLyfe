package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        mFnameView = (EditText) findViewById(R.id.fnameTxt);
        mLnameView = (EditText) findViewById(R.id.lnameTxt);
        mUsernameView = (EditText) findViewById(R.id.usernameTxt);
        mPasswordView = (EditText) findViewById(R.id.passwordTxt);

        Button mRegisterButton = (Button) findViewById(R.id.registerBtn);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerUser()){
                    Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                    goToNextActivity.putExtra("username", mUsernameView.getText().toString());
                    startActivity(goToNextActivity);
                }
            }
        });
    }

    public boolean registerUser(){

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String fname = mFnameView.getText().toString();
        String lname = mLnameView.getText().toString();


        //how to querey from the table
        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+username+"'",null);

        if (resultSet.getCount() != 0){
            mUsernameView.setError("This username has already been taken. Please choose another.");
            return false;
        }
        else{
            mydatabase.execSQL("INSERT INTO users VALUES ('"+username+"', '"+password+"', 1, '"+fname+" "+lname+"');");
            return true;
        }
    }
}
