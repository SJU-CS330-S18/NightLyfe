package edu.csbsju.nightlyfe;

import android.database.sqlite.*;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.*;
import android.widget.LinearLayout.*;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.*;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class FriendsList extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        //opens nightlyfe database
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //receives active user's username from previous page
        user = getIntent().getStringExtra("user");

        //creates listener for search button, which redirects to search page
        Button mSearch = (Button) findViewById(R.id.searchBtn);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mSearchTerm = (TextView) findViewById(R.id.searchTxt);
                String searchText =  mSearchTerm.getText().toString();
                Intent goToNextActivity = new Intent(getApplicationContext(), FriendSearch.class);
                goToNextActivity.putExtra("search",searchText);
                goToNextActivity.putExtra("user",user);
                startActivity(goToNextActivity);
            }
        });

        Button mHome = (Button) findViewById(R.id.homeBtn);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        //receives resultSet for all friends associated with active user
        Cursor resultSet = mydatabase.rawQuery("Select * from friends where user1 = '"+user+"'",null);

        //gets size of resultset and moves the cursor to the first entry
        int size = resultSet.getCount();
        resultSet.moveToFirst();

        //finds linearlayout to display friends and creates LayoutParams object
        LinearLayout ll = (LinearLayout)findViewById(R.id.friendsLayout);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        //loops through each entry in friends list and displays them
        for (int i = 0; i < size ; i++) {
            //gets username of entry in resultset
            String name = resultSet.getString(1);

            //creates and formats textview to display friend's username
            TextView mFriendView = new TextView(this);
            mFriendView.setTextSize(20);
            mFriendView.setTextColor(Color.BLACK);

            //sets value of textview
            mFriendView.setText(name);

            //adds the view to layout
            ll.addView(mFriendView, lp);

            //moves cursor to the next entry in the resultset
            resultSet.moveToNext();
        }
    }
}
