package com.ap.fahm.popular_movie_app.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ap.fahm.popular_movie_app.BuildConfig;
import com.ap.fahm.popular_movie_app.R;
import com.ap.fahm.popular_movie_app.data.db.MoviesContract;
import com.ap.fahm.popular_movie_app.data.db.MoviesDBHelper;
import com.ap.fahm.popular_movie_app.data.model.Movie;

import com.ap.fahm.popular_movie_app.data.remote.ApiClient;
import com.ap.fahm.popular_movie_app.data.remote.ApiInterface;
import com.ap.fahm.popular_movie_app.data.remote.MoviesResponse;
import com.ap.fahm.popular_movie_app.ui.adapter.MoviesAdapter;
import com.ap.fahm.popular_movie_app.utilities.NetworkUtils;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMovieActivity extends AppCompatActivity  {

    private ArrayList<Movie> movieList = new ArrayList<>();
    private GridView mGridView;
    private MoviesAdapter mMovieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) this.findViewById(R.id.movies_grid);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainMovieActivity.this, DetailActivity.class);
                Movie selectedMovie = movieList.get(position);
                i.putExtra(NetworkUtils.SELECTED_MOVIE, selectedMovie);
                startActivity(i);
            }
        });

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie_list")) {
            getPopularMovies();
        }else {
            movieList = savedInstanceState.getParcelableArrayList("movie_list");
            int index = savedInstanceState.getInt("scroll_pos",0);
            mMovieAdapter = new MoviesAdapter(MainMovieActivity.this, movieList);
            mGridView.setAdapter(mMovieAdapter);
            mGridView.setSelection(index);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.moviessort, menu);
        return true;
    }
    private void getPopularMovies()
    {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                try {
                    int statusCode = response.code();

                    movieList = (ArrayList<Movie>) response.body().getResults();
                    mMovieAdapter = new MoviesAdapter(MainMovieActivity.this, movieList);
                    mGridView.setAdapter(mMovieAdapter);
                }
                catch (NullPointerException e)
                {e.printStackTrace();}
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("FAHEEM", t.toString());
            }
        });
    }

    private void getTopRatedMovies() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                try {
                    int statusCode = response.code();

                    movieList = (ArrayList<Movie>) response.body().getResults();
                    mMovieAdapter = new MoviesAdapter(MainMovieActivity.this, movieList);
                    mGridView.setAdapter(mMovieAdapter);
                }
                catch (NullPointerException e)
                {e.printStackTrace();}
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("FAHEEM", t.toString());
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int index = mGridView.getFirstVisiblePosition();
        outState.putParcelableArrayList("movie_list", movieList);
        outState.putInt("scroll_pos",index);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            getPopularMovies();

            return true;
        }
        if (id == R.id.action_toprated) {
            getTopRatedMovies();
            return true;
        }
        if (id == R.id.action_favorite) {
            getFAVMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFAVMovies() {

        MoviesDBHelper dbHelper = new MoviesDBHelper(this);
        SQLiteDatabase mDb;
        mDb = dbHelper.getReadableDatabase();
        movieList.clear();
        String Query = "Select * from " + MoviesContract.Tables.MOVIES ;
        Cursor cursor = mDb.rawQuery(Query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Movie movie= new Movie();
            int index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_ID);
            movie.setId(Long.valueOf(cursor.getString(index)));

            index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_TITLE);
            movie.setTitle(cursor.getString(index));

            index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_BACKDROP_PATH);
            movie.setBackdropPath(cursor.getString(index));

            index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_OVERVIEW);
            movie.setOverview(cursor.getString(index));

            index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_POPULARITY);
            movie.setPopularity(Double.valueOf(cursor.getString(index)));

            index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_POSTER_PATH);
            movie.setPosterPath(cursor.getString(index));

            index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_RELEASE_DATE);
            movie.setReleaseDate(cursor.getString(index));

            index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_VIDEO);
            boolean tempb= ( cursor.getInt(index) == 1)? true: false;
            movie.setVideo(tempb);

            index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_VOTE_AVERAGE);
            movie.setVoteAverage(Double.valueOf(cursor.getString(index)));

            index = cursor.getColumnIndex(MoviesContract.MoviesColumns.MOVIE_VOTE_COUNT);
            movie.setVoteCount(Long.valueOf(cursor.getString(index)));


            movieList.add(movie);
            cursor.moveToNext();
        }
        mMovieAdapter = new MoviesAdapter(MainMovieActivity.this, movieList);
        mGridView.setAdapter(mMovieAdapter);
    }

   /*

    //// implements IPopularMovieAsuncCallback

    private void getPopularMovies() {

        new PopularMoviesQueryTask(this).execute(NetworkUtils.getPopularMovieURL());
    }


    private void getTopRatedMovies() {

        new PopularMoviesQueryTask(this).execute(NetworkUtils.getTopRatedMovieURL());
    }

    @Override
    public void movieTaskFinish(String results) {

        if (results != null && !results.equals("")) {

            movieList = searchResultsToMovieList(results);

            mMovieAdapter = new MoviesAdapter(MainMovieActivity.this, movieList);
            mGridView.setAdapter(mMovieAdapter);
        }
    }


    private ArrayList<Movie> searchResultsToMovieList(String searchResults) {
        ArrayList<Movie> moviesList = new ArrayList<>();
        //  List<ContentValues> moviesDBlist = new ArrayList<ContentValues>();

        try {
            JSONObject obj = new JSONObject(searchResults);
            JSONArray jsonResultsArray = obj.getJSONArray("results");

            for (int count = 0; count < jsonResultsArray.length(); count++) {
                String strTemp = "";
                Movie movie = new Movie();
                JSONObject movieJsonObject = (JSONObject) jsonResultsArray.get(count);
                //  ContentValues cvMovie = new ContentValues();

                movie.setVoteCount(movieJsonObject.getLong("vote_count"));
                //  cvMovie.put(MoviesContract.MoviesColumns.MOVIE_VOTE_COUNT,movieJsonObject.getLong("vote_count"));

                movie.setId(movieJsonObject.getLong("id"));
                //  cvMovie.put(MoviesContract.MoviesColumns.MOVIE_ID,""+movieJsonObject.getLong("id"));

                movie.setVideo(movieJsonObject.getBoolean("video"));
                //  int video_val= (movieJsonObject.getBoolean("video")) ? 1:0;
                //  cvMovie.put(MoviesContract.MoviesColumns.MOVIE_VIDEO,video_val);


                movie.setVoteAverage(movieJsonObject.getDouble("vote_average"));
                //  cvMovie.put(MoviesContract.MoviesColumns.MOVIE_VOTE_AVERAGE,""+movieJsonObject.getDouble("vote_average"));

                movie.setTitle(movieJsonObject.getString("title"));
                //  cvMovie.put(MoviesContract.MoviesColumns.MOVIE_TITLE,""+movieJsonObject.getString("title"));

                movie.setPopularity(movieJsonObject.getDouble("popularity"));
                //  cvMovie.put(MoviesContract.MoviesColumns.MOVIE_POPULARITY,""+movieJsonObject.getDouble("popularity"));

                Uri.Builder posterbuiltUri = new Uri.Builder();
                strTemp = movieJsonObject.getString("poster_path");
                posterbuiltUri.scheme("http")
                        .authority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath("w185")
                        .appendPath(strTemp.substring(1));
                String posterUrl = posterbuiltUri.toString();

                movie.setPosterPath(posterUrl);
                //  cvMovie.put(MoviesContract.MoviesColumns.MOVIE_POSTER_PATH,""+posterUrl);

                movie.setOriginalLanguage(movieJsonObject.getString("original_language"));
                movie.setOriginalTitle(movieJsonObject.getString("original_title"));


                JSONArray jsonArrayTemp = movieJsonObject.getJSONArray("genre_ids");
                List<Integer> genidsTemp = new ArrayList<>();
                for (int tempint = 0; tempint < jsonArrayTemp.length(); tempint++) {
                    genidsTemp.add(jsonArrayTemp.getInt(tempint));
                }

                movie.setGenreIds(genidsTemp);

                movie.setBackdropPath(movieJsonObject.getString("backdrop_path"));
                movie.setAdult(movieJsonObject.getBoolean("adult"));

                movie.setOverview(movieJsonObject.getString("overview"));
                //  cvMovie.put(MoviesContract.MoviesColumns.MOVIE_OVERVIEW,""+movieJsonObject.getString("overview"));

                movie.setReleaseDate(movieJsonObject.getString("release_date"));
                //  cvMovie.put(MoviesContract.MoviesColumns.MOVIE_RELEASE_DATE,""+movieJsonObject.getString("release_date"));
                //  moviesDBlist.add(cvMovie);

                moviesList.add(movie);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Debug 4", " Movies size= " + moviesList.size());
        return moviesList;
    }*/

}
