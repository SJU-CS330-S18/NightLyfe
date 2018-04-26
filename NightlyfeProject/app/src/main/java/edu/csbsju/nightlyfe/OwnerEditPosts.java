package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

public class OwnerEditPosts extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public String post;
    public int id;
    public String user;
    public String name;
    public int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_edit_posts);
        mydatabase = openOrCreateDatabase("NightLyfe", MODE_PRIVATE, null);
        post = getIntent().getStringExtra("post");
        id = getIntent().getIntExtra("id",-1);
        user = getIntent().getStringExtra("user");
        name = getIntent().getStringExtra("name");
        key = getIntent().getIntExtra("key",-1);
        final EditText editPost = findViewById(R.id.editablePost);
        editPost.setText(post);

        Button update = findViewById(R.id.editButton);
        if(id != -1){
        update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String updated = editPost.getText().toString();
                    if(updated.equals("")){
                        mydatabase.execSQL("DELETE FROM specials WHERE sID = "+id+"");
                    }
                    else {
                        mydatabase.execSQL("UPDATE specials SET special = '" + updated + "' WHERE sID = " + id + "");
                    }
                    Intent goToNextActivity = new Intent(getApplicationContext(), OwnerBulletinBoard.class);
                    goToNextActivity.putExtra("user", user);
                    goToNextActivity.putExtra("name", name);
                    goToNextActivity.putExtra("key", key);
                    startActivity(goToNextActivity);
                }
            });
        }

        Button delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydatabase.execSQL("DELETE FROM specials WHERE sID = "+id+"");
                Intent goToNextActivity = new Intent(getApplicationContext(), OwnerBulletinBoard.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("name", name);
                goToNextActivity.putExtra("key", key);
                startActivity(goToNextActivity);
            }
        });

    }
}
