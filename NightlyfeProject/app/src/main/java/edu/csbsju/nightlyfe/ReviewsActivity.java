package edu.csbsju.nightlyfe;

import android.content.DialogInterface;
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
import android.graphics.Typeface;
import android.app.AlertDialog;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;

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


        TextView mReviewsHeader = findViewById(R.id.mReviewsHeader);
        TextView mBarName = findViewById(R.id.labelBarName);
        String busName = resultSetBars.getString(1);
        mReviewsHeader.setText("Reviews");
        mBarName.setText(busName);

        int size = resultSetReviews.getCount();
        for(int i = 0; i < size; i++){
            TextView mName = new TextView(this);
            TextView mTime = new TextView(this);
            TextView mReview = new TextView(this);
            Button btnDelete = new Button(this);

            String username = resultSetReviews.getString(0);
            mName.setText(username);
            int time = resultSetReviews.getInt(2);
            SimpleDateFormat originalFormat = new SimpleDateFormat("MMddyyyy");
            try {
                Date date = originalFormat.parse(String.valueOf(time));
                mTime.setText(date.toString());
            } catch(Exception e) {
                System.out.println(e);
            }

            mName.setTextSize(15);
            if(username.equals(user)){
                mName.setTextColor(Color.RED);
                btnDelete.setText("Delete My Review For " + busName);
                btnDelete.setTextColor(Color.RED);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteReview();
                    }
                });
                mReviewWindow.addView(btnDelete);
            }
            mReview.setText("\t\t\t"+resultSetReviews.getString(3));
            mReview.setTextColor(Color.BLACK);
            mReview.setTextSize(25);
            mReview.setTypeface(null, Typeface.ITALIC);
            mReviewWindow.addView(mName);
            mReviewWindow.addView(mTime);
            mReviewWindow.addView(mReview);
            resultSetReviews.moveToNext();
        }

        Button mHome = (Button) findViewById(R.id.btnHome);
        mHome.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", user);
                startActivity(goToNextActivity);
            }
        }));

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
        String quotedReview = "\"" + review + "\"";
        Cursor checkUser = mydatabase.rawQuery("SELECT * FROM reviews WHERE username = '" + user + "' AND id = " + key +";", null);
        checkUser.moveToFirst();
        if(checkUser.getCount() == 0) {
            Cursor resultSet = mydatabase.rawQuery("INSERT INTO reviews VALUES ('" + user + "', " + key + ", strftime('%s','now'), '" + quotedReview + "');", null);
            resultSet.moveToFirst();
        } else {
            Toast toastName = Toast.makeText(getApplicationContext(),"You have already submitted a review!", Toast.LENGTH_LONG);
            toastName.show();
        }
    }

    private void deleteReview() {
        Cursor deletion = mydatabase.rawQuery("DELETE FROM reviews WHERE username = '" + user + "' AND id = " + key + ";", null);
        deletion.moveToFirst();
        Intent goToNextActivity = new Intent(getApplicationContext(), ReviewsActivity.class);
        goToNextActivity.putExtra("key", key);
        goToNextActivity.putExtra("user", user);
        startActivity(goToNextActivity);
    }
}
