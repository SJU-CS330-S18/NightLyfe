package edu.csbsju.nightlyfe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendSearch extends AppCompatActivity {

    private String search;
    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);

        //opens database for use
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //gets parameters passed to activity
        search = getIntent().getStringExtra("search");
        user = getIntent().getStringExtra("user");

        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+search+"'",null);

        int size = resultSet.getCount();
        resultSet.moveToFirst();

        LinearLayout ll = (LinearLayout)findViewById(R.id.searchLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < size ; i++) {
            String name = resultSet.getString(3);
            TextView mFriendView = new TextView(this);
            mFriendView.setTextSize(20);
            mFriendView.setTextColor(Color.BLACK);
            mFriendView.setText(name);
            ll.addView(mFriendView, lp);
            resultSet.moveToNext();
        }
    }
}
