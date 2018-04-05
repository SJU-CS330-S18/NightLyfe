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
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewsActivity extends AppCompatActivity {

    public SQLiteDatabase mydatabase;
    public int key;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //gets parameters passed to activity
        key = getIntent().getIntExtra("key", 0);
        user = getIntent().getStringExtra("user");

        Cursor resultSetReviews = mydatabase.rawQuery("Select * from reviews where businesses = "+key+" ORDER BY time ASC",null);
        resultSetReviews.moveToFirst();
        LinearLayout mChatWindow = findViewById(R.id.reviewWindow);

        int size = resultSetReviews.getCount();
        for(int i = 0; i < size; i++){
            TextView mTime = new TextView(this);
            TextView mReview = new TextView(this);
            String username = resultSetReviews.getString(0);
            mTime.setText(resultSetReviews.getString(3));
            if(username.equals(user)){
                mTime.setTextColor(Color.RED);
            }
            mReview.setText("\t\t\t"+resultSetReviews.getString(2));
            mReview.setTextColor(Color.BLACK);
            mChatWindow.addView(mTime);
            mChatWindow.addView(mReview);
            resultSetReviews.moveToNext();
        }

        Button submitReview = (Button) findViewById(R.id.submitReviewBtn);
        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiAutoCompleteTextView review = findViewById(R.id.mReviewEntry);

                submitReview(review.getText().toString());
                Intent goToNextActivity = new Intent(getApplicationContext(), ReviewsActivity.class);
                goToNextActivity.putExtra("key", key);
                startActivity(goToNextActivity);
            }
        });
    }

    private void submitReview(String review) {
        Cursor resultSet = mydatabase.rawQuery("INSERT INTO reviews VALUES ('"+user+"', "+key+", strftime('%s','now'), '"+review+"');",null);
        resultSet.moveToFirst();
    }
}
