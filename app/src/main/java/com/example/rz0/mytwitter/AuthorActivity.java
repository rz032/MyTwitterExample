package com.example.rz0.mytwitter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class AuthorActivity extends Activity {

    final static String APIKEY = "Nh6hXHX3i8iOyFUNQN4F31sv1";
    final static String APISECRET = "wQl71RC6hiEdsBFYvok5eAqt2JYXUtTZvuyuCPoUWYBofCgcO4";
    final static String CALLBACK = "oauth://twitter";

    OAuthService mService;
    Token requestToken;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mService  = new ServiceBuilder()
                .provider(TwitterApi.SSL.class)
                .apiKey(APIKEY)
                .apiSecret(APISECRET)
                .callback(CALLBACK)
                .build();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        webView = (WebView) findViewById(R.id.webView);

        new TwitterInAuthTask().execute();



    }

    String getUserName(String access_token, String access_secret)
    {

        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(APIKEY, APISECRET);
        AccessToken a = new AccessToken(access_token,access_secret);
        twitter.setOAuthAccessToken(a);

        try
        {
            String username = twitter.getScreenName();
            return username;
        }

        catch (TwitterException e)
        {
            System.out.println(e.toString());
            return e.toString();
        }
    }

    private static void storeAccessToken(int useId, AccessToken accessToken){
        //store accessToken.getToken()
        //store accessToken.getTokenSecret()
    }

    private class TwitterInAuthTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... arg0) {

            // Temporary URL
            String authURL = "https://api.twitter.com/";

            try
            {
                requestToken = mService.getRequestToken();
                authURL = mService.getAuthorizationUrl(requestToken);
            }
            catch ( OAuthException e )
            {
                e.printStackTrace();
                return null;
            }

            return authURL;
        }

        @Override
        protected void onPostExecute(String authURL) {
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    super.shouldOverrideUrlLoading(view, url);

                    if( url.startsWith("oauth") )
                    {
                        webView.setVisibility(WebView.GONE);
                        //Toast.makeText(getApplicationContext(), "Успешная авторизация", Toast.LENGTH_SHORT);//не работает

                        final String url1 = url;
                        Thread t1 = new Thread() {
                            public void run() {
                                Uri uri = Uri.parse(url1);

                                String verifier = uri.getQueryParameter("oauth_verifier");
                                Verifier v = new Verifier(verifier);
                                Token accessToken = mService.getAccessToken(requestToken, v);

                                String user = getUserName(accessToken.getToken(), accessToken.getSecret());
                                Intent intent = new Intent(AuthorActivity.this,MessagesActivity.class);
                                intent.putExtra("access_token", accessToken.getToken());
                                intent.putExtra("access_secret", accessToken.getSecret());
                                intent.putExtra("username", user);

                                startActivity(intent);

                                //finish();
                            }
                        };
                        t1.start();
                        return false;
                    }

                    else{
                        //Toast.makeText(getApplicationContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT);
                        return true;
                    }

                    //return false;
                }
            });

            webView.loadUrl(authURL);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.author, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}