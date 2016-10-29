package com.santhosh.codepath.twitterstream.activity;

import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.CREATED_AT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.DESCRIPTION;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.EXTENDED_ENTITIES;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.FAVORITED;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.FAVORITE_COUNT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.FOLLOWERS_COUNT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.FOLLOWING_COUNT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.ID;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.LOCATION;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.MEDIA;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.MEDIA_URL;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.PHOTO;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.PROFILE_BACKGROUND_IMAGE;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.PROFILE_IMAGE;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.RETWEETED;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.RETWEET_COUNT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.TWEET_TEXT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.TYPE;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.URL;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.USER;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.USER_HANDLE;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.USER_NAME;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.USER_NAME_EXTRA;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.VERIFIED;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.santhosh.codepath.twitterstream.R;
import com.santhosh.codepath.twitterstream.fragment.PostTweetFragment;
import com.santhosh.codepath.twitterstream.listener.TweetListener;
import com.santhosh.codepath.twitterstream.model.SingleTweet;
import com.santhosh.codepath.twitterstream.model.TweetsAdapter;
import com.santhosh.codepath.twitterstream.oauth.TwitterRestClient;
import com.santhosh.codepath.twitterstream.utils.EndlessScrollListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity implements TweetListener {

    private static List<SingleTweet> mTweetList = new ArrayList<>();
    @BindView(R.id.banner_image)
    ImageView mBannerImage;
    @BindView(R.id.user_profile_image)
    ImageView mUserProfileImage;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.verified)
    ImageView mVerified;
    @BindView(R.id.user_handle)
    TextView mUserHandle;
    @BindView(R.id.user_info)
    LinearLayout mUserInfo;
    @BindView(R.id.user_description)
    TextView mUserDescription;
    @BindView(R.id.location_icon)
    ImageView mLocationIcon;
    @BindView(R.id.location_text)
    TextView mLocationText;
    @BindView(R.id.link_icon)
    ImageView mLinkIcon;
    @BindView(R.id.link_text)
    TextView mLinkText;
    @BindView(R.id.location_site)
    LinearLayout mLocationSite;
    @BindView(R.id.following_count)
    TextView mFollowingCount;
    @BindView(R.id.following_text)
    TextView mFollowingText;
    @BindView(R.id.followers_count)
    TextView mFollowersCount;
    @BindView(R.id.followers_text)
    TextView mFollowersText;
    @BindView(R.id.follower_layout)
    LinearLayout mFollowerLayout;
    @BindView(R.id.user_timeline_list)
    RecyclerView mUserTimelineList;
    @BindView(R.id.fab_compose_tweet)
    FloatingActionButton mFabComposeTweet;
    @BindView(R.id.parent_profile_layout)
    RelativeLayout mParentProfileLayout;
    TweetsAdapter mTweetsAdapter;
    @BindView(R.id.toobar_title)
    TextView mToobarTitle;
    @BindView(R.id.toolbar_profile)
    ImageView mToolbarProfile;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String mUserNameExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profille);
        ButterKnife.bind(this);

        mUserNameExtra = getIntent().getStringExtra(USER_NAME_EXTRA);

        setSupportActionBar(mToolbar);
        mToobarTitle.setText(mUserNameExtra);
        mToolbarProfile.setVisibility(View.GONE);

        mTweetsAdapter = new TweetsAdapter(mTweetList, this);
        mTweetsAdapter.setTweetListener(this);
        mUserTimelineList.setAdapter(mTweetsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mUserTimelineList.setLayoutManager(linearLayoutManager);

        mUserTimelineList.addOnScrollListener(
                new EndlessScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        fetchUserTimeLine(false, page);
                    }
                });

        fetchUserProfileInfo();
        fetchUserTimeLine(true, 0);

        mFabComposeTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                PostTweetFragment postTweetFragment = new PostTweetFragment();
                Bundle bundle = new Bundle();
                bundle.putString(USER_NAME_EXTRA, mUserNameExtra);
                postTweetFragment.setArguments(bundle);
                postTweetFragment.setTweetListener(ProfileActivity.this);
                postTweetFragment.show(fragmentManager, "tweet_compose");
            }
        });
    }

    private void fetchUserProfileInfo() {
        TwitterRestClient client = TwitterApplication.getRestClient();
        client.getUserProfile(new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", "userInfo: " + jsonArray.toString());
                JSONObject user = jsonArray.optJSONObject(0);
                String name = user.optString(USER_NAME);
                mToobarTitle.setText(name);
                String handle = user.optString(USER_HANDLE);
                String location = user.optString(LOCATION);
                String url = user.optString(URL);
                String description = user.optString(DESCRIPTION);
                int followers = user.optInt(FOLLOWERS_COUNT);
                int following = user.optInt(FOLLOWING_COUNT);
                boolean verified = user.optBoolean(VERIFIED);
                String backgroundImage = user.optString(PROFILE_BACKGROUND_IMAGE);
                String profileImage = user.optString(PROFILE_IMAGE);

                if (backgroundImage != null || !backgroundImage.isEmpty()) {
                    Picasso.with(getApplicationContext())
                            .load(backgroundImage)
                            .fit()
                            .into(mBannerImage);
                } else {
                    mBannerImage.setBackgroundColor(getColor(R.color.twitter_blue));
                }

                if (location == null || location.isEmpty() || url == null || url.isEmpty()) {
                    mLocationSite.setVisibility(View.GONE);
                } else {
                    mLocationSite.setVisibility(View.VISIBLE);
                    mLocationText.setText(location);
                    mLinkText.setText(url);
                }

                Picasso.with(getApplicationContext())
                        .load(profileImage)
                        .fit()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(mUserProfileImage);

                if (description != null && !description.isEmpty()) {
                    mUserDescription.setVisibility(View.VISIBLE);
                    mUserDescription.setText(description);
                } else {
                    mUserDescription.setVisibility(View.GONE);
                }
                mFollowersCount.setText(String.valueOf(followers));
                mFollowingCount.setText(String.valueOf(following));

                mUserName.setText(name);
                mUserHandle.setText(getString(R.string.at_symbol, handle));

                if (!verified) {
                    mVerified.setVisibility(View.GONE);
                } else {
                    mVerified.setVisibility(View.VISIBLE);
                    mVerified.setBackground(getDrawable(R.drawable.ic_verified));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                Snackbar.make(mParentProfileLayout, R.string.failed_to_fetch,
                        Snackbar.LENGTH_LONG).show();
            }
        }, mUserNameExtra);
    }

    private void fetchUserTimeLine(final boolean clear, int page) {
        TwitterRestClient client = TwitterApplication.getRestClient();
        client.getUserTimeline(page + 1, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", "timeline: " + jsonArray.toString());
                List<SingleTweet> tempList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    long id = object.optLong(ID);
                    String createdAt = object.optString(CREATED_AT);
                    String text = object.optString(TWEET_TEXT);
                    int retweetCount = object.optInt(RETWEET_COUNT);
                    boolean retweeted = object.optBoolean(RETWEETED);
                    boolean favorited = object.optBoolean(FAVORITED);
                    int favoriteCount = object.optInt(FAVORITE_COUNT);

                    JSONObject userObject = object.optJSONObject(USER);
                    String name = userObject.optString(USER_NAME);
                    String handle = userObject.optString(USER_HANDLE);
                    String profileImage = userObject.optString(PROFILE_IMAGE);

                    String mediaUrl = "";
                    String type = "";
                    JSONObject extended = object.optJSONObject(EXTENDED_ENTITIES);
                    if (extended != null) {
                        JSONArray media = extended.optJSONArray(MEDIA);
                        for (int j = 0; j < media.length(); j++) {
                            JSONObject mediaObject = media.optJSONObject(j);
                            type = mediaObject.optString(TYPE);
                            if (type.equals(PHOTO)) {
                                mediaUrl = mediaObject.optString(MEDIA_URL);
                                break;
                            }
                        }
                    }

                    tempList.add(
                            new SingleTweet(id, createdAt, text, retweetCount, retweeted,
                                    favoriteCount, favorited, name, handle, profileImage, mediaUrl,
                                    type));
                }

                if (clear) {
                    mTweetList.clear();
                    mTweetsAdapter.notifyDataSetChanged();
                }
                int oldSize = mTweetList.size();
                mTweetList.addAll(tempList);
                mTweetsAdapter.notifyItemRangeChanged(oldSize, mTweetList.size() - 1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                Log.d("DEBUG", "UserTimeline: " + statusCode + " : " + errorResponse);
                Snackbar.make(mParentProfileLayout, R.string.failed_to_fetch,
                        Snackbar.LENGTH_LONG).show();
            }
        }, mUserNameExtra);
    }

    @Override
    public void newTweet() {
        Snackbar.make(mParentProfileLayout, R.string.tweet_success, Snackbar.LENGTH_LONG).show();
        fetchUserTimeLine(true, 0);
    }

    @Override
    public void startProfileView(String userHandle) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(USER_NAME_EXTRA, userHandle);
        startActivity(intent);
        finish();
    }

    @Override
    public void startReplyTweetCompose(String userHandle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PostTweetFragment postTweetFragment = new PostTweetFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_NAME_EXTRA, userHandle);
        postTweetFragment.setArguments(bundle);
        postTweetFragment.setTweetListener(ProfileActivity.this);
        postTweetFragment.show(fragmentManager, "tweet_compose");
    }

    @Override
    public void favoriteTweet(long id, boolean favorite) {
        TwitterRestClient client = TwitterApplication.getRestClient();
        if (favorite) {
            client.unFavTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                        JSONObject response) {
                    Log.d("DEBUG", "Post success: " + statusCode);
                    newTweet();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                }
            }, id);
        } else {
            client.favTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                        JSONObject response) {
                    Log.d("DEBUG", "Post success: " + statusCode);
                    newTweet();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                }
            }, id);
        }
    }

    @Override
    public void reTweet(long id) {
        TwitterRestClient client = TwitterApplication.getRestClient();
        client.reTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                    JSONObject response) {
                Log.d("DEBUG", "Post success: " + statusCode);
                newTweet();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
            }
        }, id);
    }
}
