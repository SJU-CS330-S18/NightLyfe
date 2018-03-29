
package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Restaurant_Page extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);
        user = getIntent().getStringExtra("user");

        Button BulletinBtn = (Button) findViewById(R.id.PhotosBtn);
        BulletinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), BulletinBoard.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

    }
    //Buttons should be linked to created pages and a "back to search" button should be created
}
