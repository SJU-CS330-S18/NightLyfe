
package edu.csbsju.nightlyfe;

/*
Restaurant_Page class provides functionality to dynamically display restaurant information and buttons/paths based on user choice

@author Grant Salk
 */
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.EditText;
import android.content.Context;

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
        final Cursor userResultSet = mydatabase.rawQuery("Select * from users where username = '" + user +"'", null);
        userResultSet.moveToFirst();
        final String busName = resultSet.getString(1);
        String address = resultSet.getString(3);

        String phone = resultSet.getString(6);
        String hours = resultSet.getString(7);

        /*
        Button associated with viewing the map location of a given bar
         */
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

        /*
        Button associated with viewing the bulletin board of a given bar
         */
        Button BulletinBtn = (Button) findViewById(R.id.BulletinBtn);
        BulletinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((userResultSet.getInt(2) == 2 || userResultSet.getInt(2) == 4 || userResultSet.getInt(2) == 5) && userResultSet.getInt(4) == key) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), OwnerBulletinBoard.class);
                    goToNextActivity.putExtra("user", user);
                    goToNextActivity.putExtra("name", busName);
                    goToNextActivity.putExtra("key", key);
                    startActivity(goToNextActivity);
                }
                else{
                    Intent goToNextActivity = new Intent(getApplicationContext(), BulletinBoard.class);
                    goToNextActivity.putExtra("user", user);
                    goToNextActivity.putExtra("name", busName);
                    goToNextActivity.putExtra("key", key);
                    startActivity(goToNextActivity);
                }
            }
        });

        /*
        Button associated with adding or removing a given bar to your favorites list
         */
        Button favoritesBtn = (Button) findViewById(R.id.FavoritesBtn);
        Cursor checkFavorites = mydatabase.rawQuery("SELECT * FROM favorites WHERE user = '" + user + "' AND locationID = " + key + ";", null);
        checkFavorites.moveToFirst();
        //Changes text of button if bar is already on favorites list
        if(checkFavorites.getCount() == 1){
            favoritesBtn.setText("Remove from Favorites");
        }
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
                    ((Button)view).setText("Remove from Favorites");
                } else {
                    Cursor resultSet = mydatabase.rawQuery("DELETE FROM favorites WHERE user = '" + user + "' and locationID = " + key + ";", null);
                    resultSet.moveToFirst();
                    Toast toastName = Toast.makeText(getApplicationContext(), busName + " has been removed from your Favorites List", Toast.LENGTH_LONG);
                    toastName.show();
                    ((Button)view).setText("Add to Favorites");
                }
            }
        });

        //Button to return back to the list of bars
        Button listBtn = (Button) findViewById(R.id.listBtn);
        if(userResultSet.getInt(2) == 2 || userResultSet.getInt(2) == 4){
            listBtn.setText("Return Home");
        }
        listBtn.setTag(userResultSet.getInt(2));
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((int)view.getTag()) == 1) {
                    Intent goToNextActivity = new Intent(getApplicationContext(), RestaurantList.class);
                    goToNextActivity.putExtra("user", user);
                    startActivity(goToNextActivity);
                }
                else if (((int)view.getTag()) == 2 || ((int)view.getTag() == 4)){
                    Intent goToNextActivity = new Intent(getApplicationContext(), OwnerHomescreen.class);
                    goToNextActivity.putExtra("user", user);
                    startActivity(goToNextActivity);
                }
            }
        });

        //Button associated with adding a given restaurant as a destination
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

        //Button associated with viewing reviews of a given bar
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

        //SQL request to determine if the bar already has an owner
        Cursor ownerResultSet = mydatabase.rawQuery("Select * from users where destination = " + key + " and (type = 2 or type  = 4)", null);
        ownerResultSet.moveToFirst();
        //System.out.println(ownerResultSet.getCount());

        //retrieves layout to add claimContainer
        RelativeLayout rl = findViewById(R.id.businessLayout);
        rl.setGravity(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        if(ownerResultSet.getCount() == 0) {
            //Creates claimTxt and claimBtn to be added to restaurant page if there is no owner
            EditText claimTxt = new EditText(this);
            claimTxt.setWidth(200);
            Button claimBtn = new Button(this);
            claimBtn.setText("Claim");

            LinearLayout claimContainer = new LinearLayout(this);
            claimContainer.setOrientation(LinearLayout.HORIZONTAL);
            //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            claimContainer.addView(claimTxt);
            claimContainer.addView(claimBtn);
            claimContainer.setGravity(Gravity.CENTER_HORIZONTAL);

            //rp.addRule(RelativeLayout.ABOVE, R.id.listBtn);
            rp.addRule(RelativeLayout.BELOW, R.id.BusinessHours);
            rp.addRule(RelativeLayout.CENTER_IN_PARENT);
            rl.addView(claimContainer,rp);

            claimBtn.setTag(claimTxt);
            claimBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText claim = (EditText)view.getTag();
                    int verifyClaim = Integer.parseInt(claim.getText().toString());
                    Cursor resultSet3 = mydatabase.rawQuery("Select * from business where id = "+key, null);
                    resultSet3.moveToFirst();
                    int restaurantOwn = resultSet3.getInt(8);
                    if(verifyClaim != restaurantOwn) {
                        claim.setError("Invalid owner ID");
                    }
                    else if(verifyClaim == restaurantOwn){
                        Cursor resultSet4 = mydatabase.rawQuery("Update users set type = " + 2 + ", destination = "+key+" where username = '"+user+"'", null);
                        resultSet4.moveToFirst();
                        Intent goToNextActivity = new Intent(getApplicationContext(), OwnerHomescreen.class);
                        goToNextActivity.putExtra("user", user);
                        startActivity(goToNextActivity);
                        Context context = getApplicationContext();
                        Toast toastClaim = Toast.makeText(context,"Ownership Claimed Successfully", Toast.LENGTH_LONG);
                        toastClaim.show();
                    }
                }
            });
        }

        //Sets the text field of the bar name
        TextView BusinessName = findViewById(R.id.BusinessName);
        BusinessName.setText(busName);

        //Sets the text field of the bar address
        TextView BusinessAddress = findViewById(R.id.BusinessAddress);
        BusinessAddress.setText(address);

        //Sets the text field of the bar phone number
        TextView BusinessPhone = findViewById(R.id.BusinessPhone);
        BusinessPhone.setText(phone);

        //Sets the text field of the bar hours
        TextView BusinessHours = findViewById(R.id.BusinessHours);
        BusinessHours.setText(hours);
    }

    /*
    Method associated with destination button, which toggles the restaurant as a destination if it isnt already,
    and deactivates the destination if it is already selected.
     */
    public void makeDestination(){
        ImageButton star = findViewById(R.id.starBtn);
        Cursor resultSet2 = mydatabase.rawQuery("Select * from users where username = '"+user+"'", null);
        resultSet2.moveToFirst();
        if(resultSet2.getInt(2) == 2 || resultSet2.getInt(2) == 4){
            Context context = getApplicationContext();
            Toast toastClaim = Toast.makeText(context,"As an owner, you cannot change your destination", Toast.LENGTH_LONG);
            toastClaim.show();
        }
        else if(resultSet2.getInt(4) == key){
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
