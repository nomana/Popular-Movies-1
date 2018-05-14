package com.example.android.popularmovies1;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Joshua on 5/11/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private ImageView mPoster;
    private ImageView mBackdrop;

    private TextView mVoteCounts;
    private TextView mVoteAverage;
    private TextView mOverview;
    private TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = (ImageView) findViewById(R.id.detail_poster);
        mBackdrop = (ImageView) findViewById(R.id.detail_backdrop);

        mVoteCounts = (TextView) findViewById(R.id.detail_vote_count);
        mVoteAverage = (TextView) findViewById(R.id.detail_rating);
        mOverview = (TextView) findViewById(R.id.detail_overview);
        mReleaseDate = (TextView) findViewById(R.id.detail_release_date);

        Intent intentFromMainActivity = getIntent();

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            String ratingCount = intentFromMainActivity.getStringExtra("EXTRA_RATING_COUNT");
            String rating = intentFromMainActivity.getStringExtra("EXTRA_RATING");
            String title = intentFromMainActivity.getStringExtra("EXTRA_TITLE");
            String posterPath = intentFromMainActivity.getStringExtra("EXTRA_POSTER");
            String backdrop = intentFromMainActivity.getStringExtra("EXTRA_BACKDROP");
            String overview = intentFromMainActivity.getStringExtra("EXTRA_OVERVIEW");
            String releaseDate = intentFromMainActivity.getStringExtra("EXTRA_RELEASE_DATE");

            URL posterURL = NetworkUtils.buildImageUrl(posterPath,"w500");
            URL backdropURL = NetworkUtils.buildImageUrl(backdrop,"original");

            Picasso.with(this)
                    .load(posterURL.toString())
                    .into(mPoster);

            Picasso.with(this)
                    .load(backdropURL.toString())
                    .into(mBackdrop);

            //Set Rating
            mVoteAverage.setText(rating + " / 10");

            //Set Vote Count
            int ratingCountInt = Integer.parseInt(ratingCount);
            ratingCount = NumberFormat.getIntegerInstance(Locale.US).format(ratingCountInt);
            mVoteCounts.setText(ratingCount + " votes");

            //Set Plot Summary
            mOverview.setText(overview);

            //Format Date
            SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = oldDateFormat.parse(releaseDate);
            } catch (ParseException e) {

                e.printStackTrace();
            }
            SimpleDateFormat dateFormater = new SimpleDateFormat("MMMM dd, yyyy");
            String outputDate = dateFormater.format(date);

            //Set Release Date
            mReleaseDate.setText(outputDate);

            //Set header text to the movie title
            setTitle(title);
        }
    }

}
