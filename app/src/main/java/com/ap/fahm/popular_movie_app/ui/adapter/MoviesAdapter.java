package com.ap.fahm.popular_movie_app.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ap.fahm.popular_movie_app.AppController;
import com.ap.fahm.popular_movie_app.R;
import com.ap.fahm.popular_movie_app.data.model.Movie;
import com.ap.fahm.popular_movie_app.utilities.NetworkUtils;

import java.util.ArrayList;

/**
 * Created by Faheem on 11/07/17.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {

    private ArrayList<Movie> movies;
    private LayoutInflater mInflater;
    private Context mContext;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
        mContext = context;
        this.movies = movies;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (mInflater == null)
            mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.movie_gridviewitem, parent, false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();


        NetworkImageView poster_img = convertView
                .findViewById(R.id.image_movieItem);
        Movie movie = movies.get(position);


        poster_img.setImageUrl(NetworkUtils.buildPosterUrl(movie.getPosterPath()), imageLoader);

        return convertView;
    }

}