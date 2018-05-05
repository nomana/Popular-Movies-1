package com.example.android.popularmovies1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies1.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mSearchTextView;

    private TextView mUrlDisplayTextView;

    private TextView mUrlResultsTextView;

    private TextView mTopResult;

    private String jsonFromUrl;

    final static String API_KEY = "ca1eda3d7d2727738ebbeffcde814ed8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchTextView = (TextView) findViewById(R.id.search_action);
        mUrlDisplayTextView = (TextView) findViewById(R.id.url_display);
        mUrlResultsTextView = (TextView) findViewById(R.id.url_results);
        mTopResult = (TextView) findViewById(R.id.first);

    }

    private void makeUrlQuery() {
        URL popularUrl = NetworkUtils.buildPopularUrl(API_KEY);
        URL topRatedUrl = NetworkUtils.buildTopRatedUrl(API_KEY);
        mUrlDisplayTextView.setText(popularUrl.toString());

        new UrlQueryTask().execute(popularUrl);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.main_error_message, Toast.LENGTH_SHORT).show();
    }

    public class UrlQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];

            String urlResults = null;
            try {
                urlResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return urlResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                mUrlResultsTextView.setText(s);
                jsonFromUrl = s;
                try {
                    JSONObject json = new JSONObject(jsonFromUrl);
                    JSONArray results = json.getJSONArray("results");
                    List<String> first = new ArrayList<>();
                    first.add(results.getString(0));
                    String firstString = android.text.TextUtils.join(", ", first);
                    mTopResult.setText(firstString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_sort) {
            makeUrlQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //API Key (v3 auth)
    //ca1eda3d7d2727738ebbeffcde814ed8

    //API Read Access Token (v4 auth)
    //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjYTFlZGEzZDdkMjcyNzczOGViYmVmZmNkZTgxNGVkOCIsInN1YiI6IjVhZWE4MjEzYzNhMzY4MDk1MTAwMWM5YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.6fZr3LvXES3_nHYBrHb1T3m6HQqKhVPjQUz1x3-ljNM

    //Example API Request
    //https://api.themoviedb.org/3/movie/550?api_key=ca1eda3d7d2727738ebbeffcde814ed8
}
