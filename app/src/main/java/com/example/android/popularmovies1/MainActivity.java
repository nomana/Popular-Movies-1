package com.example.android.popularmovies1;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

    private List<String> mVoteCounts = new ArrayList<>();
    private List<String> mVoteAverage = new ArrayList<>();
    private List<String> mTitle = new ArrayList<>();
    private List<String> mPoster = new ArrayList<>();
    private List<String> mBackdropPath = new ArrayList<>();
    private List<String> mOverview = new ArrayList<>();
    private List<String> mReleaseDate = new ArrayList<>();

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;

    final static int SORT_POPULARITY_KEY = 1;
    final static int SORT_RATING_KEY = 2;

    private static final int NUMBER_OF_ITEMS = 100;

    final static String API_KEY = "ca1eda3d7d2727738ebbeffcde814ed8";

    private Toast mToast;

    final static int SORT_POPULARITY_INDEX = 0;
    final static int SORT_RATING_INDEX = 1;

    //ImageView mStar = (ImageView)findViewById(R.id.star);
    //mStar.setImageResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int gridSpan = 2;
        if(width > 2000) {
            gridSpan = 4;
        }
        else if(width > 1500) {
            gridSpan = 3;
        }


        GridLayoutManager layoutManager = new GridLayoutManager(this, gridSpan);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(NUMBER_OF_ITEMS,this);

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
        mToast.makeText(this, R.string.main_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int position) {

        /*if(mToast!=null) {
            mToast.cancel();
        }
        String message = "Movie "+(position+1)+" selected";
        mToast.makeText(this, message, Toast.LENGTH_SHORT).show();*/

        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.putExtra("EXTRA_RATING_COUNT", mVoteCounts.get(position));
        movieDetailIntent.putExtra("EXTRA_RATING", mVoteAverage.get(position));
        movieDetailIntent.putExtra("EXTRA_TITLE", mTitle.get(position));
        movieDetailIntent.putExtra("EXTRA_POSTER", mPoster.get(position));
        movieDetailIntent.putExtra("EXTRA_BACKDROP", mBackdropPath.get(position));
        movieDetailIntent.putExtra("EXTRA_OVERVIEW", mOverview.get(position));
        movieDetailIntent.putExtra("EXTRA_RELEASE_DATE", mReleaseDate.get(position));
        startActivity(movieDetailIntent);

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
                    mVoteCounts = voteCountsList;
                    mVoteAverage = voteAverageList;
                    mTitle = titleList;
                    mPoster = posterPathList;
                    mBackdropPath = backdropPathList;
                    mOverview = overviewList;
                    mReleaseDate = releaseDateList;
                    mMovieAdapter.setData(posterPathList, voteAverageList, popularityList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private Menu menu1 = null;

    //Create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //TextView view = (TextView) findViewById(R.id.action_sort_popularity);
        //view.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        menu1 = menu;

        //Set text color of "By Popularity" menu to colorAccent
        MenuItem item = menu.getItem(SORT_POPULARITY_INDEX);
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        return true;
    }

    private Menu getMenu() {
        return menu1;
    }

    //When menu clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_sort_popularity) {

            //Set text color of "By Popularity" menu to colorAccent
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);

            //Set text color of "By Rating" menu to white
            MenuItem item2 = getMenu().getItem(SORT_RATING_INDEX);
            SpannableString spanString2 = new SpannableString(item2.getTitle().toString());
            spanString2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spanString2.length(), 0); //fix the color to white
            item2.setTitle(spanString2);

            makeUrlQuery(SORT_POPULARITY_KEY);
            return true;
        }
        else if (itemThatWasClickedId == R.id.action_sort_rating) {

            //Set text color of "By Rating" menu to colorAccent
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);

            //Set text color of "By Popularity" menu to white
            MenuItem item2 = getMenu().getItem(SORT_POPULARITY_INDEX);
            SpannableString spanString2 = new SpannableString(item2.getTitle().toString());
            spanString2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spanString2.length(), 0); //fix the color to white
            item2.setTitle(spanString2);

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
