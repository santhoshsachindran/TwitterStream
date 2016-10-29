package com.santhosh.codepath.twitterstream.model;


import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.PHOTO;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.santhosh.codepath.twitterstream.R;
import com.santhosh.codepath.twitterstream.listener.TweetListener;
import com.santhosh.codepath.twitterstream.utils.PatternEditableBuilder;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MentionsAdapter extends RecyclerView.Adapter<MentionsAdapter.ViewHolder> {

    private static List<SingleTweet> mTweets;
    private static Context mContext;
    private static TweetListener mTweetListener;

    public MentionsAdapter(List<SingleTweet> tweets, Context context) {
        mTweets = tweets;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_tweet_image, parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SingleTweet singleTweet = mTweets.get(position);

        holder.mUserName.setText(singleTweet.getUserName());
        holder.mUserHandle.setText(
                mContext.getResources().getString(R.string.at_symbol, singleTweet.getUserHandle
                        ()));
        holder.mTweetText.setText(singleTweet.getText());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                if (mTweetListener != null) {
                                    if (text.startsWith("@")) {
                                        mTweetListener.startProfileView(text.substring(1));
                                    }
                                }
                            }
                        })
                .addPattern(Pattern.compile("#(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                if (mTweetListener != null) {
                                    if (text.startsWith("@")) {
                                        mTweetListener.startProfileView(text.substring(1));
                                    }
                                }
                            }
                        }).into(holder.mTweetText);
        holder.mTimeRemaining.setText(getRelativeTimeStamp(singleTweet.getCreatedAt()));
//        Glide.with(holder.mUserProfileImage.getContext())
//                .load(singleTweet.getUserProfileImage())
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.mUserProfileImage);
        Picasso.with(holder.mUserProfileImage.getContext())
                .load(singleTweet.getUserProfileImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(holder.mUserProfileImage);
        if (!(mTweets.get(position).getMediaUrl() == null || mTweets.get(
                position).getMediaUrl().isEmpty()) && mTweets.get(position).getType().equals(
                PHOTO)) {
            holder.mUrlImagePreview.setVisibility(View.VISIBLE);
            Picasso.with(holder.mUrlImagePreview.getContext())
                    .load(singleTweet.getMediaUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .into(holder.mUrlImagePreview);
        } else {
            holder.mUrlImagePreview.setVisibility(View.GONE);
        }

        holder.mRetweetImage.setImageResource(
                singleTweet.isRetweeted() ? R.drawable.tweet_retweet_on : R.drawable.tweet_retweet);
        holder.mRetweetCount.setText(String.valueOf(singleTweet.getRetweetCount()));
        holder.mHeartImage.setImageResource(
                singleTweet.isFavorited() ? R.drawable.tweet_heart_on : R.drawable.tweet_heart);
        holder.mHeartCount.setText(String.valueOf(singleTweet.getFavoriteCount()));
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public String getRelativeTimeStamp(String time) {
        String twitterTimeFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(twitterTimeFormat, Locale.US);
        dateFormat.setLenient(true);

        String relativeTime = "";
        try {
            long dateMillis = dateFormat.parse(time).getTime();
            relativeTime = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] splitTime = relativeTime.split(" ");

        return splitTime.length < 2 ? relativeTime : splitTime[0] + splitTime[1].charAt(0);
    }

    public void setTweetListener(TweetListener tweetListener) {
        mTweetListener = tweetListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_profile_image)
        ImageView mUserProfileImage;
        @BindView(R.id.user_name)
        TextView mUserName;
        @BindView(R.id.user_handle)
        TextView mUserHandle;
        @BindView(R.id.time_remaining)
        TextView mTimeRemaining;
        @BindView(R.id.tweet_text)
        TextView mTweetText;
        @BindView(R.id.url_image_preview)
        ImageView mUrlImagePreview;
        @BindView(R.id.reply)
        ImageView mReply;
        @BindView(R.id.retweet_image)
        ImageView mRetweetImage;
        @BindView(R.id.retweet_count)
        TextView mRetweetCount;
        @BindView(R.id.heart_image)
        ImageView mHeartImage;
        @BindView(R.id.heart_count)
        TextView mHeartCount;

        public ViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mUserProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SingleTweet tweet = mTweets.get(getAdapterPosition());
                    if (mTweetListener != null) {
                        mTweetListener.startProfileView(tweet.getUserHandle());
                    }
                }
            });

            mReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SingleTweet tweet = mTweets.get(getAdapterPosition());
                    if (mTweetListener != null) {
                        mTweetListener.startReplyTweetCompose(tweet.getUserHandle());
                    }
                }
            });

            mRetweetImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SingleTweet tweet = mTweets.get(getAdapterPosition());
                    if (mTweetListener != null && !tweet.isRetweeted()) {
                        mTweetListener.reTweet(tweet.getId());
                    }

                }
            });

            mHeartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SingleTweet tweet = mTweets.get(getAdapterPosition());
                    if (mTweetListener != null) {
                        mTweetListener.favoriteTweet(tweet.getId(), tweet.isFavorited());
                    }
                }
            });
        }
    }
}