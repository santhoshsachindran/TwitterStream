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

    public SingleTweet(String createdAt, String text, int retweetCount, boolean retweeted,
            int favoriteCount, boolean favorited, String userName, String userHandle,
            String userProfileImage) {
        mCreatedAt = createdAt;
        mText = text;
        mRetweetCount = retweetCount;
        mRetweeted = retweeted;
        mFavoriteCount = favoriteCount;
        mFavorited = favorited;
        mUserName = userName;
        mUserHandle = userHandle;
        mUserProfileImage = userProfileImage;
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

    public boolean getRetweeted() {
        return mRetweeted;
    }

    public int getFavoriteCount() {
        return mFavoriteCount;
    }

    public boolean getFavorited() {
        return mFavorited;
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

    @Override
    public String toString() {
        return "SingleTweet{" +
                "mCreatedAt='" + mCreatedAt + '\'' +
                ", mText='" + mText + '\'' +
                ", mRetweetCount=" + mRetweetCount +
                ", mRetweeted=" + mRetweeted +
                ", mFavoriteCount=" + mFavoriteCount +
                ", mFavorited=" + mFavorited +
                ", mUserName='" + mUserName + '\'' +
                ", mUserHandle='" + mUserHandle + '\'' +
                ", mUserProfileImage='" + mUserProfileImage + '\'' +
                '}';
    }
}
