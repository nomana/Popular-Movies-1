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

/**
 * Created by Joshua on 5/11/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private ImageView mPoster;
    private ImageView mBackdrop;

    private TextView mVoteAverage;
    private TextView mOverview;
    private TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = (ImageView) findViewById(R.id.detail_poster);
        mBackdrop = (ImageView) findViewById(R.id.detail_backdrop);

        mVoteAverage = (TextView) findViewById(R.id.detail_rating);
        mOverview = (TextView) findViewById(R.id.detail_overview);
        mReleaseDate = (TextView) findViewById(R.id.detail_release_date);

        Intent intentFromMainActivity = getIntent();

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
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

            mVoteAverage.setText(Html.fromHtml("<b>Rating:</b><br><br>"+rating+" / 10"));
            mOverview.setText(Html.fromHtml("<b>Plot Summary:</b><br><br>"+overview));
            mReleaseDate.setText(Html.fromHtml("<b>Release date:</b><br><br>"+releaseDate));

            setTitle(title);
        }
    }

}
