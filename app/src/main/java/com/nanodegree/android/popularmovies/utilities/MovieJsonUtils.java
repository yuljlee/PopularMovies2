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
import android.util.Log;

import com.nanodegree.android.popularmovies.Movie;
import com.nanodegree.android.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
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

    public static ContentValues[] getMovieContentValuesFromJson(Context context, String JsonStr)
            throws JSONException {

        JSONObject moiveJson = new JSONObject(JsonStr);
        JSONArray movieArray = moiveJson.getJSONArray("results");

        //ContentValues[] movieContentValues = new ContentValues[moiveJsonStr.length()];
        ContentValues[] movieContentValues = new ContentValues[movieArray.length()];

        String movieId;
        String posterPath = "";
        String movieTitle = "";
        String releaseDate = "";
        String voteAverage = "";
        String overview = "";
        String videoPath = "";
        String reviewPath = "";

        //ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject eachMoive = movieArray.getJSONObject(i);

            movieId = eachMoive.getString("id");
            //movieTitle = eachMoive.getString("original_title");
            movieTitle = eachMoive.getString("title");
            posterPath = eachMoive.getString("poster_path");
            releaseDate = eachMoive.getString("release_date");
            voteAverage = eachMoive.getString("vote_average");
            overview = eachMoive.getString("overview");

            //movies.add(new Movie(movieTitle, posterPath, releaseDate, voteAverage, overview));
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieTitle);
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, posterPath);
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
            movieValues.put(MovieContract.MovieEntry.COLUMN_AVERAGE, voteAverage);
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
//            movieValues.put(MovieContract.MovieEntry.COLUMN_VIDEO_URL, "video");
//            movieValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_URL, "review");

            movieContentValues[i] = movieValues;
        }

        return movieContentValues;
    }

    public static ContentValues[] getTrailerContentValuesFromJson(Context context, String JsonStr)
            throws JSONException {

        JSONObject json = new JSONObject(JsonStr);
        JSONArray array = json.getJSONArray("results");

        //ContentValues[] movieContentValues = new ContentValues[moiveJsonStr.length()];
        ContentValues[] contentValues = new ContentValues[array.length()];

        String key = "";
        String name = "";
        String site = "";
        String size = "";
        String type = "";

        //ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            //movieTitle = eachMoive.getString("original_title");
            key = jsonObject.getString("key");
            name = jsonObject.getString("name");
            site = jsonObject.getString("site");
            size = jsonObject.getString("size");
            type = jsonObject.getString("type");

            //movies.add(new Movie(movieTitle, posterPath, releaseDate, voteAverage, overview));
            ContentValues trailerValues = new ContentValues();
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_KEY, key);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_NAME, name);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_SITE, site);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_SIZE, size);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TYPE, type);

            contentValues[i] = trailerValues;
        }

        return contentValues;
    }

    public static ContentValues[] getReviewContentValuesFromJson(Context context, String JsonStr)
            throws JSONException {

        JSONObject json = new JSONObject(JsonStr);
        JSONArray array = json.getJSONArray("results");

        ContentValues[] contentValues = new ContentValues[array.length()];

        String id;
        String author;
        String content;
        String url;

        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            id = jsonObject.getString("id");
            author = jsonObject.getString("author");
            content = jsonObject.getString("content");
            url = jsonObject.getString("url");

            //movies.add(new Movie(movieTitle, posterPath, releaseDate, voteAverage, overview));
            ContentValues reviewValues = new ContentValues();
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, id);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, author);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, content);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_URL, url);

            contentValues[i] = reviewValues;
        }

        return contentValues;
    }
}