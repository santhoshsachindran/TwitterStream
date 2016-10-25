package com.santhosh.codepath.twitterstream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.santhosh.codepath.twitterstream.R;
import com.santhosh.codepath.twitterstream.oauth.TwitterRestClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TwitterLoginActivity extends OAuthLoginActionBarActivity<TwitterRestClient> {

    @BindView(R.id.login)
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginToRest();
            }
        });
    }

    private void loginToRest() {
        getClient().connect();
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, TwitterTimeline.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(Exception e) {

    }
}
