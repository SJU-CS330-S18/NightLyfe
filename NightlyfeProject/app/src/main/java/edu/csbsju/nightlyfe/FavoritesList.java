package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class that handles all functionality for the favorites list (Viewing Favorites, Adding Favorites,
 * Removing Favorites, Visiting Items on Favorites)
 *
 * @author dannyfritz3
 */
public class FavoritesList extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        //opens nightlyfe database
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //receives active user's username from previous page
        user = getIntent().getStringExtra("user");


        //receives resultSet for all favorites associated with active user
        //Cursor resultSet = mydatabase.rawQuery("Select * from businesses where name in (SELECT location from favorites where user = '"+user+"')",null);
        Cursor resultSet = mydatabase.rawQuery("SELECT * FROM favorites WHERE user = '" + user + "';", null);
        //gets size of resultset and moves the cursor to the first entry
        int size = resultSet.getCount();
        resultSet.moveToFirst();

        //finds linearlayout to display friends and creates LayoutParams object
        LinearLayout ll = (LinearLayout)findViewById(R.id.favLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if(size == 0){
            TextView mMessage = new TextView(this);
            mMessage.setText("You have no favorites, \tvisit some restaurants to build your list!");
            mMessage.setGravity(Gravity.CENTER);
            ll.addView(mMessage, lp);
        }
        else {
            //loops through each entry in favorites list and displays them
            for (int i = 0; i < size; i++) {
                //gets username of entry in resultset
                final int barID = resultSet.getInt(1);

                Cursor selectBars = mydatabase.rawQuery("SELECT * FROM business WHERE id = " + barID + ";", null);
                selectBars.moveToFirst();
                //creates and formats textview to display friend's username
                TextView mFavView = new TextView(this);
                mFavView.setTextSize(30);
                mFavView.setTextColor(Color.BLACK);

                //sets value of textview
                final String busName = selectBars.getString(1);
                mFavView.setText(busName);
                mFavView.setGravity(View.TEXT_ALIGNMENT_CENTER);

                Button visitBar = new Button(this);
                visitBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent goToNextActivity = new Intent(getApplicationContext(), Restaurant_Page.class);
                        goToNextActivity.putExtra("user", user);
                        goToNextActivity.putExtra("key", barID);
                        startActivity(goToNextActivity);
                    }
                });

                Button removeBar = new Button(this);
                removeBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor removal = mydatabase.rawQuery("DELETE FROM favorites WHERE user = '" + user + "' AND locationID = " + barID + ";", null);
                        removal.moveToFirst();
                        Intent goToNextActivity = new Intent(getApplicationContext(), FavoritesList.class);
                        goToNextActivity.putExtra("user", user);
                        Toast toastName = Toast.makeText(getApplicationContext(), busName + " Removed from Favorites List", Toast.LENGTH_LONG);
                        toastName.show();
                        startActivity(goToNextActivity);
                    }
                });

                visitBar.setText("Visit " + busName + "'s Page");
                removeBar.setText("Remove " + busName + " From Favorites");
                removeBar.setTextColor(Color.RED);

                //adds the view to layout
                ll.addView(mFavView, lp);
                ll.addView(visitBar);
                ll.addView(removeBar);
                //moves cursor to the next entry in the resultset
                resultSet.moveToNext();
            }
        }

        Button mHome = (Button) findViewById(R.id.homeBtn);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });
    }
}

