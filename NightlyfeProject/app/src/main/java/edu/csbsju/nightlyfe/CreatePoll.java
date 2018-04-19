package edu.csbsju.nightlyfe;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
Class to create a poll
@author Andrew McIntyre
 */
public class CreatePoll extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public int id;
    public String user;
    int count = 0;
    int key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //use activity_group layout
        setContentView(R.layout.activity_create_poll);

        //opens database for use
        mydatabase = openOrCreateDatabase("NightLyfe", MODE_PRIVATE, null);

        //gets parameters passed to activity
        id = getIntent().getIntExtra("id", 0);
        user = getIntent().getStringExtra("user");

        Cursor resultSet = mydatabase.rawQuery("Select * from business", null);

        //Gets size of resultset and moves the cursor to the first entry
        int size = resultSet.getCount();
        resultSet.moveToFirst();


        //Finds linearlayout to display restaurants and creates LayoutParams object
        LinearLayout ll = (LinearLayout) findViewById(R.id.restaurantsView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mydatabase.execSQL("INSERT INTO polls VALUES ('"+id+"', '"+null+"', '"+null+"', '"+null+"', 0, 0, 0);");

        //Loops through each entry in friends list and displays them
        for (int i = 0; i < size; i++) {

            //Creates new horizonal LinearLayout for each entry into business
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            //Creates LayoutParams object to dictate entries
            LinearLayout.LayoutParams rowp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            //Gets username of entry in resultset
            String name = resultSet.getString(1);

            //Creates and formats textview to display friend's username
            TextView mBusinessView = new TextView(this);
            mBusinessView.setTextSize(20);
            mBusinessView.setTextColor(Color.BLACK);

            //Creates a button to visit a business
            Button mAddBusiness = new Button(this);

            //Gets the key relating to the business for a tag
            key = resultSet.getInt(0);

            //Sets the associated restaurant ID as a tag associated with the button for inner class use
            mAddBusiness.setTag(key);
            mAddBusiness.setText("Add to Poll");

            //Dynamic button to bring user to individual restaurant pages
            //Passes user ID and business key to individual page
            mAddBusiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count = count +1;
                    if(count<4){
                        addToPoll((int)view.getTag(), count);
                    }
                    else{
                        Context context = getApplicationContext();
                        Toast toastCreate = Toast.makeText(context,"Please select only 3 businesses", Toast.LENGTH_LONG);
                        toastCreate.show();
                    }
                }
            });

            //sets value of textview
            mBusinessView.setText(name);

            //adds the name and button to the linear layouts
            row.addView(mBusinessView, rowp);
            row.addView(mAddBusiness, rowp);
            ll.addView(row,lp);

            //moves cursor to the next entry in the resultset
            resultSet.moveToNext();
        }

        Button createPoll = (Button) findViewById(R.id.createBtn);
        createPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count>=3) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), Group.class);
                    goToNextActivity.putExtra("user", user);
                    goToNextActivity.putExtra("id", id);
                    startActivity(goToNextActivity);
                }
                else{
                    Context context = getApplicationContext();
                    Toast toastie = Toast.makeText(context, "Please select 3 businesses", Toast.LENGTH_LONG);
                    toastie.show();
                }
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor results = mydatabase.rawQuery("Select * from polls where groupID = " +id, null);
                results.moveToFirst();
                int rSize = results.getCount();
                if(rSize != 0) {
                    mydatabase.execSQL("DELETE FROM polls WHERE groupID = " + id + ";");
                }
                Intent goToNextActivity = new Intent(getApplicationContext(), Group.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("id", id);
                startActivity(goToNextActivity);
            }
        });
    }

    private void addToPoll(int rID, int count){
        Cursor resultSet = mydatabase.rawQuery("Update polls set option"+count+" = '" + rID + "' where groupID = '"+id+"'",null);
        resultSet.moveToFirst();
    }
}