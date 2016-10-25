package com.santhosh.codepath.twitterstream.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.santhosh.codepath.twitterstream.R;
import com.santhosh.codepath.twitterstream.oauth.TwitterRestClient;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class TwitterTimeline extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_timeline);

        TwitterRestClient client = TwitterApplication.getRestClient();
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", "timeline: " + jsonArray.toString());
                // Load json array into model classes
            }
        });
    }
}
