/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nanodegree.android.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.nanodegree.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Utility functions to handle Movie JSON data.
 */
public final class MovieJsonUtils {

    private static final String TAG = MovieJsonUtils.class.getSimpleName();

    public static ArrayList<Movie> getSimpleMovieStringsFromJson(Context context, String moiveJsonStr)
            throws JSONException {

        JSONObject moiveJson = new JSONObject(moiveJsonStr);
        JSONArray movieArray = moiveJson.getJSONArray("results");

        //String movieId;
        String posterPath = "";
        String movieTitle = "";
        String releaseDate = "";
        String voteAverage = "";
        String overview = "";

        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject eachMoive = movieArray.getJSONObject(i);

            //movieId = eachMoive.getString("id");
            movieTitle = eachMoive.getString("original_title");
            posterPath = eachMoive.getString("poster_path");
            releaseDate = eachMoive.getString("release_date");
            voteAverage = eachMoive.getString("vote_average");
            overview = eachMoive.getString("overview");

            movies.add(new Movie(movieTitle, posterPath, releaseDate, voteAverage, overview));
        }

        return movies;
    }

}