package com.santhosh.codepath.twitterstream.model;


import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.IMAGE_TWEET;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.PHOTO;
import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.PLAIN_TWEET;

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

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public static class ViewHolderImage extends RecyclerView.ViewHolder {
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

        public ViewHolderImage(View itemView) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case PLAIN_TWEET:
                View plainView = inflater.inflate(R.layout.single_tweet, parent, false);
                viewHolder = new ViewHolder(plainView);
                break;
            case IMAGE_TWEET:
                View imageVIew = inflater.inflate(R.layout.single_tweet_image, parent, false);
                viewHolder = new ViewHolderImage(imageVIew);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case PLAIN_TWEET:
                ViewHolder plainHolder = (ViewHolder) holder;
                configurePlainViewHolder(plainHolder, position);
                break;
            case IMAGE_TWEET:
                ViewHolderImage imageHolder = (ViewHolderImage) holder;
                configureImageViewHolder(imageHolder, position);
                break;
        }
    }

    private void configureImageViewHolder(ViewHolderImage holder, int position) {
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
        Picasso.with(holder.mUrlImagePreview.getContext())
                .load(singleTweet.getMediaUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(holder.mUrlImagePreview);
    }

    private void configurePlainViewHolder(ViewHolder holder, int position) {
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
    public int getItemViewType(int position) {
        if ((mTweets.get(position).getMediaUrl() == null || mTweets.get(
                position).getMediaUrl().isEmpty()) && !mTweets.get(position).getType().equals(
                PHOTO)) {
            return PLAIN_TWEET;
        } else {
            return IMAGE_TWEET;
        }
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
