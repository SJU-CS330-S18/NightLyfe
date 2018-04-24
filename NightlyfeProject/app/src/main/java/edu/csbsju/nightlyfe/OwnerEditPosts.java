package edu.csbsju.nightlyfe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class OwnerEditPosts extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public String post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_edit_posts);
        mydatabase = openOrCreateDatabase("NightLyfe", MODE_PRIVATE, null);
        post = getIntent().getStringExtra("post");
        
    }
}
