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
    public final static String BASE_URL = "https://api.themoviedb.org/3";
    public final static String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
    public final static String API_KEY = "api_key";

    public final static String POPULAR_MOVIE_URL = "/popular";
    public final static String TOPRATED_MOVIE_URL = "/top_rated";
    /**  /movie/{id}/videos **/
    public final static String MOVIE_TRAILER_URL = "/videos";
    /**** /movie/{id}/reviews *****/
    public final static String MOVIE_REVIEWS_URL = "/reviews";

    public final static String IMAGE_POSTER_BASE_URL = "http://image.tmdb.org/t/p";

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

    public static String buildPosterUrl(String imagePath) {

        Uri.Builder posterbuiltUri = new Uri.Builder();
        String strPathTemp = imagePath;
        posterbuiltUri.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(strPathTemp.substring(1));
        String posterUrl = posterbuiltUri.toString();
        return posterUrl;
    }
}