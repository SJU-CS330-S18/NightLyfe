
package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

public class Restaurant_Page extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public String user;
    public int key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);
        user = getIntent().getStringExtra("user");
        key = getIntent().getIntExtra("key", -1);

        //System.out.println(key);

        Cursor resultSet = mydatabase.rawQuery("Select * from business where id = " + key, null);
        resultSet.moveToFirst();
        Cursor userResultSet = mydatabase.rawQuery("Select * from users where username = '" + user +"'", null);
        userResultSet.moveToFirst();
        final String busName = resultSet.getString(1);
        String address = resultSet.getString(3);

        String phone = resultSet.getString(6);
        String hours = resultSet.getString(7);

        Button MapsBtn = (Button) findViewById(R.id.MapsBtn);
        MapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), BusinessLocation.class);
                goToNextActivity.putExtra("name", busName);
                goToNextActivity.putExtra("key", key);
                startActivity(goToNextActivity);
            }
        });

        Button BulletinBtn = (Button) findViewById(R.id.BulletinBtn);
        BulletinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), BulletinBoard.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("name", busName);
                goToNextActivity.putExtra("key", key);
                startActivity(goToNextActivity);
            }
        });

        Button favoritesBtn = (Button) findViewById(R.id.FavoritesBtn);
        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor checkFavorites = mydatabase.rawQuery("SELECT * FROM favorites WHERE user = '" + user + "' AND locationID = " + key + ";", null);
                checkFavorites.moveToFirst();
                if(checkFavorites.getCount() == 0) {
                    Cursor resultSet = mydatabase.rawQuery("INSERT INTO favorites VALUES ('" + user + "', " + key + ");", null);
                    resultSet.moveToFirst();
                    Toast toastName = Toast.makeText(getApplicationContext(), busName + " was added to your Favorites List", Toast.LENGTH_LONG);
                    toastName.show();
                } else {
                    Toast toastName = Toast.makeText(getApplicationContext(), busName + " has already been added to your Favorites List", Toast.LENGTH_LONG);
                    toastName.show();
                }
            }
        });

        Button listBtn = (Button) findViewById(R.id.listBtn);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), RestaurantList.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });

        ImageButton mStar = findViewById(R.id.starBtn);
        if(userResultSet.getInt(4) == key){
            mStar.setImageResource(R.drawable.goldstarbutton);
        }
        mStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDestination();
            }
        });


    TextView BusinessName = findViewById(R.id.BusinessName);
    BusinessName.setText(busName);

        Button reviewsBtn = (Button) findViewById(R.id.ReviewBtn);
        reviewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), ReviewsActivity.class);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putExtra("key", key);
                startActivity(goToNextActivity);
            }
        });
    TextView BusinessAddress = findViewById(R.id.BusinessAddress);
    BusinessAddress.setText(address);

    TextView BusinessPhone = findViewById(R.id.BusinessPhone);
    BusinessPhone.setText(phone);

    TextView BusinessHours = findViewById(R.id.BusinessHours);
    BusinessHours.setText(hours);

    }

    public void makeDestination(){
        ImageButton star = findViewById(R.id.starBtn);
        Cursor resultSet2 = mydatabase.rawQuery("Select * from users where username = '"+user+"'", null);
        resultSet2.moveToFirst();
        if(resultSet2.getInt(4) == key){
            Cursor dummy = mydatabase.rawQuery("Update users set destination = " + 0 + " where username = '"+user+"'",null);
            dummy.moveToFirst();
            star.setImageResource(R.drawable.blackstarbutton);
        }
        else{
            Cursor dummy = mydatabase.rawQuery("Update users set destination = " + key + " where username = '"+user+"'",null);
            dummy.moveToFirst();
            star.setImageResource(R.drawable.goldstarbutton);
        }
    }
    //Buttons should be linked to created pages and a "back to search" button should be created
}
