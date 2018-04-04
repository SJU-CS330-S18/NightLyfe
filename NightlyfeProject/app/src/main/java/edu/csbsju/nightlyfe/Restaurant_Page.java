
package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Restaurant_Page extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;
    public String key;
    public String busName;
    public String address;
    public String phone;
    public String hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);
        user = getIntent().getStringExtra("user");
        key = getIntent().getStringExtra("key");


        Button BulletinBtn = (Button) findViewById(R.id.BulletinBtn);
        BulletinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), BulletinBoard.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("key", key);
                startActivity(goToNextActivity);
            }
        });

        Button listBtn = (Button) findViewById(R.id.listBtn);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), RestaurantList.class);
                startActivity(goToNextActivity);
            }
        });

    Cursor resultSet = mydatabase.rawQuery("Select * from business where id = '" + key + "'", null);
    String busName = resultSet.getString(1);
    String address = resultSet.getString(3);
<<<<<<< HEAD
    String phone = resultSet.getString(6);
    String hours = resultSet.getString(7);

    TextView BusinessName = findViewById(R.id.BusinessName);
    BusinessName.setText(busName);

    TextView BusinessAddress = findViewById(R.id.BusinessAddress);
    BusinessAddress.setText(address);

    TextView BusinessPhone = findViewById(R.id.BusinessPhone);
    BusinessPhone.setText(phone);

    TextView BusinessHours = findViewById(R.id.BusinessHours);
    BusinessHours.setText(hours);

=======
    String phone = resultSet.getString(3);
    String hours = resultSet.getString(3);
>>>>>>> cfd3ce2392fa10a0b30ea74e12407bceee408921
    }
    //Buttons should be linked to created pages and a "back to search" button should be created
}
