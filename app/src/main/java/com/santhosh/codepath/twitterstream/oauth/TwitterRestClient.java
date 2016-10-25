package com.santhosh.codepath.twitterstream.oauth;

import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.TWITTER_CALLBACK_URL;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.TWITTER_KEY;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.TWITTER_REST_URL;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.TWITTER_SECRET;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;


public class TwitterRestClient extends OAuthBaseClient {
    private static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    private static final String REST_URL = TWITTER_REST_URL;
    private static final String REST_CONSUMER_KEY = TWITTER_KEY;
    private static final String REST_CONSUMER_SECRET = TWITTER_SECRET;
    private static final String REST_CALLBACK_URL = TWITTER_CALLBACK_URL;

    public TwitterRestClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET,
                REST_CALLBACK_URL);
    }

    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        getClient().get(apiUrl, params, handler);
    }

    public void postTweet(String body, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        getClient().post(apiUrl, params, handler);
    }
}
