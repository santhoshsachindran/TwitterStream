package com.santhosh.codepath.twitterstream.activity;


import android.app.Application;
import android.content.Context;

import com.santhosh.codepath.twitterstream.oauth.TwitterRestClient;

public class TwitterApplication extends Application {
    private static Context mContext;

    public static TwitterRestClient getRestClient() {
        return (TwitterRestClient) TwitterRestClient.getInstance(TwitterRestClient.class, mContext);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
