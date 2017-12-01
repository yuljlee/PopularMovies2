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

public class ReviewFetchTask {

    public static void getReview(@NonNull final Context context, final String movieId) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {

                try {

                    URL reviewRequestUrl = NetworkUtils.buildUrlReview(movieId);

                    Log.v("getReview", movieId);

                    Log.v("reviewRequestUrl", reviewRequestUrl.toString());

                    String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);

                    ContentValues[] reviewValues = MovieJsonUtils
                            .getReviewContentValuesFromJson(context, jsonReviewResponse);

                    if (reviewValues != null && reviewValues.length != 0) {

                        ContentResolver reviewContentResolver = context.getContentResolver();

                        reviewContentResolver.delete(
                                MovieContract.ReviewEntry.CONTENT_URI,
                                null,
                                null);

                        reviewContentResolver.bulkInsert(
                                MovieContract.ReviewEntry.CONTENT_URI,
                                reviewValues);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
