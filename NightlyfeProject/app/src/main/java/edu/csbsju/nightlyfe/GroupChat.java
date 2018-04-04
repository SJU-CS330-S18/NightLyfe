package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class
GroupChat extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public int id;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        //opens database for use
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //gets parameters passed to activity
        id = getIntent().getIntExtra("id", 0);
        user = getIntent().getStringExtra("user");

        ScrollView mScroll = findViewById(R.id.scrollView);
        mScroll.scrollTo(0, mScroll.getBottom());

        Cursor resultSet = mydatabase.rawQuery("Select * from friendgroups where groupID = "+id,null);

        if(resultSet.getCount() == 0){
            return;
        }

        Cursor resultSetComments = mydatabase.rawQuery("Select * from groupmessage where groupID = "+id+" ORDER BY time ASC",null);
        resultSetComments.moveToFirst();
        LinearLayout mChatWindow = findViewById(R.id.chatWindow);

        if(resultSetComments.getCount() == 0){
            TextView mMessage = new TextView(this);
            mMessage.setText("No message history, start a conversation!");
            mMessage.setGravity(Gravity.CENTER);
            mChatWindow.addView(mMessage);
        }
        else{
            int size = resultSetComments.getCount();
            for(int i = 0; i < size; i++){
                TextView mUser = new TextView(this);
                TextView mMessage = new TextView(this);
                String username = resultSetComments.getString(1);
                mUser.setText(username+":");
                if(username.equals(user)){
                    mUser.setTextColor(Color.RED);
                }
                mMessage.setText("\t\t\t"+resultSetComments.getString(3));
                mMessage.setTextColor(Color.BLACK);
                mChatWindow.addView(mUser);
                mChatWindow.addView(mMessage);
                resultSetComments.moveToNext();
            }
        }

        Button mBack = (Button) findViewById(R.id.backBtn);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Group.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("id", id);
                startActivity(goToNextActivity);
            }
        });

        Button mSend = (Button) findViewById(R.id.sendBtn);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mComment = findViewById(R.id.editText);
                String comment = mComment.getText().toString();
                boolean sent = sendMessage(comment);

                if (sent) {
                    //Intent goToNextActivity = new Intent(getApplicationContext(), GroupChat.class);
                    //goToNextActivity.putExtra("user", user);
                    //goToNextActivity.putExtra("id", id);
                    //startActivity(goToNextActivity);
                    startActivity(getIntent());
                }
                else{
                    mComment.setError("Message must be between 0 and 100 character");
                }
            }
        });

        resultSet.moveToFirst();

        //finds linearlayout to display friends and creates LayoutParams object
        TextView mGroupName = (TextView)findViewById(R.id.nameTxt);
        mGroupName.setText(resultSet.getString(1));
    }

    public boolean sendMessage(String message){
        if(message.length()>100 || message.length() <= 0){
            return false;
        }
        mydatabase.execSQL("INSERT INTO groupmessage VALUES ("+id+", '"+user+"', strftime('%s','now'), '"+message+"');");
        return true;
    }
}