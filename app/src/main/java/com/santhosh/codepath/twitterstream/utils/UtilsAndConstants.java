package com.santhosh.codepath.twitterstream.utils;


public class UtilsAndConstants {
    // TODO: Add own KEY and SECRET for fetching/posting tweets
    public static final String TWITTER_KEY = "";
    public static final String TWITTER_SECRET = "";
    public static final String TWITTER_CALLBACK_URL = "x-oauthflow-twitter://mytwitterfeed";
    public static final String TWITTER_REST_URL = "https://api.twitter.com/1.1";

    // JSON Parsing
    public static final String CREATED_AT = "created_at";
    public static final String TWEET_TEXT = "text";
    public static final String RETWEET_COUNT = "retweet_count";
    public static final String RETWEETED = "retweeted";
    public static final String FAVORITE_COUNT = "favorite_count";
    public static final String FAVORITED = "favorited";
    public static final String USER = "user";
    public static final String USER_NAME = "name";
    public static final String USER_HANDLE = "screen_name";
    public static final String PROFILE_IMAGE = "profile_image_url";
    public static final String EXTENDED_ENTITIES = "extended_entities";
    public static final String MEDIA = "media";
    public static final String MEDIA_URL = "media_url";
    public static final String TYPE = "type";
    public static final String PHOTO = "photo";

    public static final int PLAIN_TWEET = 0;
    public static final int IMAGE_TWEET = 1;
}
