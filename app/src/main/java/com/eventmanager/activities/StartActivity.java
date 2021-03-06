package com.eventmanager.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.eventmanager.R;
import com.eventmanager.database.AppDatabase;

/** This activity is the first activity that shows up when the app is launched. It will bypass the
 * initial screen if the user has already logged in and jump directly to the corresponding activity
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Initialize the database object. Initializing it later may cause performance hiccups on
        //first launch because creating the database for the first time takes time. This results in
        //some views rendering incorrectly.
        AppDatabase.getInstance(this);

    }

    /** Start the Manager Login Activity to prompt a manager to log in
     *
     * @param v The view on which this is called. In this case, a button.
     */
    public void onClickLoginManager(View v) {
        Intent i = new Intent(this, ManagerLoginActivity.class);
        startActivity(i);
    }

    /** Start the Guest Activity
     *
     * @param v The view on which this is called. In this case, a button.
     */
    public void onClickLoginGuest(View v) {
        Intent i = new Intent(this, GuestActivity.class);
        startActivity(i);
    }
}
