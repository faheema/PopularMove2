package com.ap.fahm.popular_movie_app.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.ap.fahm.popular_movie_app.data.db.MoviesContract.MoviesColumns;

/**
 * Created by Faheem on 28/08/17.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "fav_movies.db";
        private static final int DB_VERSION = 1;

        private final Context mContext;

        public MoviesDBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        public static void deleteDatabase(Context context) {
            context.deleteDatabase(DB_NAME);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + MoviesContract.Tables.MOVIES + "("
                    + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                    + MoviesColumns.MOVIE_ID + " TEXT NOT NULL,"
                    + MoviesColumns.MOVIE_TITLE + " TEXT NOT NULL,"
                    + MoviesColumns.MOVIE_OVERVIEW + " TEXT,"
                    + MoviesColumns.MOVIE_GENRE_IDS + " TEXT,"
                    + MoviesColumns.MOVIE_POPULARITY + " REAL,"
                    + MoviesColumns.MOVIE_VOTE_AVERAGE + " REAL,"
                    + MoviesColumns.MOVIE_VOTE_COUNT + " INTEGER,"
                    + MoviesColumns.MOVIE_BACKDROP_PATH + " TEXT,"
                    + MoviesColumns.MOVIE_POSTER_PATH + " TEXT,"
                    + MoviesColumns.MOVIE_RELEASE_DATE + " TEXT,"
                    + MoviesColumns.MOVIE_VIDEO + " INTEGER NOT NULL DEFAULT 0,"
                    + MoviesColumns.MOVIE_FAVORED + " INTEGER NOT NULL DEFAULT 0,"
                    + "UNIQUE (" + MoviesColumns.MOVIE_ID + ") ON CONFLICT REPLACE)");


        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }



        private interface References {
            String MOVIE_ID = "REFERENCES " + MoviesContract.Tables.MOVIES + "(" + MoviesContract.Movies.MOVIE_ID + ")";
        }



    }

