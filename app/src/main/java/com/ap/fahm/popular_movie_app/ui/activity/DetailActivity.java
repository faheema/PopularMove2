package com.ap.fahm.popular_movie_app.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ap.fahm.popular_movie_app.AppController;
import com.ap.fahm.popular_movie_app.BuildConfig;
import com.ap.fahm.popular_movie_app.R;
import com.ap.fahm.popular_movie_app.data.db.MoviesContract;
import com.ap.fahm.popular_movie_app.data.db.MoviesDBHelper;
import com.ap.fahm.popular_movie_app.data.model.Movie;
import com.ap.fahm.popular_movie_app.data.model.Review;
import com.ap.fahm.popular_movie_app.data.model.Video;
import com.ap.fahm.popular_movie_app.data.sync_httpurlconn.IMovieTrailerAsyncCallback;
import com.ap.fahm.popular_movie_app.data.sync_httpurlconn.IPopularMovieAsuncCallback;
import com.ap.fahm.popular_movie_app.data.sync_httpurlconn.MovieTrailerAsyncTask;
import com.ap.fahm.popular_movie_app.data.sync_httpurlconn.PopularMoviesQueryTask;
import com.ap.fahm.popular_movie_app.data.sync_retrofit.ApiClient;
import com.ap.fahm.popular_movie_app.data.sync_retrofit.ApiInterface;
import com.ap.fahm.popular_movie_app.data.sync_retrofit.MovieReviewsResponse;
import com.ap.fahm.popular_movie_app.data.sync_retrofit.MovieVideosResponse;
import com.ap.fahm.popular_movie_app.data.sync_retrofit.MoviesResponse;
import com.ap.fahm.popular_movie_app.ui.adapter.MoviesAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements OnTrailersItemClickListner{
    TextView mTitle, mReleaseDate, mVoteAvg, mOverView;
    ToggleButton mBtnMarkFav;
    NetworkImageView mPoster;
    RecyclerView mRVMovieTrailer, mRVMovieReview;
    Movie mMovie;
    ArrayList<Video> videoArrayList;
    ArrayList<Review> reviewArrayList;

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
            mPoster.setImageUrl(NetworkUtils.buildPosterUrl(mMovie.getPosterPath()), imageLoader);

            long movie_id = mMovie.getId();

            loadTrailers();
            loadReviews();
            if (isFavMovieExists(movie_id+""))
            {
                mBtnMarkFav.setChecked(false);

            }else {
                mBtnMarkFav.setChecked(true);
            }

            mBtnMarkFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addFavMovieInDB();
                }
            });
        }

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
    private void loadTrailers()
    {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MovieVideosResponse> call = apiService.getMovieVideo((int)mMovie.getId(),BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<MovieVideosResponse>() {
            @Override
            public void onResponse(Call<MovieVideosResponse> call, Response<MovieVideosResponse> response) {
                try {

                    if (response.isSuccessful()) {
                        videoArrayList = (ArrayList<Video>) response.body().getResults();
                        TrailersAdapter trailersAdapter;

                        trailersAdapter = new TrailersAdapter(videoArrayList, DetailActivity.this);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mRVMovieTrailer.setLayoutManager(mLayoutManager);
                        mRVMovieTrailer.setAdapter(trailersAdapter);
                    }
                }
                catch (NullPointerException e)
                {e.printStackTrace();}
            }

            @Override
            public void onFailure(Call<MovieVideosResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("FAHEEM", t.toString());
            }
        });
    }
    private void loadReviews()
    {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MovieReviewsResponse> call = apiService.getMovieReviews((int)mMovie.getId(),BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {
                try {
                    if (response.isSuccessful()) {

                    reviewArrayList = (ArrayList<Review>) response.body().getResults();
                    ReviewsAdapter reviewsAdapter;

                    reviewsAdapter = new ReviewsAdapter(reviewArrayList);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mRVMovieReview.setLayoutManager(mLayoutManager);
                    mRVMovieReview.setAdapter(reviewsAdapter);

                    DividerItemDecoration divider = new DividerItemDecoration(mRVMovieReview.getContext(), DividerItemDecoration.VERTICAL);
                    divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.line_divider));
                    mRVMovieReview.addItemDecoration(divider);
                    }
                }
                catch (NullPointerException e)
                {e.printStackTrace();}

            }
            @Override
            public void onFailure(Call<MovieReviewsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("FAHEEM", t.toString());
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie", mMovie);
        super.onSaveInstanceState(outState);
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

            Uri uri = DetailActivity.this.getContentResolver().insert(MoviesContract.Movies.CONTENT_URI, cvMovie);

            // COMPLETED (8) Display the URI that's returned with a Toast
            // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
            if(uri != null) {
                Log.e("FAHEEM", "Favorits successfully added! ");
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
         // MoviesContract.MoviesColumns.MOVIE_ID+"="+movie_id
        // cursor= mDb.rawQuery(sql,null);
        cursor = DetailActivity.this.getContentResolver().query(MoviesContract.Movies.CONTENT_URI,
                null,
                MoviesContract.MoviesColumns.MOVIE_ID+"="+movie_id,
                null,
                null);
        boolean exists = false;
        if (cursor == null) {
            Log.e("FAHEEM", "Cursor Count : " + "ERROR");
        } else exists = (cursor.getCount() > 0);

        return exists;
    }


    /*
    *
    *
    * private void loadTrailerRevies()
    {
        long movie_id = mMovie.getId();
        URL trailerURL = NetworkUtils.getMovieTrailersURL(movie_id);
        new MovieTrailerAsyncTask(this).execute(trailerURL);

        URL reviewURL = NetworkUtils.getMovieReviewsURL(movie_id);
        new PopularMoviesQueryTask(this).execute(reviewURL);
        Log.d("REVIEW URL", " " + reviewURL);
    }
    @Override
    public void movieTrailerTskFinish(String results) {
        if (results != null && !results.equals("")) {
            TrailersAdapter trailersAdapter;
            videoArrayList = parseTrailerSearchData(results);
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
            reviewArrayList = parseReviewSearchData(results);
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

    * */
}
