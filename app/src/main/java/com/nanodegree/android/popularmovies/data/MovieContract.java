package com.nanodegree.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by u2stay1915 on 7/26/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.nanodegree.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FAVORTIE_MOVIE = "favorite_movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_POSTER_URL = "poster_url";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_AVERAGE = "average";

        public static final String COLUMN_OVERVIEW = "overview";

        public static Uri buildMovieUriWithId(int indexId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(indexId))
                    .build();
        }
    }

    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORTIE_MOVIE)
                .build();

        /* Used internally as the name of our weather table. */
        public static final String TABLE_NAME = "favorite_movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_AVERAGE = "average";
        public static final String COLUMN_OVERVIEW = "overview";

        public static Uri buildMovieUriWithId(int indexId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(indexId))
                    .build();
        }

        public static Uri buildMovieUriWithMovieId(String movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath("movie_id")
                    .appendPath(movieId)
                    .build();
        }
    }

    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILER)
                .build();

        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";

        public static Uri buildMovieUriWithId(int indexId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(indexId))
                    .build();
        }
    }

    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEW)
                .build();

        public static final String TABLE_NAME = "review";

        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_REVIEW_URL = "review_url";
    }
}
