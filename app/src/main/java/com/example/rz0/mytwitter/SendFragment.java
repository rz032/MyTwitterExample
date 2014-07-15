package com.example.rz0.mytwitter;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class SendFragment extends SherlockFragment {

    final String TWITTER_CONSUMER_KEY = "Nh6hXHX3i8iOyFUNQN4F31sv1";
    final String TWITTER_CONSUMER_SECRET = "wQl71RC6hiEdsBFYvok5eAqt2JYXUtTZvuyuCPoUWYBofCgcO4";

    String access_token, access_secret;

    EditText message, signature;
    Button postButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send, container, false);

        message = (EditText) rootView.findViewById(R.id.messageText);
        signature = (EditText) rootView.findViewById(R.id.signatureText);
        postButton = (Button) rootView.findViewById(R.id.sendButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TwitterPosting().execute();
            }
        });

        Bundle extras = getActivity().getIntent().getExtras();
        access_token = extras.getString("access_token");
        access_secret = extras.getString("access_secret");

        getSherlockActivity().getSupportActionBar().setTitle("Новое сообщение");

        //new TwitterPosting().execute();

        return rootView;
    }


    class TwitterPosting extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //        //if (!StringUtil.isNullOrWhitespace(accessTokenString) && !StringUtil.isNullOrWhitespace(accessTokenSecret)) {
            AccessToken accessToken = new AccessToken(access_token, access_secret);
//            twitter4j.Status status = TwitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).updateStatus(params[0]);
            //Instantiate a new Twitter instance
            TwitterFactory twitterFactory = new TwitterFactory();
            Twitter twitter = twitterFactory.getInstance();
            twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
            twitter.setOAuthAccessToken(accessToken);

            //Instantiate and initialize a new twitter status update
            StatusUpdate statusUpdate = new StatusUpdate(
                    //your tweet or status message
                    signature.getText().toString() +
                            message.getText().toString());
            //attach any media, if you want to

            //tweet or update status
            try
            {
                twitter.updateStatus(statusUpdate);
            }

            catch (TwitterException e)
            {
                System.out.println(e.toString());
            }

            //}
            return "1";
        }
    }




}