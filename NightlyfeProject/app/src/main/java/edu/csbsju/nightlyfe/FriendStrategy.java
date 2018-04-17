package edu.csbsju.nightlyfe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

/*
Interface for our Strategy Behavioral design pattern, associated with adding or removing friends
 */
public interface FriendStrategy {

    /*
    method to dynamically add or remove a friend in subclasses
    @param String friend to add or remove
    @param String user to associate with friend
     */
    boolean algorithmInterface(String friend, String user);
}
