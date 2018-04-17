package edu.csbsju.nightlyfe;

import android.database.sqlite.SQLiteDatabase;

public class AddFriendStrategy implements FriendStrategy {

    public SQLiteDatabase mydatabase;

    /*
    Constructor for AddFriendStrategy object
    @param String friend to add
    @param String user to associate with friend
     */
    public AddFriendStrategy(String friend, String user){
        //opens database for use
        mydatabase = FriendSearch.mydatabase;
        algorithmInterface(friend,user);
    }

    /*
    method to add a friend in a AddFriendStrategy object
    @param String friend to add
    @param String user to associate with friend
     */
    public boolean algorithmInterface(String friend, String user){
        mydatabase.execSQL("INSERT INTO friends VALUES ('"+user+"', '"+friend+"', 1);");
        mydatabase.execSQL("INSERT INTO friends VALUES ('"+friend+"', '"+user+"', 1);");
        return true;
    }
}
