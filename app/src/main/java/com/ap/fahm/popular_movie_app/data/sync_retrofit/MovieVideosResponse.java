package com.ap.fahm.popular_movie_app.data.sync_retrofit;

import com.ap.fahm.popular_movie_app.data.model.Movie;
import com.ap.fahm.popular_movie_app.data.model.Video;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Faheem on 16/11/17.
 */

public class MovieVideosResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Video> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
