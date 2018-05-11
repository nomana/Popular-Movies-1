package com.example.android.popularmovies1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies1.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private TextView mSearchTextView;

    private TextView mUrlDisplayTextView;

    private TextView mUrlResultsTextView;

    private TextView mTopResult;

    private String jsonFromUrl;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;

    final static int SORT_POPULARITY_KEY = 1;
    final static int SORT_RATING_KEY = 2;

    final static String API_KEY = "ca1eda3d7d2727738ebbeffcde814ed8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mSearchTextView = (TextView) findViewById(R.id.search_action);
        //mUrlDisplayTextView = (TextView) findViewById(R.id.url_display);
        //mUrlResultsTextView = (TextView) findViewById(R.id.url_results);
        //mTopResult = (TextView) findViewById(R.id.first);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter();

        mRecyclerView.setAdapter(mMovieAdapter);

        makeUrlQuery(SORT_POPULARITY_KEY);
    }

    private void makeUrlQuery(int sortKey) {
        URL popularUrl = NetworkUtils.buildPopularUrl(API_KEY);
        URL topRatedUrl = NetworkUtils.buildTopRatedUrl(API_KEY);
        if(sortKey == 1) {
            //mUrlDisplayTextView.setText(popularUrl.toString());
            new UrlQueryTask().execute(popularUrl);
        }
        else if(sortKey == 2) {
            //mUrlDisplayTextView.setText(topRatedUrl.toString());
            new UrlQueryTask().execute(topRatedUrl);
        }

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.main_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(long orderNumber) {
        /*
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        Uri uriForDateClicked = WeatherContract.WeatherEntry.buildWeatherUriWithDate(date);
        weatherDetailIntent.setData(uriForDateClicked);
        startActivity(weatherDetailIntent);
        */
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
                //mUrlResultsTextView.setText(s);
                jsonFromUrl = s;
                try {
                    JSONObject json = new JSONObject(jsonFromUrl);
                    JSONArray results = json.getJSONArray("results");

                    List<String> voteCountsList = new ArrayList<>();
                    List<String> idList = new ArrayList<>();
                    List<String> videoList = new ArrayList<>();
                    List<String> voteAverageList = new ArrayList<>();
                    List<String> titleList = new ArrayList<>();
                    List<String> popularityList = new ArrayList<>();
                    List<String> posterPathList = new ArrayList<>();
                    List<String> originalLanguageList = new ArrayList<>();
                    List<String> originalTitleList = new ArrayList<>();
                    List<String> genreIdsList = new ArrayList<>();
                    List<String> backdropPathList = new ArrayList<>();
                    List<String> adultList = new ArrayList<>();
                    List<String> overviewList = new ArrayList<>();
                    List<String> releaseDateList = new ArrayList<>();

                    int resultsLengh = results.length();

                    for(int i = 0; i<resultsLengh; i++) {
                        JSONObject focus = results.getJSONObject(i);

                        voteCountsList.add(focus.getString("vote_count"));
                        idList.add(focus.getString("id"));
                        videoList.add(focus.getString("video"));
                        voteAverageList.add(focus.getString("vote_average"));
                        titleList.add(focus.getString("title"));
                        popularityList.add(focus.getString("popularity"));
                        posterPathList.add(focus.getString("poster_path"));
                        originalLanguageList.add(focus.getString("original_language"));
                        originalTitleList.add(focus.getString("original_title"));
                        genreIdsList.add(focus.getString("genre_ids"));
                        backdropPathList.add(focus.getString("backdrop_path"));
                        adultList.add(focus.getString("adult"));
                        overviewList.add(focus.getString("overview"));
                        releaseDateList.add(focus.getString("release_date"));
                    }
                    //first.add(results.getString(0));
                    String voteCounts = android.text.TextUtils.join(", ", voteCountsList);
                    String id = android.text.TextUtils.join(", ", idList);
                    String video = android.text.TextUtils.join(", ", videoList);
                    String voteAverage = android.text.TextUtils.join(", ", voteAverageList);
                    String title = android.text.TextUtils.join(", ", titleList);
                    String popularity = android.text.TextUtils.join(", ", popularityList);
                    String posterPath = android.text.TextUtils.join(", ", posterPathList);
                    String originalLanguage = android.text.TextUtils.join(", ", originalLanguageList);
                    String originalTitle = android.text.TextUtils.join(", ", originalTitleList);
                    String genreIds = android.text.TextUtils.join(", ", genreIdsList);
                    String backdropPath = android.text.TextUtils.join(", ", backdropPathList);
                    String adult = android.text.TextUtils.join(", ", adultList);
                    String overview = android.text.TextUtils.join(", ", overviewList);
                    String releaseDate = android.text.TextUtils.join(", ", releaseDateList);

                    String text = voteCounts;
                    //mTopResult.setText(title+"\n\n"+voteAverage+"\n\n"+popularity);

                    //mTopResult.setText(voteCounts+"\n\n"+id+"\n\n"+video+"\n\n"+voteAverage+"\n\n"+title+"\n\n"+popularity+"\n\n"+posterPath+"\n\n"+originalLanguage+"\n\n"+originalTitle+"\n\n"+genreIds+"\n\n"+backdropPath+"\n\n"+adult+"\n\n"+overview+"\n\n"+releaseDate);

                    mMovieAdapter.setData(posterPathList, voteAverageList, popularityList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //Create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //When menu clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_sort_popularity) {
            makeUrlQuery(SORT_POPULARITY_KEY);
            return true;
        }
        else if (itemThatWasClickedId == R.id.action_sort_rating) {
            makeUrlQuery(SORT_RATING_KEY);
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
