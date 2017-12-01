package com.nanodegree.android.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nanodegree.android.popularmovies.data.MovieContract;
import com.nanodegree.android.popularmovies.utilities.MovieJsonUtils;
import com.nanodegree.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by u2stay1915 on 8/10/17.
 */

public class TrailerFetchTask {

    public static void getTrailer(@NonNull final Context context, final String movieId) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {

                try {

                    URL trailerRequestUrl = NetworkUtils.buildUrlTrailer(movieId);

                    Log.v("getTrailer", movieId);

                    Log.v("trailerRequestUrl", trailerRequestUrl.toString());

                    String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);

                    ContentValues[] trailerValues = MovieJsonUtils
                            .getTrailerContentValuesFromJson(context, jsonTrailerResponse);

                    if (trailerValues != null && trailerValues.length != 0) {

                        ContentResolver trailerContentResolver = context.getContentResolver();

                        trailerContentResolver.delete(
                                MovieContract.TrailerEntry.CONTENT_URI,
                                null,
                                null);

                        trailerContentResolver.bulkInsert(
                                MovieContract.TrailerEntry.CONTENT_URI,
                                trailerValues);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
