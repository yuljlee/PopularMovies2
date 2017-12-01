package com.nanodegree.android.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.nanodegree.android.popularmovies.Movie;
import com.nanodegree.android.popularmovies.data.MovieContract;

/**
 * Created by u2stay1915 on 7/28/17.
 */

public class MovieSyncUtils {

    public static void startImmediateSync(@NonNull final Context context, String sortBy) {
        Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        intentToSyncImmediately.putExtra("sortBy", sortBy);
        context.startService(intentToSyncImmediately);
    }
}
