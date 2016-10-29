package com.santhosh.codepath.twitterstream.listener;


public interface TweetListener {
    void newTweet();

    void startProfileView(String userHandle);

    void startReplyTweetCompose(String userHandle);

    void favoriteTweet(long id, boolean favorite);

    void reTweet(long id);
}
