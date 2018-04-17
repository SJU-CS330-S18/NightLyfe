package edu.csbsju.nightlyfe;

import android.database.sqlite.SQLiteDatabase;

public class AddFriendStrategy implements FriendStrategy {

    public SQLiteDatabase mydatabase;

    public AddFriendStrategy(String friend, String user){
        //opens database for use
        mydatabase = FriendSearch.mydatabase;
        algorithmInterface(friend,user);
    }

    public boolean algorithmInterface(String friend, String user){
        mydatabase.execSQL("INSERT INTO friends VALUES ('"+user+"', '"+friend+"', 1);");
        mydatabase.execSQL("INSERT INTO friends VALUES ('"+friend+"', '"+user+"', 1);");
        return true;
        //Intent goToNextActivity = new Intent(getApplicationContext(), FriendsList.class);
        //goToNextActivity.putExtra("user", user);
        //startActivity(goToNextActivity);
    }
}
