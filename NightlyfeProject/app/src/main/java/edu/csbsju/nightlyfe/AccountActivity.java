package edu.csbsju.nightlyfe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);
        user = getIntent().getStringExtra("username");
        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+user+"'",null);
        resultSet.moveToFirst();
        System.out.println(resultSet.getCount());
        //String name = resultSet.getString(1);
        TextView mUserHeader = findViewById(R.id.mUserHeader);
        //mUserHeader.setText(name);
    }
}
