package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GroupChat extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public int id;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        //opens database for use
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //gets parameters passed to activity
        id = getIntent().getIntExtra("id", 0);
        user = getIntent().getStringExtra("user");

        Cursor resultSet = mydatabase.rawQuery("Select * from friendgroups where groupID = "+id,null);

        if(resultSet.getCount() == 0){
            return;
        }

        Button mBack = (Button) findViewById(R.id.backBtn);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Group.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("id", id);
                startActivity(goToNextActivity);
            }
        });

        resultSet.moveToFirst();

        //finds linearlayout to display friends and creates LayoutParams object
        TextView mGroupName = (TextView)findViewById(R.id.nameTxt);
        mGroupName.setText(resultSet.getString(1));
    }
}
