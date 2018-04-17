package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
Class associated with viewing the page to create a new group
@author Tom Richmond
 */
public class NewGroupPage extends AppCompatActivity {
    String user;
    SQLiteDatabase mydatabase;
    EditText mGroupName;

    /**
     * Initializes the new group page
     * See appCompatActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group_page);

        //opens database for use
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        user = getIntent().getStringExtra("user");

        //Terminates the create new group process, redirects to GroupsList
        Button mCancel = (Button) findViewById(R.id.cancelBtn);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), GroupsList.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        mGroupName = (EditText) findViewById(R.id.groupNameTxt);

        //Creates a new group
        Button mConfirm = (Button) findViewById(R.id.confirmBtn);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });
    }

    /**
     * Creates group by retrieving information from text input fields, stores group in database table friendgroups
     */
    public void createGroup() {

        //Retrieves group name from tex input box
        String groupName = mGroupName.getText().toString();
        if (groupName.length() > 20 || groupName.length() <=2){
            mGroupName.setError("Group name must be between 3 and 20 characters.");
            return;
        }
        else{
            //receives resultSet for all friends associated with active user
            Cursor resultSet = mydatabase.rawQuery("Select DISTINCT g.groupID from friendgroups g",null);
            resultSet.moveToFirst();

            int id = resultSet.getCount()+1;
            mydatabase.execSQL("INSERT INTO friendgroups VALUES ("+id+", '"+groupName+"');");
            mydatabase.execSQL("INSERT INTO groupmember VALUES ("+id+", '"+user+"');");
            
            //Redirects to GroupsList page
            Intent goToNextActivity = new Intent(getApplicationContext(), GroupsList.class);
            goToNextActivity.putExtra("user", user);
            startActivity(goToNextActivity);
        }
    }
}
