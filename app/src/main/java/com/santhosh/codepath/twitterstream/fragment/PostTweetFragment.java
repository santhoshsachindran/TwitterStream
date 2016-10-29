package com.santhosh.codepath.twitterstream.fragment;

import static com.santhosh.codepath.twitterstream.utils.UtilsAndConstants.USER_NAME_EXTRA;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.santhosh.codepath.twitterstream.R;
import com.santhosh.codepath.twitterstream.activity.TwitterApplication;
import com.santhosh.codepath.twitterstream.listener.TweetListener;
import com.santhosh.codepath.twitterstream.oauth.TwitterRestClient;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PostTweetFragment extends DialogFragment {
    @BindView(R.id.close_compose)
    ImageButton mCloseCompose;
    @BindView(R.id.user_image)
    ImageView mUserImage;
    @BindView(R.id.tweet_compose)
    EditText mTweetCompose;
    @BindView(R.id.character_count)
    TextView mCharacterCount;
    @BindView(R.id.tweet_button)
    Button mTweetButton;

    TweetListener mTweetListener;
    private String mUserHandle;

    public PostTweetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserHandle = getArguments().getString(USER_NAME_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_tweet, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCloseCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTweetCompose.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.discard_tweet)
                            .setMessage(R.string.are_you_sure)
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            quitCompose();
                                        }
                                    })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else {
                    quitCompose();
                }
            }
        });

        if (mUserHandle != null && !mUserHandle.isEmpty()) {
            String toHandle = getString(R.string.at_symbol, mUserHandle + " ");
            mTweetCompose.setText(toHandle);
            mTweetCompose.setSelection(toHandle.length());
        }

        mTweetCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int remaining_item = 140 - charSequence.length();

                mCharacterCount.setText(String.valueOf(remaining_item));
                if (remaining_item < 0) {
                    mCharacterCount.setTextColor(
                            getResources().getColor(android.R.color.holo_red_light));
                    mTweetButton.setEnabled(false);
                    mTweetButton.setBackgroundColor(
                            getResources().getColor(android.R.color.darker_gray));
                } else {
                    mCharacterCount.setTextColor(
                            getResources().getColor(android.R.color.black));
                    mTweetButton.setEnabled(true);
                    mTweetButton.setBackgroundColor(
                            getResources().getColor(android.R.color.holo_blue_light));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterRestClient client = TwitterApplication.getRestClient();
                if (!mTweetCompose.getText().toString().isEmpty()) {
                    client.postTweet(mTweetCompose.getText().toString(),
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers,
                                        JSONObject response) {
                                    Log.d("DEBUG", "Post success: " + statusCode);
                                    if (mTweetListener != null) {
                                        mTweetListener.newTweet();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers,
                                        Throwable throwable,
                                        JSONObject errorResponse) {
                                    Log.d("DEBUG", "Post Failed: " + statusCode);
                                }
                            });
                    dismiss();
                } else {
                    Toast.makeText(getContext(), R.string.empty_tweet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setTweetListener(TweetListener tweetListener) {
        mTweetListener = tweetListener;
    }

    private void quitCompose() {
        dismiss();
    }
}
