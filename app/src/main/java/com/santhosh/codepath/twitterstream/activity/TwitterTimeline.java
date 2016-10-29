package com.santhosh.codepath.twitterstream.activity;

import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.PROFILE_IMAGE;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.USER_NAME_EXTRA;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.santhosh.codepath.twitterstream.R;
import com.santhosh.codepath.twitterstream.fragment.MentionsFragment;
import com.santhosh.codepath.twitterstream.fragment.PostTweetFragment;
import com.santhosh.codepath.twitterstream.fragment.TimelineFragment;
import com.santhosh.codepath.twitterstream.listener.TweetListener;
import com.santhosh.codepath.twitterstream.oauth.TwitterRestClient;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TwitterTimeline extends AppCompatActivity implements
        TimelineFragment.StartProfileViewListener, MentionsFragment.StartProfileViewListener,
        TweetListener {

    @BindView(R.id.sliding_tabs)
    TabLayout mSlidingTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.toobar_title)
    TextView mToobarTitle;
    @BindView(R.id.toolbar_profile)
    ImageView mToolbarProfile;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab_compose_tweet)
    FloatingActionButton mFabComposeTweet;

    TweetsPagerAdapter mTweetsPagerAdapter;
    @BindView(R.id.master_layout)
    CoordinatorLayout mMasterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_timeline);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        mToobarTitle.setText(R.string.timeline);
        getUserUrl();

        mToolbarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProfileView("twitzhandle");
            }
        });

        mFabComposeTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                PostTweetFragment postTweetFragment = new PostTweetFragment();
                postTweetFragment.setTweetListener(TwitterTimeline.this);
                postTweetFragment.show(fragmentManager, "tweet_compose");
            }
        });

        mTweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mTweetsPagerAdapter);
        mSlidingTabs.setupWithViewPager(mViewpager);
        mSlidingTabs.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public void newTweet() {
        Snackbar.make(mMasterLayout, R.string.tweet_success, Snackbar.LENGTH_LONG).show();
        mTweetsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void composeNewTweet() {
        newTweet();
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
        newReplyTweet(userHandle);
    }

    @Override
    public void newReplyTweet(String userHandle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PostTweetFragment postTweetFragment = new PostTweetFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_NAME_EXTRA, userHandle);
        postTweetFragment.setArguments(bundle);
        postTweetFragment.setTweetListener(TwitterTimeline.this);
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

    public void getUserUrl() {
        TwitterRestClient client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String profileImage;
                profileImage = response.optString(PROFILE_IMAGE);
//                Glide.with(getApplicationContext())
//                        .load(profileImage)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .fitCenter()
//                        .into(mToolbarProfile);
                Picasso.with(getApplicationContext())
                        .load(profileImage)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .into(mToolbarProfile);
            }
        });
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String titles[] = new String[]{"Timeline", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new TimelineFragment();
            } else if (position == 1) {
                return new MentionsFragment();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return titles[position];
        }
    }
}
