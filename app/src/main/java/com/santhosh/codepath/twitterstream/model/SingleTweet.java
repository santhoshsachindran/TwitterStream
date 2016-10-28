package com.santhosh.codepath.twitterstream.model;


public class SingleTweet {
    private String mCreatedAt;
    private String mText;
    private int mRetweetCount;
    private boolean mRetweeted;
    private int mFavoriteCount;
    private boolean mFavorited;
    private String mUserName;
    private String mUserHandle;
    private String mUserProfileImage;
    private String mMediaUrl;
    private String mType;

    public SingleTweet(String createdAt, String text, int retweetCount, boolean retweeted,
            int favoriteCount, boolean favorited, String userName, String userHandle,
            String userProfileImage, String mediaUrl, String type) {
        mCreatedAt = createdAt;
        mText = text;
        mRetweetCount = retweetCount;
        mRetweeted = retweeted;
        mFavoriteCount = favoriteCount;
        mFavorited = favorited;
        mUserName = userName;
        mUserHandle = userHandle;
        mUserProfileImage = userProfileImage;
        mMediaUrl = mediaUrl;
        mType = type;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getText() {
        return mText;
    }

    public int getRetweetCount() {
        return mRetweetCount;
    }

    public int getFavoriteCount() {
        return mFavoriteCount;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserHandle() {
        return mUserHandle;
    }

    public String getUserProfileImage() {
        return mUserProfileImage;
    }

    public boolean isRetweeted() {
        return mRetweeted;
    }

    public boolean isFavorited() {
        return mFavorited;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public String getType() {
        return mType;
    }
}
