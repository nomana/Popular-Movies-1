package com.example.android.popularmovies1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //API Key (v3 auth)
    //ca1eda3d7d2727738ebbeffcde814ed8

    //API Read Access Token (v4 auth)
    //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjYTFlZGEzZDdkMjcyNzczOGViYmVmZmNkZTgxNGVkOCIsInN1YiI6IjVhZWE4MjEzYzNhMzY4MDk1MTAwMWM5YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.6fZr3LvXES3_nHYBrHb1T3m6HQqKhVPjQUz1x3-ljNM

    //Example API Request
    //https://api.themoviedb.org/3/movie/550?api_key=ca1eda3d7d2727738ebbeffcde814ed8
}
