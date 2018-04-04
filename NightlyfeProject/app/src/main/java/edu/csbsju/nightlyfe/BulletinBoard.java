package edu.csbsju.nightlyfe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BulletinBoard extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public String user;
    public int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_board);
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);
        user = getIntent().getStringExtra("user");
        key = Integer.parseInt(getIntent().getStringExtra("key"));
        Cursor resultSet = mydatabase.rawQuery("Select * from specials where id = "+key, null);
        Cursor bNameList = mydatabase.rawQuery("Select * from business where id = "+key,null);
        int size = resultSet.getCount();
        bNameList.moveToFirst();
        if(bNameList.getCount()>=1){
            TextView bName = findViewById(R.id.BusinessName);
            bName.setText(bNameList.getString(1));
        }
        if(size>0) {
            resultSet.moveToLast();
            TextView first = findViewById(R.id.eventTextTop);
            first.setText(resultSet.getString(1));
        }
        if(size>1) {
            resultSet.moveToPrevious();
            TextView middle = findViewById(R.id.eventTextMiddle);
            middle.setText(resultSet.getString(1));
        }
        if(size>2) {
            resultSet.moveToPrevious();
            TextView bottom = findViewById(R.id.eventTextBottom);
            bottom.setText(resultSet.getString(1));
        }





    }
}
