package com.santhosh.codepath.twitterstream.model;

import org.parceler.Parcel;

@Parcel
public class SingleTweet {
    String mCreatedAt;
    String mText;
    int mRetweetCount;
    boolean mRetweeted;
    int mFavoriteCount;
    boolean mFavorited;
    String mUserName;
    String mUserHandle;
    String mUserProfileImage;
    String mMediaUrl;
    String mType;

    public SingleTweet() {
    }

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
