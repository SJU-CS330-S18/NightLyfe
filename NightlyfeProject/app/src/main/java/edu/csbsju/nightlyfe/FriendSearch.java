package edu.csbsju.nightlyfe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FriendSearch extends AppCompatActivity {

    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);

        search = getIntent().getStringExtra("search");
    }
}
