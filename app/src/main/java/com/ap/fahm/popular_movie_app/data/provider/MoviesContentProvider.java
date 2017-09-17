package com.ap.fahm.popular_movie_app.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ap.fahm.popular_movie_app.data.db.MoviesContract;
import com.ap.fahm.popular_movie_app.data.db.MoviesDBHelper;

import static com.ap.fahm.popular_movie_app.data.db.MoviesContract.CONTENT_AUTHORITY;

/**
 * Created by Faheem on 14/09/17.
 */

public final class MoviesContentProvider extends ContentProvider {

    private static final String TAG = MoviesContentProvider.class.getSimpleName();
    private static final int MOVIES = 100;
    private static final int MOVIES_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        matcher.addURI(authority, "movies", MOVIES);
        matcher.addURI(authority, "movies/*", MOVIES_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new MoviesDBHelper(context);
        return true;
    }

    private void deleteDatabase() {
        mDbHelper.close();
        Context context = getContext();
        MoviesDBHelper.deleteDatabase(context);
        mDbHelper = new MoviesDBHelper(getContext());
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("FAHEEM"," QUERY ");
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {

            case MOVIES:
                cursor =  db.query(MoviesContract.Tables.MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG,"MOVE INSERT");
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case MOVIES: {
                db.insertOrThrow(MoviesContract.Tables.MOVIES, null, values);
                notifyChange(uri);
                return MoviesContract.Movies.buildMovieUri(values.getAsString(MoviesContract.Movies.MOVIE_ID));
            }

            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }
    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if (uri.equals(MoviesContract.BASE_CONTENT_URI)) {
            deleteDatabase();
            notifyChange(uri);
            return 1;
        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
