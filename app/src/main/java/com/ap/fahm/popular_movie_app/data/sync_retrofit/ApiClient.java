package com.ap.fahm.popular_movie_app.data.sync_retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ap.fahm.popular_movie_app.utilities.NetworkUtils.MOVIE_BASE_URL;

/**
 * Created by Faheem on 15/11/17.
 */

public class ApiClient {


    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
