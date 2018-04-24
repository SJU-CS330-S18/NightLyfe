package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class OwnerBulletinBoard extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;
    public int key;
    public String name;
    Cursor resultSet;
    ArrayList<String> postsWithDate = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_bulletin_board);
        //Establish connection to the database
        mydatabase = openOrCreateDatabase("NightLyfe", MODE_PRIVATE, null);
        //Getting the user who is using the app
        user = getIntent().getStringExtra("user");
        //Getting the key of the bar or restaurant id
        key = getIntent().getIntExtra("key", -1);
        //Query database for the specials where the business ID matches the key from earlier
        resultSet = mydatabase.rawQuery("Select * from specials where businessID = " + key, null);
        int size = resultSet.getCount();
        name = getIntent().getStringExtra("name");
        //Sets the business name so the page can be dynamic for each different place.
        TextView bName = findViewById(R.id.ownerBusinessName);
        bName.setText(name);
        postsWithDate = buildArray(resultSet);
        final ListView ownerPosts = (ListView)findViewById(R.id.ownerListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, postsWithDate);
        ownerPosts.setAdapter(adapter);
        //Starting listener for clicks on postsWithDate
        ownerPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToNextActivity = new Intent(getApplicationContext(), OwnerEditPosts.class);
                String clickedPost = postsWithDate.get(position);
                goToNextActivity.putExtra("post", clickedPost);
                startActivity(goToNextActivity);
            }
        });

    }
    public ArrayList<String> buildArray(Cursor list){
        //Here begins to fill the List View with all the items in to resultset from the querey of specials
        ArrayList<String> output = new ArrayList<String>();
        int size = list.getCount();
        int temp = 0;
        list.moveToFirst();
        for(temp=0; temp<=size; temp++) {
            output.add(list.getString(2)+"\n"+"Date: "+list.getString(3));
            temp++;
            list.moveToPosition(temp);
        }
        Collections.reverse(output);
        return(output);

    }
}

