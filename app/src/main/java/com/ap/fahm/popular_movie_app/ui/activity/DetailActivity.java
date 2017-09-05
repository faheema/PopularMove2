package com.ap.fahm.popular_movie_app.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ap.fahm.popular_movie_app.AppController;
import com.ap.fahm.popular_movie_app.R;
import com.ap.fahm.popular_movie_app.data.db.MoviesContract;
import com.ap.fahm.popular_movie_app.data.db.MoviesDBHelper;
import com.ap.fahm.popular_movie_app.data.model.Movie;
import com.ap.fahm.popular_movie_app.data.model.Review;
import com.ap.fahm.popular_movie_app.data.model.Video;
import com.ap.fahm.popular_movie_app.data.sync.IMovieTrailerAsyncCallback;
import com.ap.fahm.popular_movie_app.data.sync.IPopularMovieAsuncCallback;
import com.ap.fahm.popular_movie_app.data.sync.MovieTrailerAsyncTask;
import com.ap.fahm.popular_movie_app.data.sync.PopularMoviesQueryTask;
import com.ap.fahm.popular_movie_app.ui.adapter.ReviewsAdapter;
import com.ap.fahm.popular_movie_app.ui.adapter.TrailersAdapter;
import com.ap.fahm.popular_movie_app.ui.listner.OnTrailersItemClickListner;
import com.ap.fahm.popular_movie_app.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity implements IPopularMovieAsuncCallback, IMovieTrailerAsyncCallback, OnTrailersItemClickListner {
    TextView mTitle, mReleaseDate, mVoteAvg, mOverView;
    ToggleButton mBtnMarkFav;
    NetworkImageView mPoster;
    RecyclerView mRVMovieTrailer, mRVMovieReview;
    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        setTitle(R.string.detail_movie);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            Intent i = getIntent();
            mMovie = i.getParcelableExtra(NetworkUtils.SELECTED_MOVIE);
        }else {
            mMovie = savedInstanceState.getParcelable("movie");
        }

        if (mMovie != null) {

            findUIs();

            mTitle.setText(mMovie.getTitle());
            mReleaseDate.setText(mMovie.getReleaseDate());
            DecimalFormat df = new DecimalFormat("#.0");
            mVoteAvg.setText(df.format(mMovie.getVoteAverage()) + getResources().getString(R.string.by_10));
            mOverView.setText(mMovie.getOverview());

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            mPoster.setImageUrl(mMovie.getPosterPath(), imageLoader);

            long movie_id = mMovie.getId();

            URL trailerURL = NetworkUtils.getMovieTrailersURL(movie_id);
            new MovieTrailerAsyncTask(this).execute(trailerURL);

            URL reviewURL = NetworkUtils.getMovieReviewsURL(movie_id);
            new PopularMoviesQueryTask(this).execute(reviewURL);
            Log.d("REVIEW URL", " " + reviewURL);

            if (isFavMovieExists(movie_id+""))
            {
                mBtnMarkFav.setChecked(false);
            }else {mBtnMarkFav.setChecked(true);}

            mBtnMarkFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addFavMovieInDB();
                }
            });
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie", mMovie);
        super.onSaveInstanceState(outState);
    }
    private void findUIs() {
        mTitle = (TextView) findViewById(R.id.dmovie_title);
        mReleaseDate = (TextView) findViewById(R.id.dmovie_release_date);
        mVoteAvg = (TextView) findViewById(R.id.dmovie_vote_avg);
        mOverView = (TextView) findViewById(R.id.dmovie_overview);
        mPoster = (NetworkImageView) findViewById(R.id.dmovie_poster);
        mRVMovieTrailer = (RecyclerView) findViewById(R.id.rv_movie_trailers);
        mRVMovieReview = (RecyclerView) findViewById(R.id.rv_movie_reviews);
        mBtnMarkFav = (ToggleButton) findViewById(R.id.btn_mark_fav);

    }

    @Override
    public void movieTrailerTskFinish(String results) {
        if (results != null && !results.equals("")) {
            TrailersAdapter trailersAdapter;
            ArrayList<Video> videoArrayList = parseTrailerSearchData(results);
            trailersAdapter = new TrailersAdapter(videoArrayList, this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRVMovieTrailer.setLayoutManager(mLayoutManager);
            mRVMovieTrailer.setAdapter(trailersAdapter);

        }
    }

    @Override
    public void movieTaskFinish(String results) {
        if (results != null && !results.equals("")) {
            ReviewsAdapter reviewsAdapter;
            ArrayList<Review> reviewArrayList = parseReviewSearchData(results);
            reviewsAdapter = new ReviewsAdapter(reviewArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRVMovieReview.setLayoutManager(mLayoutManager);
            mRVMovieReview.setAdapter(reviewsAdapter);
            DividerItemDecoration divider = new DividerItemDecoration(mRVMovieReview.getContext(), DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.line_divider));
            mRVMovieReview.addItemDecoration(divider);
        }
    }

    private ArrayList<Video> parseTrailerSearchData(String searchResults) {
        ArrayList<Video> trailerArrayList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(searchResults);
            JSONArray jsonResultsArray = obj.getJSONArray("results");

            for (int count = 0; count < jsonResultsArray.length(); count++) {


                Video video = new Video();
                JSONObject reviewJsonObject = (JSONObject) jsonResultsArray.get(count);

                video.setId(reviewJsonObject.getString("id"));
                video.setIso(reviewJsonObject.getString("iso_639_1"));
                video.setKey(reviewJsonObject.getString("key"));
                video.setName(reviewJsonObject.getString("name"));
                video.setSite(reviewJsonObject.getString("site"));
                video.setSize(reviewJsonObject.getInt("size"));
                video.setType(reviewJsonObject.getString("type"));

                trailerArrayList.add(video);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailerArrayList;
    }

    private ArrayList<Review> parseReviewSearchData(String searchResults) {
        ArrayList<Review> reviewArrayList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(searchResults);
            JSONArray jsonResultsArray = obj.getJSONArray("results");

            for (int count = 0; count < jsonResultsArray.length(); count++) {

                Review review = new Review();
                JSONObject reviewJsonObject = (JSONObject) jsonResultsArray.get(count);

                review.setId(reviewJsonObject.getString("id"));
                review.setAuthor(reviewJsonObject.getString("author"));
                review.setContent(reviewJsonObject.getString("content"));
                review.setUrl(reviewJsonObject.getString("url"));

                reviewArrayList.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ParseReviewws", " REVIES size= " + reviewArrayList.size());
        return reviewArrayList;
    }

    public void playVideo(Video video) {
        if (video.getSite().equals(Video.SITE_YOUTUBE))
            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
        else
            Log.d("ERROR", "Unsupported video format");
    }


    @Override
    public void onItemClick(Video item) {
        playVideo(item);

    }

    private void addFavMovieInDB()
    {

        String movie_id= mMovie.getId()+"";


        boolean deleteStatus=isFavMovieExists(movie_id);
        if (!deleteStatus)
        {
            ContentValues cvMovie = new ContentValues();

            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_VOTE_COUNT,""+mMovie.getVoteCount());
            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_RELEASE_DATE,""+mMovie.getReleaseDate());
            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_VOTE_AVERAGE,""+mMovie.getVoteAverage());
            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_OVERVIEW,""+mMovie.getOverview());
            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_ID,""+mMovie.getId());
            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_BACKDROP_PATH,""+mMovie.getBackdropPath());
            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_POPULARITY,""+mMovie.getPopularity());
            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_POSTER_PATH,""+mMovie.getPosterPath());
            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_TITLE,""+mMovie.getTitle());

            int video_val= (mMovie.getVideo()) ? 1:0;
            cvMovie.put(MoviesContract.MoviesColumns.MOVIE_VIDEO,""+video_val);

            MoviesDBHelper dbHelper = new MoviesDBHelper(this);
            SQLiteDatabase db;
            db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try
            {
                db.insert(MoviesContract.Tables.MOVIES, null, cvMovie);
                db.setTransactionSuccessful();
            }
            catch (SQLException e) {
            }
            finally
            {
                db.endTransaction();
                dbHelper.close();
                db.close();
            }
        }
        else
        {
           // Log.d("FAHEEM"," ALREADY FAVORATE");
        }



    }
    private boolean isFavMovieExists(String movie_id)
    {
        MoviesDBHelper dbHelper = new MoviesDBHelper(this);
        SQLiteDatabase mDb;
        mDb = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String sql ="SELECT * FROM "+MoviesContract.Tables.MOVIES+" WHERE "
                +MoviesContract.MoviesColumns.MOVIE_ID+"="+movie_id;
        cursor= mDb.rawQuery(sql,null);
//+" AND "+MoviesContract.MoviesColumns.MOVIE_FAVORED+"=1"
      //  Log.d("FAHEEM","Cursor Count : " + cursor.getCount());

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        dbHelper.close();
        mDb.close();
        return exists;
    }

}
