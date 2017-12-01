package com.nanodegree.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nanodegree.android.popularmovies.data.MovieContract;
import com.nanodegree.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.title_bar;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TrailerAdapter mTrailerApdapter;
    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewApdapter;

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_URL,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_AVERAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW
//            MovieContract.MovieEntry.COLUMN_VIDEO_URL,
//            MovieContract.MovieEntry.COLUMN_REVIEW_URL
    };

//    public static final String[] FAVORITE_MOVIE_DETAIL_PROJECTION = {
//            MovieContract.FavoriteMovieEntry._ID,
//            MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
//            MovieContract.FavoriteMovieEntry.COLUMN_TITLE,
//            MovieContract.FavoriteMovieEntry.COLUMN_POSTER_URL,
//            MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
//            MovieContract.FavoriteMovieEntry.COLUMN_AVERAGE,
//            MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW
//            MovieContract.MovieEntry.COLUMN_VIDEO_URL,
//            MovieContract.MovieEntry.COLUMN_REVIEW_URL
//    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_MOVIE_TITLE = 2;
    public static final int INDEX_MOVIE_POSTER_URL = 3;
    public static final int INDEX_MOVIE_RELEASE_DATE = 4;
    public static final int INDEX_MOVIE_AVERAGE = 5;
    public static final int INDEX_MOVIE_OVERVIEW = 6;
//    public static final int INDEX_MOVIE_VIDEO_URL = 6;
//    public static final int INDEX_MOVIE_REVIEW_URL = 7;

    private static final int MOVIE_DETAIL_LOADER = 10;
    private static final int FAVORITE_MOVIE_DETAIL_LOADER = 20;
    private static final int TRAILER_LOADER = 30;
    private static final int REVIEW_LOADER = 40;

    private Uri mUri;
    private Uri mUriFavorite;
    private Uri mUriTrailer;
    private Uri mUriReview;

    private int mId;
    private String mMovieId;
    private TextView mMovieTitle;
    private String mPosterPath;
    private ImageView mImgPoster;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mOverview;
    FloatingActionButton mFabFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        TextView tvMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
//        ImageView imgPoster = (ImageView) findViewById(R.id.img_thumbnail);
//        TextView tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
//        TextView tvVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
//        TextView tvOverview = (TextView) findViewById(R.id.tv_synopsis);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailer);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTrailerApdapter = new TrailerAdapter(this, this);
        mRecyclerView.setAdapter(mTrailerApdapter);

        mReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_review);
        LinearLayoutManager layoutManagerReview
                = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(layoutManagerReview);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewApdapter = new ReviewAdapter(this);
        mReviewRecyclerView.setAdapter(mReviewApdapter);

        mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        mImgPoster = (ImageView) findViewById(R.id.img_thumbnail);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        mOverview = (TextView) findViewById(R.id.tv_synopsis);
        mFabFavorite = (FloatingActionButton) findViewById(R.id.fab_favorite);

        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        //Toast.makeText(this, "mUri -> " + mUri.toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "content uri -> " + MovieContract.MovieEntry.CONTENT_URI, Toast.LENGTH_LONG).show();

        Log.v("detail Uri -> ", mUri.toString());
        getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, DetailActivity.this);



//        Bundle bundle = getIntent().getExtras();
//        Movie movie = bundle.getParcelable("movie");

//        Picasso.with(this)
//                .load(movie.getPosterUrl())
//                .into(imgPoster);
//
//        String title = movie.getTitle();
//        if (title != null)
//            tvMovieTitle.setText(title);
//        String releaseDate = movie.getReleaseDate();
//        if (releaseDate != null) {
//            tvReleaseDate.setText(releaseDate);
//        }
//        String voteAverage = movie.getVoteAverage();
//        if (voteAverage != null)
//            //tvVoteAverage.setText(Double.toString(voteAverage));
//            voteAverage.concat("/10");
//            tvVoteAverage.setText(voteAverage);
//        String overview = movie.getOverview();
//        if (overview != null)
//            tvOverview.setText(overview);
    }

    private void checkFavorite() {

        String uriPath = mUri.getPath();
        String subPath = uriPath.substring(1, 6);

        //Toast.makeText(this, "extractedPath ->" + subPath, Toast.LENGTH_LONG).show();
        if (subPath.equals("movie")) {
            //Toast.makeText(this, "from Movie", Toast.LENGTH_LONG).show();

            // check the movie exists in Favorite DB
            mUriFavorite = MovieContract.FavoriteMovieEntry.buildMovieUriWithMovieId(mMovieId);
            //Toast.makeText(this, "mUriFavorite -> " + mUriFavorite.toString(), Toast.LENGTH_LONG).show();
            String[] projection = {MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID};
            Cursor cursor = getContentResolver().query(mUriFavorite, projection, null, null, null);

            if (cursor.getCount() > 0) {
                setFavoriteOn();
            } else {
                setFavoriteOff();
            }

            Log.v("checkFavorite cnt  -> ", Integer.toString(cursor.getCount()));
            Log.v("checkFavorite Uri -> ", mUriFavorite.toString());

            cursor.close();

            //getSupportLoaderManager().initLoader(FAVORITE_MOVIE_DETAIL_LOADER, null, DetailActivity.this);

        } else {
            //Toast.makeText(this, "from Fav", Toast.LENGTH_LONG).show();
            setFavoriteOn();
        }
    }

    private void setFavoriteOn() {
        mFabFavorite.setImageResource(android.R.drawable.star_big_on);
        mFabFavorite.setTag("saved");
    }

    private void setFavoriteOff() {
        mFabFavorite.setImageResource(android.R.drawable.star_big_off);
        mFabFavorite.setTag("unsaved");
    }

    public void onClickFavorite(View v) {

        boolean isSaved = false;

        // if a movie exists...set color red
        // or set color grey

        //mFabFavorite.setTag("favorite_on");
        String favTag = mFabFavorite.getTag().toString();

        //Toast.makeText(this, "favTag -> "+ favTag, Toast.LENGTH_LONG).show();
        //return;

        if (favTag.equals("saved")) {
            // delete the movie

            // Build appropriate uri with String row id appended
            String stringId = mMovieId;
            //Uri uri = MovieContract.FavoriteMovieEntry.CONTENT_URI;
            //uri = uri.buildUpon().appendPath(stringId).build();

            Uri uri = MovieContract.FavoriteMovieEntry.buildMovieUriWithMovieId(mMovieId);

            //Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();

            getContentResolver().delete(uri, null, null);

            //getSupportLoaderManager().restartLoader(MOVIE_DETAIL_LOADER, null, DetailActivity.this);

            Toast.makeText(this, "The movie deleted!", Toast.LENGTH_LONG).show();
            mFabFavorite.setImageResource(android.R.drawable.star_big_off);

            setResult(RESULT_OK);

        } else {
            // save the movie
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, mMovieId);
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_TITLE, mMovieTitle.getText().toString());
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_URL, mPosterPath);
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, mReleaseDate.getText().toString());
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_AVERAGE, mVoteAverage.getText().toString());
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, mOverview.getText().toString());

            Uri uri = getContentResolver().insert(MovieContract.FavoriteMovieEntry.CONTENT_URI, contentValues);

            if(uri != null) {
                //Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, "The movie saved!", Toast.LENGTH_LONG).show();
                mFabFavorite.setImageResource(android.R.drawable.star_big_on);
                setResult(RESULT_OK);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case MOVIE_DETAIL_LOADER:
                //Log.v("Loader -> ", String.valueOf(loaderId));
                return new CursorLoader(this,
                        mUri,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null);

            case FAVORITE_MOVIE_DETAIL_LOADER:
                //Log.v("Loader -> ", String.valueOf(loaderId));
                return new CursorLoader(this,
                        mUriFavorite,
                        null,
                        null,
                        null,
                        null);

            case TRAILER_LOADER:
                //Log.v("Loader -> ", String.valueOf(loaderId));
                return new CursorLoader(this,
                        mUriTrailer,
                        null,
                        null,
                        null,
                        null);

            case REVIEW_LOADER:
                //Log.v("Loader -> ", String.valueOf(loaderId));
                return new CursorLoader(this,
                        mUriReview,
                        null,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    private void setTrailer(String movieId) {
        TrailerFetchTask.getTrailer(this, movieId);
        mUriTrailer = MovieContract.TrailerEntry.CONTENT_URI;
        getSupportLoaderManager().initLoader(TRAILER_LOADER, null, DetailActivity.this);
    }

    private void setReview(String movieId) {
        ReviewFetchTask.getReview(this, movieId);
        mUriReview = MovieContract.ReviewEntry.CONTENT_URI;
        getSupportLoaderManager().initLoader(REVIEW_LOADER, null, DetailActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case MOVIE_DETAIL_LOADER:

                boolean cursorHasValidData = false;
                if (data != null && data.moveToFirst()) {
                    cursorHasValidData = true;
                }

                if (!cursorHasValidData) {
                    return;
                }

                mId = data.getInt(INDEX_ID);
                mMovieId = data.getString(INDEX_MOVIE_ID);
                String title = data.getString(INDEX_MOVIE_TITLE);
                mMovieTitle.setText(title);
                String releaseDate = data.getString(INDEX_MOVIE_RELEASE_DATE);
                mReleaseDate.setText(releaseDate);
                String voteAverage = data.getString(INDEX_MOVIE_AVERAGE);
                voteAverage.concat("\\/10");
                mVoteAverage.setText(voteAverage);
                String overview = data.getString(INDEX_MOVIE_OVERVIEW);
                mOverview.setText(overview);
                mPosterPath = data.getString(INDEX_MOVIE_POSTER_URL);
                String imgFullPath = NetworkUtils.MOIVE_POSTER_URL + mPosterPath;

                Log.v("path -> ", imgFullPath);
                Picasso.with(this)
                        .load(imgFullPath)
                        .into(mImgPoster);

                setTrailer(mMovieId);
                setReview(mMovieId);

                checkFavorite();

                break;

            case TRAILER_LOADER:
                mTrailerApdapter.swapCursor(data);

                break;

            case REVIEW_LOADER:
                mReviewApdapter.swapCursor(data);

                break;

//            case FAVORITE_MOVIE_DETAIL_LOADER:
//
//                int cnt = data.getCount();
//
//                Toast.makeText(this, "Count in Favorite DB --> " + cnt, Toast.LENGTH_LONG).show();
//
//                if (data.getCount() > 0) {
//                    setFavoriteOn();
//                } else {
//                    setFavoriteOff();
//                }
//
//                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case TRAILER_LOADER:
                mTrailerApdapter.swapCursor(null);
                break;

            case REVIEW_LOADER:
                mReviewApdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onClick(String key) {
        String url = "https://www.youtube.com/watch?v=" + key;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData (Uri.parse(url));
        startActivity(intent);
    }
}
