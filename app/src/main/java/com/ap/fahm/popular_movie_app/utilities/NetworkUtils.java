package com.ap.fahm.popular_movie_app.utilities;

import android.net.Uri;

import com.ap.fahm.popular_movie_app.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Faheem on 14/07/17.
 */

public class NetworkUtils {

    public final static  String SELECTED_MOVIE = "SELECTED_MOVIE";
    private final static String BASE_URL = "https://api.themoviedb.org/3";
    private final static String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
    private final static String API_KEY = "api_key";

    private final static String POPULAR_MOVIE_URL = "/popular";
    private final static String TOPRATED_MOVIE_URL = "/top_rated";
    /**  /movie/{id}/videos **/
    private final static String MOVIE_TRAILER_URL = "/videos";
    /**** /movie/{id}/reviews *****/
    private final static String MOVIE_REVIEWS_URL = "/reviews";

    private final static String IMAGE_POSTER_BASE_URL = "http://image.tmdb.org/t/p";

    /**
     * Builds the URL to optain poplar movies.
     *
     * @return The URL to use to query the GitHub.
     */
    public static URL getPopularMovieURL() {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + POPULAR_MOVIE_URL).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Log.d("URL getPOPULARURL",url.toString());
        return url;
    }

    /**
     * Builds the URL to optain poplar movies.
     *
     * @return The URL to use to query the GitHub.
     */
    public static URL getTopRatedMovieURL() {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + TOPRATED_MOVIE_URL).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL getMovieTrailersURL(long movieId) {

        Uri builtUri = Uri.parse(MOVIE_BASE_URL+"/"+movieId+MOVIE_TRAILER_URL).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL getMovieReviewsURL(long movieId) {

        Uri builtUri = Uri.parse(MOVIE_BASE_URL+"/"+movieId+MOVIE_REVIEWS_URL).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String buildPosterUrl(String imagePath, int width) {
        String widthPath;

        if (width <= 92)
            widthPath = "/w92";
        else if (width <= 154)
            widthPath = "/w154";
        else if (width <= 185)
            widthPath = "/w185";
        else if (width <= 342)
            widthPath = "/w342";
        else if (width <= 500)
            widthPath = "/w500";
        else
            widthPath = "/w780";

        //Timber.v("buildPosterUrl: widthPath=" + widthPath);
        return IMAGE_POSTER_BASE_URL + widthPath + imagePath;
    }
}