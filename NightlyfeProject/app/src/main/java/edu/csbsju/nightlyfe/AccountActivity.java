package edu.csbsju.nightlyfe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {

    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        user = getIntent().getStringExtra("username");
        TextView mUserHeader = findViewById(R.id.mUserHeader);
        mUserHeader.setText("Hello, " + user + "!");
    }
}
