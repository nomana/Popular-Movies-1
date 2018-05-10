package com.example.android.popularmovies1;

/**
 * Created by Joshua on 5/6/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies1.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    //private final Context mContext;

    private Cursor mCursor;

    //final private MovieAdapterOnClickHandler mClickHandler;

    private List<String> mPoster;
    private List<String> mRating;
    private List<String> mPopularity;

    public interface MovieAdapterOnClickHandler {
        void onClick(long orderNumber);
    }

    /*public MovieAdapter(@NonNull Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }*/

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_list_item;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        //mCursor.moveToPosition(position);

        List<String> posterPathList = new ArrayList<>();
        posterPathList.add("/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg");
        URL imageURL = NetworkUtils.buildImageUrl(posterPathList.get(0));

        //holder.posterView.setImageResource(position);

        holder.ratingView.setText(mRating.get(position));

        holder.popularityView.setText(mPopularity.get(position));

        //holder.bind(position);
    }

    @Override
    public int getItemCount() {
        /*if (null == mCursor) return 0;
        return mCursor.getCount();*/
        if(mRating==null) {
            return 0;
        }
        else {
            return mRating.size();
        }
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {

        final ImageView posterView;
        final TextView ratingView;
        final TextView popularityView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            posterView = (ImageView) itemView.findViewById(R.id.movie_poster);
            ratingView = (TextView) itemView.findViewById(R.id.rating);
            popularityView = (TextView) itemView.findViewById(R.id.popularity);

            //itemView.setOnClickListener(this);
        }

        /*void bind(int listIndex) {
                ratingView.setText(String.valueOf(listIndex));
            }*/


    }

    public void setData(List<String> posterPath, List<String> voteAverageList, List<String> popularityList) {
        mPoster = posterPath;
        mRating = voteAverageList;
        mPopularity = popularityList;
        //int adapterPosition = getAdapterPosition();
        //mCursor.moveToPosition(adapterPosition);
        //mClickHandler.onClick(mCursor.getPosition());
        notifyDataSetChanged();
    }
}
