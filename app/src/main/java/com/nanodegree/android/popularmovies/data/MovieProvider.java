package com.nanodegree.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by u2stay1915 on 7/27/17.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    public static final int CODE_FAVORITE_MOVIE = 200;
    public static final int CODE_FAVORITE_MOVIE_WITH_ID = 201;
    public static final int CODE_FAVORITE_MOVIE_WITH_MOVIE_ID = 202;

    public static final int CODE_TRAILER = 300;

    public static final int CODE_REVIEW = 400;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);

        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_FAVORTIE_MOVIE, CODE_FAVORITE_MOVIE);
        // favorite movie
        matcher.addURI(authority, MovieContract.PATH_FAVORTIE_MOVIE + "/#", CODE_FAVORITE_MOVIE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_FAVORTIE_MOVIE + "/movie_id"+ "/#", CODE_FAVORITE_MOVIE_WITH_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_TRAILER, CODE_TRAILER);

        matcher.addURI(authority, MovieContract.PATH_REVIEW, CODE_REVIEW);

        return matcher;
    }

    private MovieDbHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE_WITH_ID: {

                String normalizedUtcDateString = uri.getLastPathSegment();

                String lastPathSegment = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{lastPathSegment};

                //Log.v("SelectionArg -> ", selectionArgs[0]);

                cursor = mOpenHelper.getReadableDatabase().query(

                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,

                        MovieContract.MovieEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_MOVIE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_FAVORITE_MOVIE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_FAVORITE_MOVIE_WITH_ID: {

                String lastPathSegment = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{lastPathSegment};
                //Log.v("SelectionArg -> ", selectionArgs[0]);

                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.FavoriteMovieEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_FAVORITE_MOVIE_WITH_MOVIE_ID: {

                String lastPathSegment = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{lastPathSegment};
                //Log.v("SelectionArg -> ", selectionArgs[0]);

                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        //MovieContract.FavoriteMovieEntry._ID + " = ? ",
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_TRAILER: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_REVIEW: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    int contentLength = values.length;
                    Log.v("Provider Insert -> ", String.valueOf(rowsInserted));
                    Log.v("contentLength -> ", String.valueOf(contentLength));
                }

                return rowsInserted;

            case CODE_TRAILER:
                db.beginTransaction();
                int rowsAdded = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsAdded++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsAdded > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    int contentLength = values.length;
                    Log.v("Provider Insert -> ", String.valueOf(rowsAdded));
                    Log.v("contentLength -> ", String.valueOf(contentLength));
                }

                return rowsAdded;

            case CODE_REVIEW:
                db.beginTransaction();
                int rowsEntered = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsEntered++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsEntered > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    int contentLength = values.length;
                    Log.v("Provider Insert -> ", String.valueOf(rowsEntered));
                    Log.v("contentLength -> ", String.valueOf(contentLength));
                }

                return rowsEntered;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case CODE_FAVORITE_MOVIE:
                long id = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MovieContract.FavoriteMovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_FAVORITE_MOVIE_WITH_ID:

                String id = uri.getPathSegments().get(1);

                numRowsDeleted = mOpenHelper.getWritableDatabase()
                        .delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;

            case CODE_FAVORITE_MOVIE_WITH_MOVIE_ID:

                String movie_id = uri.getPathSegments().get(2);
                Log.v("movie_id -> ", movie_id);

                numRowsDeleted = mOpenHelper.getWritableDatabase()
                        .delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, "movie_id=?", new String[]{movie_id});
                break;

            case CODE_TRAILER:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_REVIEW:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}