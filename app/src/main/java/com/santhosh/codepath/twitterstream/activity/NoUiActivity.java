package com.santhosh.codepath.twitterstream.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.santhosh.codepath.twitterstream.utils.UtilsAndConstants;

public class NoUiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getBoolean(UtilsAndConstants.IS_USER_LOGGED_IN, false)) {
            //intent = new Intent(this, LoginActivity.class);
            intent = new Intent(this, TwitterLoginActivity.class);
        } else {
            intent = new Intent(this, TwitterTimeline.class);
        }

        startActivity(intent);
        finish();
    }
}
