package com.santhosh.codepath.twitterstream.fragment;

import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.CREATED_AT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.EXTENDED_ENTITIES;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.FAVORITED;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.FAVORITE_COUNT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.MEDIA;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.MEDIA_URL;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.PHOTO;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.PROFILE_IMAGE;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.RETWEETED;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.RETWEET_COUNT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.TWEET_TEXT;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.TYPE;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.USER;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.USER_HANDLE;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.USER_NAME;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.santhosh.codepath.twitterstream.R;
import com.santhosh.codepath.twitterstream.activity.TwitterApplication;
import com.santhosh.codepath.twitterstream.listener.TweetListener;
import com.santhosh.codepath.twitterstream.model.SingleTweet;
import com.santhosh.codepath.twitterstream.model.TweetsAdapter;
import com.santhosh.codepath.twitterstream.oauth.TwitterRestClient;
import com.santhosh.codepath.twitterstream.utils.EndlessScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineFragment extends Fragment implements TweetListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.timeline_list)
    RecyclerView mTimelineList;
    @BindView(R.id.fab_compose_tweet)
    FloatingActionButton mFabComposeTweet;

    private static List<SingleTweet> mTweetList = new ArrayList<>();
    TweetsAdapter mTweetsAdapter;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, view);

        mTweetsAdapter = new TweetsAdapter(mTweetList, getContext());
        mTimelineList.setAdapter(mTweetsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mTimelineList.setLayoutManager(linearLayoutManager);

        mTimelineList.addOnScrollListener(
                new EndlessScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        fetchTimeLine(false, page);
                    }
                });

        fetchTimeLine(true, 0);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.timeline);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimeLine(true, 0);
            }
        });

        mSwipeContainer.setProgressViewOffset(false, 0, 100);

        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mFabComposeTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                PostTweetFragment postTweetFragment = new PostTweetFragment();
                postTweetFragment.setTweetListener(TimelineFragment.this);
                postTweetFragment.show(fragmentManager, "tweet_compose");
            }
        });

        return view;
    }

    private void fetchTimeLine(final boolean clear, int page) {
        TwitterRestClient client = TwitterApplication.getRestClient();
        client.getHomeTimeline(page + 1, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", "timeline: " + jsonArray.toString());
                List<SingleTweet> tempList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
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
                            new SingleTweet(createdAt, text, retweetCount, retweeted, favoriteCount,
                                    favorited, name, handle, profileImage, mediaUrl, type));
                }

                if (clear) mTweetList.clear();
                int oldSize = mTweetList.size();
                mTweetList.addAll(tempList);
                mTweetsAdapter.notifyItemRangeChanged(oldSize, mTweetList.size() - 1);
                mSwipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                mSwipeContainer.setRefreshing(false);
                Snackbar.make(getView(), R.string.failed_to_fetch, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void newTweet() {
        Snackbar.make(getView(), R.string.tweet_success, Snackbar.LENGTH_LONG).show();
        fetchTimeLine(true, 0);
    }
}
