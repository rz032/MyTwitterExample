package com.example.rz0.mytwitter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class ListFragment extends SherlockFragment {

    private static final String ID = "id";
    private static final String TEXT = "text";
    private static final String DATE = "date";
    private static ArrayList<HashMap<String, Object>> myBooks;
    public ListView listView;
    SherlockFragmentActivity f;

    String access_token, access_secret, user;

    ArrayList<twitter4j.Status> tweetStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);


        myBooks = new ArrayList<HashMap<String, Object>>();
        setHasOptionsMenu(true);

        Bundle extras = getActivity().getIntent().getExtras();
        access_token = extras.getString("access_token");
        access_secret = extras.getString("access_secret");
        user = extras.getString("username");


        getSherlockActivity().getSupportActionBar().setTitle("Список сообщений");

        //new GetTweets().execute();

        //FillList(tweetStatus);
        return rootView;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        f = getSherlockActivity();
        new GetTweets().execute();
    }

    void FillList(ArrayList<twitter4j.Status> tweets)
    {
        for(int i = 0; i<tweets.size(); i++){
            HashMap<String, Object> hm;
            hm = new HashMap<String, Object>();

            hm.put(ID, String.valueOf(tweets.get(i).getId()));
            hm.put(TEXT, tweets.get(i).getText());
            hm.put(DATE, tweets.get(i).getCreatedAt());

            myBooks.add(hm);


            SimpleAdapter adapter = new SimpleAdapter(f, myBooks, R.layout.list,
                    new String[] {ID, TEXT, DATE}, new int[] {R.id.id, R.id.text_message, R.id.data_message});

            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setAdapter(adapter);

        }

    }

    class GetTweets extends AsyncTask<String, String, ArrayList<twitter4j.Status>> {

        @Override
        protected void onPostExecute(ArrayList<twitter4j.Status> tweets) {
            FillList(tweets);
        }

        @Override
        protected ArrayList<twitter4j.Status> doInBackground(String... params) {
            Paging pg = new Paging();
            String userName = user;

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey("Nh6hXHX3i8iOyFUNQN4F31sv1");
            cb.setOAuthConsumerSecret("wQl71RC6hiEdsBFYvok5eAqt2JYXUtTZvuyuCPoUWYBofCgcO4");
            cb.setOAuthAccessToken(access_token);
            cb.setOAuthAccessTokenSecret(access_secret);

            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            int numberOfTweets = 3;
            long lastID = Long.MAX_VALUE;
            ArrayList<twitter4j.Status> tweets = new ArrayList<twitter4j.Status>();


            while (tweets.size () < numberOfTweets) {
                try {
                    tweets.addAll(twitter.getUserTimeline(userName,pg));
                    System.out.println("Gathered " + tweets.size() + " tweets");
                    for (twitter4j.Status t: tweets)
                        if(t.getId() < lastID) lastID = t.getId();


                }
                catch (TwitterException te) {
                    System.out.println("Couldn't connect: " + te);
                };
                pg.setMaxId(lastID-1);
            }

            tweetStatus = tweets;
            return tweets;
        }
        }


    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.action_back:
                String selected = "";
                int cntChoice = listView.getCount();

                SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
                for(int i = 0; i < cntChoice; i++){
                    if(sparseBooleanArray.get(i)) {
                        selected += listView.getItemAtPosition(i).toString() + "\n";

                    }

                }

                Toast.makeText(getActivity(), selected, Toast.LENGTH_LONG).show();


                return true;
            case R.id.action_delete:
                System.out.println("DELETE");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.messages, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
