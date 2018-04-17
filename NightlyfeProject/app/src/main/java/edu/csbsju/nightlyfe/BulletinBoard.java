package edu.csbsju.nightlyfe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BulletinBoard extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public String user;
    public int key;
    public String name;
    public String firstD;
    public String middleD;
    public String lastD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_board);
        //Getting the database....
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);
        //Getting the user who is using the app
        user = getIntent().getStringExtra("user");
        //Getting the key of the bar or restaurant id
        key = getIntent().getIntExtra("key", -1);
        //Query database for the specials where the business ID matches the key from earlier
        Cursor resultSet = mydatabase.rawQuery("Select * from specials where businessID = "+key, null);
        int size = resultSet.getCount();
        name = getIntent().getStringExtra("name");
        //Sets the business name so the page can be dynamic for each different place.
        TextView bName = findViewById(R.id.BusinessName);
        bName.setText(name);
        System.out.println(size);

        //This creates a semi dynamic list of specials that are the most Frequent / newest posted.
        //Functions similar to a twitter page
        //If there is less than 3 posts, only shows the number of posts. I.E if there is 2 there
        //  would only be two posts shown
        //Currently has a hard limit of three posts per page
        if(size>0) {
            resultSet.moveToLast();
            TextView first = findViewById(R.id.eventTextTop);
            first.setText(resultSet.getString(2));
            TextView firstDate = findViewById(R.id.topDate);
            firstD = resultSet.getString(3);
            firstDate.setText(firstD);
            TextView middle = findViewById(R.id.eventTextMiddle);
            middle.setText("");
            TextView bottom = findViewById(R.id.eventTextBottom);
            bottom.setText("");
        }
        if(size>1) {
            resultSet.moveToPosition(size-2);
            TextView middle = findViewById(R.id.eventTextMiddle);
            middle.setText(resultSet.getString(2));
            TextView middleDate = findViewById(R.id.middleDate);
            middleD = resultSet.getString(3);
            middleDate.setText(middleD);
        }
        if(size>2) {
            resultSet.moveToPosition(size-3);
            TextView bottom = findViewById(R.id.eventTextBottom);
            bottom.setText(resultSet.getString(2));
            TextView bottomDate = findViewById(R.id.bottomDate);
            lastD = resultSet.getString(3);
            bottomDate.setText(lastD);
        }





    }
}
