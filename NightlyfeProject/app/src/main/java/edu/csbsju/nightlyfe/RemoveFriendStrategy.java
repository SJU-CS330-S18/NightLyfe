package edu.csbsju.nightlyfe;

import android.database.sqlite.SQLiteDatabase;

/*
Class for our Strategy Behavioral design pattern, associated with removing a friend
@implements FriendStrategy
 */
public class RemoveFriendStrategy implements FriendStrategy {

    public SQLiteDatabase mydatabase;

    /*
    Constructor for AddFriendStrategy object
    @param String friend to add
    @param String user to associate with friend
     */
    public RemoveFriendStrategy(String friend, String user){
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
        mydatabase.execSQL("DELETE FROM friends WHERE user1 = '"+user+"' AND user2 =  '"+friend+"';");
        mydatabase.execSQL("DELETE FROM friends WHERE user1 = '"+friend+"' AND user2 =  '"+user+"';");
        return true;
    }
}
