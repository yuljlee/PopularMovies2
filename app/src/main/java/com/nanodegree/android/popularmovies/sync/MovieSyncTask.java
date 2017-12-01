package com.nanodegree.android.popularmovies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.nanodegree.android.popularmovies.data.MovieContract;
import com.nanodegree.android.popularmovies.utilities.MovieJsonUtils;
import com.nanodegree.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by u2stay1915 on 7/28/17.
 */

public class MovieSyncTask {

    synchronized public static void syncMovie(Context context, String sortBy) {

        try {


            //String sort = "popular";
            //String sort = "rated";
            URL movieRequestUrl = NetworkUtils.buildUrl(sortBy);

            Log.v("url", sortBy);

            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

            ContentValues[] movieValues = MovieJsonUtils
                    .getMovieContentValuesFromJson(context, jsonMovieResponse);

            if (movieValues != null && movieValues.length != 0) {

                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null);

                movieContentResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        movieValues);
            }


        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }
}
