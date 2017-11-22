package com.ap.fahm.popular_movie_app.data.sync_httpurlconn;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Faheem on 07/08/17.
 */
//Not Used
public class PopularMoviesQueryTask extends AsyncTask<URL, Void, String> {

    IPopularMovieAsuncCallback mCallBack;

    public PopularMoviesQueryTask(IPopularMovieAsuncCallback callback) {
        mCallBack = callback;
    }

    @Override
    protected String doInBackground(URL... params) {

        URL searchUrl = params[0];
        String searchResults = null;
        try {
            searchResults = getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    @Override
    protected void onPostExecute(String searchResults) {

        mCallBack.movieTaskFinish(searchResults);
    }


    public String getResponseFromHttpUrl(URL url) throws IOException {
        Log.d("Debug 1", url.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        String responseStr = "";
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            responseStr = convertStreamToString(in);

        } finally {
            urlConnection.disconnect();
        }
        return responseStr;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        Log.d("Debug 3 ", "" + sb);

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
