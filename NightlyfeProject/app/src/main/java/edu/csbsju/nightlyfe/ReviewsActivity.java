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
import android.widget.ScrollView;
import android.widget.TextView;

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
        user = getIntent().getStringExtra("user");
        key = getIntent().getIntExtra("key", 0);

        ScrollView mScroll = findViewById(R.id.scrollView);
        mScroll.scrollTo(0, mScroll.getBottom());

        Cursor resultSetReviews = mydatabase.rawQuery("Select * from reviews where id = "+key+" ORDER BY time ASC",null);
        Cursor resultSetBars = mydatabase.rawQuery("Select * from business where id = "+key, null);
        resultSetReviews.moveToFirst();
        resultSetBars.moveToFirst();
        LinearLayout mReviewWindow = findViewById(R.id.reviewWindow);


        TextView mBarName = findViewById(R.id.labelBarName);
        String busName = resultSetBars.getString(1);
        mBarName.setText(busName);

        int size = resultSetReviews.getCount();
        for(int i = 0; i < size; i++){
            TextView mName = new TextView(this);
            TextView mReview = new TextView(this);
            String username = resultSetReviews.getString(0);
            mName.setText(username);
            if(username.equals(user)){
                mName.setTextColor(Color.RED);
            }
            mReview.setText("\t\t\t"+resultSetReviews.getString(2));
            mReview.setTextColor(Color.BLACK);
            mReviewWindow.addView(mName);
            mReviewWindow.addView(mReview);
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
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        });
    }

    private void submitReview(String review) {

        Cursor resultSet = mydatabase.rawQuery("INSERT INTO reviews VALUES ('"+user+"', "+key+", '"+review+"', strftime('%s','now'));",null);
        //Cursor resultSet = mydatabase.rawQuery("INSERT INTO reviews VALUES ('"+user+"', "+key+", '"+review+"');",null);

        resultSet.moveToFirst();
    }
}
