package com.example.android.popularmovies1;

/**
 * Created by Joshua on 5/6/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import com.example.android.popularmovies1.utilities.NetworkUtils;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    //private final Context mContext;

    //private Cursor mCursor;

    final private MovieAdapterOnClickHandler mClickHandler;
    private int mPosition;

    private List<String> mPoster;
    private List<String> mRating;

    public interface MovieAdapterOnClickHandler {
        void onClick(int position);
    }

    public MovieAdapter(int position, MovieAdapterOnClickHandler clickHandler) {
        mPosition = position;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_list_item;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {

        Context context = holder.posterView.getContext();

        //mCursor.moveToPosition(position);

        /*Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = holder.posterView.getWidth();

                double aspectRatio = (double) 780 / (double) 1170;
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };*/

        URL imageURL = NetworkUtils.buildImageUrl(mPoster.get(position),"w780");

        Picasso.with(context)
                .load(imageURL.toString())
                //.transform(transformation)
                .placeholder(R.drawable.placeholder_poster)
                .error(R.drawable.no_image_poster)
                .into(holder.posterView);

        holder.ratingView.setText(mRating.get(position));

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


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView posterView;
        final TextView ratingView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            posterView = (ImageView) itemView.findViewById(R.id.movie_poster);
            ratingView = (TextView) itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(clickedPosition);
        }

        /*void bind(int listIndex) {
                ratingView.setText(String.valueOf(listIndex));
            }*/


    }

    public void setData(List<String> posterPath, List<String> voteAverageList, List<String> popularityList) {
        mPoster = posterPath;
        mRating = voteAverageList;
        //int adapterPosition = getAdapterPosition();
        //mCursor.moveToPosition(adapterPosition);
        //mClickHandler.onClick(mCursor.getPosition());
        notifyDataSetChanged();
    }


}
