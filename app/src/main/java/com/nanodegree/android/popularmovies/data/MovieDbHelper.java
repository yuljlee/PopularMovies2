package com.nanodegree.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.nanodegree.android.popularmovies.data.MovieContract.*;

/**
 * Created by u2stay1915 on 7/27/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 14;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                        MovieEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_MOVIE_ID      + " TEXT NOT NULL, "                    +
                        MovieEntry.COLUMN_TITLE         + " TEXT NOT NULL, "                 +
                        MovieEntry.COLUMN_POSTER_URL    + " TEXT NOT NULL,"                  +
                        MovieEntry.COLUMN_RELEASE_DATE  + " INTEGER NOT NULL, "                    +
                        MovieEntry.COLUMN_AVERAGE       + " TEXT NOT NULL, "                    +
                        MovieEntry.COLUMN_OVERVIEW      + " TEXT NOT NULL "                    +
                        " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

        final String SQL_CREATE_FAVORITE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                        FavoriteMovieEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteMovieEntry.COLUMN_MOVIE_ID      + " TEXT NOT NULL, "                    +
                        FavoriteMovieEntry.COLUMN_TITLE         + " TEXT NOT NULL, "                 +
                        FavoriteMovieEntry.COLUMN_POSTER_URL    + " TEXT NOT NULL,"                  +
                        FavoriteMovieEntry.COLUMN_RELEASE_DATE  + " INTEGER NOT NULL, "                    +
                        FavoriteMovieEntry.COLUMN_AVERAGE       + " TEXT NOT NULL, "                    +
                        FavoriteMovieEntry.COLUMN_OVERVIEW      + " TEXT NOT NULL, "                    +
                        " UNIQUE (" + FavoriteMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);

        final String SQL_CREATE_TRAILER_TABLE =

                "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                        TrailerEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TrailerEntry.COLUMN_KEY            + " TEXT NOT NULL,"                  +
                        TrailerEntry.COLUMN_NAME         + " TEXT NOT NULL, "                 +
                        TrailerEntry.COLUMN_SITE            + " TEXT NOT NULL,"                  +
                        TrailerEntry.COLUMN_SIZE            + " TEXT NOT NULL, "                  +
                        TrailerEntry.COLUMN_TYPE            + " TEXT NOT NULL); ";

        db.execSQL(SQL_CREATE_TRAILER_TABLE);

        final String SQL_CREATE_REVIEW_TABLE =

                "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " (" +
                        ReviewEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ReviewEntry.COLUMN_REVIEW_ID            + " TEXT NOT NULL, "                    +
                        ReviewEntry.COLUMN_AUTHOR         + " TEXT NOT NULL, "                 +
                        ReviewEntry.COLUMN_CONTENT            + " TEXT NOT NULL,"                  +
                        ReviewEntry.COLUMN_REVIEW_URL            + " TEXT NOT NULL); ";

        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // movie table
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        // favorite movie table
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        // trailer table
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        // review table
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);

        onCreate(db);
    }
}
