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

        //how to create a database
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);
        user = getIntent().getStringExtra("username");

        Button mSearch = (Button) findViewById(R.id.searchBtn);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mSearchTerm = (TextView) findViewById(R.id.searchTxt);
                String searchText =  mSearchTerm.getText().toString();
                Intent goToNextActivity = new Intent(getApplicationContext(), FriendSearch.class);
                goToNextActivity.putExtra("search",searchText);
                startActivity(goToNextActivity);
            }
        });

        Cursor resultSet = mydatabase.rawQuery("Select * from friends where user1 = '"+user+"'",null);

        int size = resultSet.getCount();
        resultSet.moveToFirst();

        LinearLayout ll = (LinearLayout)findViewById(R.id.friendsLayout);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < size ; i++) {
            String name = resultSet.getString(1);
            TextView mFriendView = new TextView(this);
            mFriendView.setTextSize(20);
            mFriendView.setTextColor(Color.BLACK);
            mFriendView.setText(name);
            ll.addView(mFriendView, lp);
            resultSet.moveToNext();
        }
    }
}
