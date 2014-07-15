package com.example.rz0.mytwitter;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import android.support.v4.app.Fragment;
import android.webkit.WebView;
import org.scribe.model.Token;

import org.scribe.oauth.OAuthService;

public class MessagesActivity extends SherlockFragmentActivity {

    ActionBar.Tab Tab1,Tab2;
    Fragment fragmentTab1 = new SendFragment();
    Fragment fragmentTab2 = new ListFragment();

    final static String APIKEY = "Nh6hXHX3i8iOyFUNQN4F31sv1";
    final static String APISECRET = "wQl71RC6hiEdsBFYvok5eAqt2JYXUtTZvuyuCPoUWYBofCgcO4";

    private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    OAuthService service;
    Token requestToken;
    WebView webView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        ActionBar actionBar = getSupportActionBar();

        Bundle bundle = new Bundle();
        bundle.putString("access_token",getIntent().getStringExtra("access_token"));
        bundle.putString("access_secret",getIntent().getStringExtra("access_secret"));
        bundle.putString("username",getIntent().getStringExtra("username"));


        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setDisplayShowTitleEnabled(true);

        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        Tab1 = actionBar.newTab().setText("Отправить");
        Tab2 = actionBar.newTab().setText("Просмотреть");


        // Set Tab Listeners
        Tab1.setTabListener(new TabListener(fragmentTab1));
        Tab2.setTabListener(new TabListener(fragmentTab2));

        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
    }

    }