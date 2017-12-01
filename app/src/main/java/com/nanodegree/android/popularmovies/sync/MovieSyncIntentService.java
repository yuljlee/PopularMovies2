package com.nanodegree.android.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by u2stay1915 on 7/28/17.
 */

public class MovieSyncIntentService extends IntentService {

    public MovieSyncIntentService() {super("MovieSyncIntentService");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String sortBy = intent.getStringExtra("sortBy");
        MovieSyncTask.syncMovie(this, sortBy);
    }
}
