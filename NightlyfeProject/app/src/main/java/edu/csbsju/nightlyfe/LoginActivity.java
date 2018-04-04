package edu.csbsju.nightlyfe;

import android.database.sqlite.*;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.*;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //how to create a database
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //populates database with default data
        populateDatabase();
        //addToDB();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserView = (AutoCompleteTextView) findViewById(R.id.userTxt);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.passTxt);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignin = (Button) findViewById(R.id.signinBtn);
        mSignin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegister = (Button) findViewById(R.id.registerBtn);
        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUserView.getText().toString();
                Intent goToNextActivity = new Intent(getApplicationContext(), Register.class);
                goToNextActivity.putExtra("user", username);
                startActivity(goToNextActivity);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUserView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check that credentials are valid in database.
        if (TextUtils.isEmpty(username)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else if (isValidCredentials(username, password) == -1) {
            //mUserView.setError(getString(R.string.error_invalid_email));
            mUserView.setError("Invalid credentials");
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
            Intent goToNextActivity;

            //Intent used to reroute to new page, example from login to homepage
            if (isValidCredentials(username, password) == 1) {
                goToNextActivity = new Intent(getApplicationContext(), Homescreen.class);
                goToNextActivity.putExtra("user", username);
            }
            else if (isValidCredentials(username, password) == 3) {
                goToNextActivity = new Intent(getApplicationContext(), AdminHomescreen.class);
                goToNextActivity.putExtra("user", username);
            }
            else if (isValidCredentials(username, password) == 2 || isValidCredentials(username, password) == 4) {
                goToNextActivity = new Intent(getApplicationContext(), OwnerHomescreen.class);
                goToNextActivity.putExtra("user", username);
            }
            else{
                goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
            }
            startActivity(goToNextActivity);
        }
    }

    private int isValidCredentials(String username, String password) {

        //how to querey from the table
        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '"+username+"'",null);

        if (resultSet.getCount() == 0){
            System.out.println("Result set null");
            return -1;
        }

        //set cursor to first item in the table
        resultSet.moveToFirst();


        System.out.println(resultSet.getString(0));

        if (username.equals(resultSet.getString(0)) && password.equals(resultSet.getString(1))){
            int type = resultSet.getInt(2);
            return type;
        }
        else{
            return -1;
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUserView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    //method to repopulate database with test information
    private void populateDatabase(){

        //drops tables to recreate and populate
        mydatabase.execSQL("DROP TABLE IF EXISTS users;");
        mydatabase.execSQL("DROP TABLE IF EXISTS business;");
        mydatabase.execSQL("DROP TABLE IF EXISTS reviews;");
        mydatabase.execSQL("DROP TABLE IF EXISTS friends;");
        mydatabase.execSQL("DROP TABLE IF EXISTS plans;");
        mydatabase.execSQL("DROP TABLE IF EXISTS specials;");
        mydatabase.execSQL("DROP TABLE IF EXISTS users;");
        mydatabase.execSQL("DROP TABLE IF EXISTS friendgroups;");
        mydatabase.execSQL("DROP TABLE IF EXISTS groupmember;");
        mydatabase.execSQL("DROP TABLE IF EXISTS favorites");
        mydatabase.execSQL("DROP TABLE IF EXISTS groupmessage");

        //creates table users
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS users (username VARCHAR(20) PRIMARY KEY, password VARCHAR(20), type INT, name VARCHAR(30), destination INT REFERENCES business(id));");

        //creates table businesses
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS business (id int PRIMARY KEY, name VARCHAR(20), city VARCHAR(20), address VARCHAR(100), latitude FLOAT(9), longitude FLOAT(9), phone INT, hours VARCHAR(15), ownerID INT);");

        //creates table reviews
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS reviews (username VARCHAR(20) REFERENCES users(username), id int REFERENCES businesses(id), commenttext VARCHAR2(2000), PRIMARY KEY(username, id));");

        //creates table friends
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS friends (user1 VARCHAR(20), user2 VARCHAR(20), status INT, FOREIGN KEY (user1) REFERENCES users(username),  FOREIGN KEY (user2) REFERENCES users(username), PRIMARY KEY (user1, user2));");

        //creates table plans
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS plans (who VARCHAR(20), business VARCHAR(20) REFERENCES businesses(name), plantime DATE);");

        //creates table specials
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS specials (businessID REFERENCES businesses(id), special VARCHAR2(2000), starttime DATE, endtime DATE);");

        //creates table friendgroups
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS friendgroups (groupID INT, groupName VARCHAR(20));");

        //creates table groupmember
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS groupmember (groupID INT REFERENCES friendgroups(groupID),  username VARCHAR(20) REFERENCES users(username), PRIMARY KEY (groupID, username));");

        //creates table favorites
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS favorites (user VARCAR(20) REFERENCES users(username),  location VARCHAR(20) REFERENCES businesses(name), PRIMARY KEY (user, location));");

        //creates table groupmessage
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS groupmessage (groupID INT REFERENCES friendgroups(groupID), username VARCHAR(20) REFERENCES users(username), time INT, comment VARCHAR(100), PRIMARY KEY(groupID, username, time));");


        mydatabase.execSQL("INSERT INTO users VALUES ('admin1', 'pass', 3, 'Admin One', 0);");
        mydatabase.execSQL("INSERT INTO users VALUES ('admin2', 'pass', 3, 'Admin Two', 0);");
        mydatabase.execSQL("INSERT INTO users VALUES ('owner1', 'pass', 2, 'Sals Owner', 1);");
        mydatabase.execSQL("INSERT INTO users VALUES ('owner2', 'pass', 2, 'La Owner', 2);");
        mydatabase.execSQL("INSERT INTO users VALUES ('user1', 'pass', 1, 'John Doe', 0);");
        mydatabase.execSQL("INSERT INTO users VALUES ('user2', 'pass', 1, 'Jane Doe', 0);");
        mydatabase.execSQL("INSERT INTO users VALUES ('user3', 'pass', 1, 'Jerry Springer', 0);");
        mydatabase.execSQL("INSERT INTO users VALUES ('tdrichmond', 'pass', 1, 'Tom Richmond', 0);");

        mydatabase.execSQL("INSERT INTO friends VALUES ('user1', 'user2', 1);");
        mydatabase.execSQL("INSERT INTO friends VALUES ('user2', 'user1', 1);");
        mydatabase.execSQL("INSERT INTO friends VALUES ('tdrichmond', 'user1', 1);");
        mydatabase.execSQL("INSERT INTO friends VALUES ('user1', 'tdrichmond', 1);");
        mydatabase.execSQL("INSERT INTO friends VALUES ('grsalk', 'user1', 1);");
        mydatabase.execSQL("INSERT INTO friends VALUES ('user1', 'grsalk', 1);");

        mydatabase.execSQL("INSERT INTO friendgroups VALUES (1, 'St. Joe Bros');");
        mydatabase.execSQL("INSERT INTO groupmember VALUES (1, 'tdrichmond');");
        mydatabase.execSQL("INSERT INTO groupmember VALUES (1, 'user1');");
        mydatabase.execSQL("INSERT INTO groupmember VALUES (1, 'user3');");

        mydatabase.execSQL("INSERT INTO business VALUES (1, 'Sals', 'Saint Joseph', '109 W Minnesota St, St Joseph, MN 56374', 45.564497, -94.320641, 123456789, '11:00am-1:00am', 1111);");
        mydatabase.execSQL("INSERT INTO business VALUES (2, 'La Playette', 'Saint Joseph', '19 College Ave N, St Joseph, MN 56374', 45.564497, -94.320641, 123456789, '11:00am-1:00am', 2222);");
        mydatabase.execSQL("INSERT INTO business VALUES (3, 'The Middy', 'Saint Joseph', '21 W Minnesota St, St Joseph, MN 56374', 45.564497, -94.320641, 123456789, '11:00am-1:00am', 3333);");
        mydatabase.execSQL("INSERT INTO business VALUES (4, 'Bad Habit', 'Saint Joseph', '15 E Minnesota St #108, St Joseph, MN 56374', 45.564497, -94.320641, 123456789, '11:00am-1:00am', 4444);");

        mydatabase.execSQL("INSERT INTO specials VALUES (1, 'Big Mugs!! Come get your drink on here at Sals!', 2018-04-04, 2018-04-04);");

        mydatabase.execSQL("INSERT INTO favorites VALUES ('user1', 'sals')");
        mydatabase.execSQL("INSERT INTO favorites VALUES ('user1', 'Middy')");

        //how to querey from the table
        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = 'user1'",null);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

