package edu.csbsju.nightlyfe;

import android.database.sqlite.SQLiteDatabase;

public class RemoveFriendStrategy implements FriendStrategy {

    public SQLiteDatabase mydatabase;

    public RemoveFriendStrategy(String friend, String user){
        //opens database for use
        mydatabase = FriendSearch.mydatabase;
        algorithmInterface(friend,user);
    }

    public boolean algorithmInterface(String friend, String user){
        mydatabase.execSQL("DELETE FROM friends WHERE user1 = '"+user+"' AND user2 =  '"+friend+"';");
        mydatabase.execSQL("DELETE FROM friends WHERE user1 = '"+friend+"' AND user2 =  '"+user+"';");
        return true;
        //Intent goToNextActivity = new Intent(getApplicationContext(), FriendsList.class);
        //goToNextActivity.putExtra("user", user);
        //startActivity(goToNextActivity);
    }
}
