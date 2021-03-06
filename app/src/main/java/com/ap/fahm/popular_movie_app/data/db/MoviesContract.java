

package com.ap.fahm.popular_movie_app.data.db;


import android.net.Uri;
import android.provider.BaseColumns;


public final class MoviesContract {

    public static final String QUERY_PARAMETER_DISTINCT = "distinct";
    public static final String CONTENT_AUTHORITY = "com.ap.fahm.popular_movie_app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_MOVIES = "movies";

    private MoviesContract() {
        throw new AssertionError("No instances.");
    }

    public interface MoviesColumns {

        String MOVIE_ID = "movie_id";
        String MOVIE_TITLE = "movie_title";
        String MOVIE_OVERVIEW = "movie_overview";
        String MOVIE_POPULARITY = "movie_popularity";
        String MOVIE_GENRE_IDS = "movie_genre_ids";
        String MOVIE_VOTE_COUNT = "movie_vote_count";
        String MOVIE_VOTE_AVERAGE = "movie_vote_average";
        String MOVIE_RELEASE_DATE = "movie_release_date";
        String MOVIE_VIDEO = "movie_video";
        String MOVIE_FAVORED = "movie_favored";
        String MOVIE_POSTER_PATH = "movie_poster_path";
        String MOVIE_BACKDROP_PATH = "movie_backdrop_path";
    }

    public static final class Movies implements MoviesColumns,BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        /**
         * Build {@link Uri} for requested {@link #MOVIE_ID}.
         */
        public static Uri buildMovieUri(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();}

    }

    public interface Tables {
        String MOVIES = "movies";
    }


}
