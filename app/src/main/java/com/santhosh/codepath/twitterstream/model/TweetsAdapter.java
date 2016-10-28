package com.santhosh.codepath.twitterstream.model;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.santhosh.codepath.twitterstream.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
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

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    private List<SingleTweet> mTweets;
    private Context mContext;

    public TweetsAdapter(List<SingleTweet> tweets) {
        mTweets = tweets;
    }

    public TweetsAdapter(List<SingleTweet> tweets, Context context) {
        mTweets = tweets;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_tweet, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SingleTweet singleTweet = mTweets.get(position);

        holder.mUserName.setText(singleTweet.getUserName());
        holder.mUserHandle.setText(
                mContext.getResources().getString(R.string.at_symbol, singleTweet.getUserHandle()));
        holder.mTweetText.setText(singleTweet.getText());
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

        return splitTime[0] + splitTime[1].charAt(0);
    }
}
