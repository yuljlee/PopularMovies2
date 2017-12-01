package com.nanodegree.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nanodegree.android.popularmovies.data.MovieContract;
import com.nanodegree.android.popularmovies.sync.MovieSyncUtils;
import com.nanodegree.android.popularmovies.utilities.MovieJsonUtils;
import com.nanodegree.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieApdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private String mSortBy;
    private static final String SORT_ORDER_KEY = "sortBy";
    private static final String SORT_POPULARITY = "popular";
    private static final String SORT_RATED = "rated";
    private static final String SORT_FAVORITE = "favorite";

    private static final int COL_NUM = 3;

    private static final int MOVIE_LOADER_ID = 77;
    private static final int FAVORITE_MOVIE_LOADER_ID = 78;

    private int mPosition = RecyclerView.NO_POSITION;
    private final int PICK_MOVIE_REQUEST = 27;
    private final int RESULT_FAVORITE = 28;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPreferences();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, COL_NUM);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieApdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieApdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(SORT_ORDER_KEY)) {
//                mSortBy = savedInstanceState.getString(SORT_ORDER_KEY).toString();
//            }
//        } else {
//            mSortBy = "popularity";
//        }

        //Log.v(TAG, "mSortBy - " + mSortBy);
        //loadMovieData(mSortBy);

        LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("keySortBy", mSortBy);
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, bundleForLoader, callback);
        getSupportLoaderManager().initLoader(FAVORITE_MOVIE_LOADER_ID, bundleForLoader, callback);

        Log.d(TAG, "before init...");
        //MovieSyncUtils.initialize(this);
        MovieSyncUtils.startImmediateSync(this, mSortBy);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle args) {
        switch (loaderId) {
            case MOVIE_LOADER_ID:

                return new CursorLoader(this,
                        MovieContract.MovieEntry.CONTENT_URI,
                        //MAIN_WEATEHR_PROJECTION,
                        null,
                        null,
                        null,
                        null);

            case FAVORITE_MOVIE_LOADER_ID:

                return new CursorLoader(this,
                        MovieContract.FavoriteMovieEntry.CONTENT_URI,
                        //MAIN_WEATEHR_PROJECTION,
                        null,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loaing Error " + loaderId);
        }
//        return new AsyncTaskLoader<ArrayList<Movie>>(this) {
//
//            ArrayList<Movie> mMovieData = null;
//
//            @Override
//            protected void onStartLoading() {
//                super.onStartLoading();
//
//                if (mMovieData != null) {
//                    deliverResult(mMovieData);
//                } else {
//                    mLoadingIndicator.setVisibility(View.VISIBLE);
//                    forceLoad();
//                }
//            }
//
//            @Override
//            public ArrayList<Movie> loadInBackground() {
//                String sortOrder = args.getString("keySortBy", SORT_POPULARITY);
//                URL movieRequestUrl = NetworkUtils.buildUrl(sortOrder);
//                //Log.v(TAG, "Parsed data " + weatherRequestUrl);
//                try {
//                    // get joson format movie data from server
//                    String jsonMoiveResponse = NetworkUtils
//                            .getResponseFromHttpUrl(movieRequestUrl);
//
//                    Log.v(TAG, "Json Response - " + jsonMoiveResponse);
//                    ArrayList<Movie> parsedMovieData = MovieJsonUtils
//                            .getSimpleMovieStringsFromJson(MainActivity.this, jsonMoiveResponse);
//
//                    return parsedMovieData;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            public void deliverResult(ArrayList<Movie> data) {
//                super.deliverResult(data);
//                //Toast.makeText(getApplicationContext(), "deliverResult", Toast.LENGTH_LONG).show();
//                mMovieData = data;
//            }
//        };
    }

    public void setMenuOrder(String menuOrder){
        mSortBy = menuOrder;
    };

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mMovieApdapter.swapCursor(data);

        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        mRecyclerView.smoothScrollToPosition(mPosition);

        Log.v("loader called", Integer.toString(mPosition));

//        if (loader.getId() == FAVORITE_MOVIE_LOADER_ID) {
//            mSortBy = SORT_FAVORITE;
//        }

        if (data.getCount() != 0) showMoiveDataView();
        //else Toast.makeText(getApplicationContext(), "No Data to load!", Toast.LENGTH_LONG).show();

//        mLoadingIndicator.setVisibility(View.INVISIBLE);
//        if (data != null) {
//            showMoiveDataView();
//            mMovieApdapter.setMovieData(data);
//        } else {
//            showErrorMessage();
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieApdapter.swapCursor(null);
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        mSortBy = sharedPreferences.getString(SORT_ORDER_KEY, SORT_POPULARITY);
        //Toast.makeText(this, "pref! ---> " + mSortBy, Toast.LENGTH_LONG).show();
    }

    private void savePreferences(String sortOrder) {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SORT_ORDER_KEY, sortOrder);
        editor.apply();

        mSortBy = sortOrder;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putString(SORT_ORDER_KEY, mSortBy);
        //Toast.makeText(this, "onSaveInstanceState ---> " + mSortBy, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();

        savePreferences(mSortBy);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(int indexId) {
//        Context context = this;
//        Class destinationClass = DetailActivity.class;
//        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
//        intentToStartDetailActivity.putExtra("movie", movie);
//        startActivity(intentToStartDetailActivity);

        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);

        // if selected movie is not the favorite, check it exists in favorite table to set the star on or off

        //Toast.makeText(this, "sort -> " + mSortBy, Toast.LENGTH_LONG).show();
        Log.d("onClick: indexId -> ", String.valueOf(indexId));
        Log.d("onClick: mSortBy -> ", mSortBy);
        Uri uriMovieClicked;
        if (mSortBy.equals(SORT_FAVORITE)) {
            uriMovieClicked = MovieContract.FavoriteMovieEntry.buildMovieUriWithId(indexId);
        } else {
            uriMovieClicked = MovieContract.MovieEntry.buildMovieUriWithId(indexId);
        }

        movieDetailIntent.setData(uriMovieClicked);
        //startActivity(movieDetailIntent);
        startActivityForResult(movieDetailIntent, PICK_MOVIE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_MOVIE_REQUEST) {
            if (resultCode == RESULT_OK) {

                Log.d("Result: favorite", String.valueOf(resultCode));
                //mSortBy = SORT_FAVORITE;

                //MenuItem actionRestart = (MenuItem) findViewById(R.id.action_favorite);
                onOptionsItemSelected(menu.findItem(R.id.action_favorite));
                savePreferences(SORT_FAVORITE);
                Log.d("Result: mSortBy -> ", mSortBy);
            }
        }
    }

    private void loadMovieData(String sortOrder) {
        LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString("keySortBy", sortOrder);

        if (sortOrder.equals(SORT_FAVORITE)) {
            getSupportLoaderManager().restartLoader(FAVORITE_MOVIE_LOADER_ID, null, callback);
        } else {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, bundleForLoader, callback);
            MovieSyncUtils.startImmediateSync(this, mSortBy);
        }

        //new FetchMovieTask().execute(sortOrder);
    }

    private void loadMovieData() {
        LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;
        //Bundle bundleForLoader = new Bundle();
        //bundleForLoader.putString("keySortBy", sortOrder);

        getSupportLoaderManager().initLoader(FAVORITE_MOVIE_LOADER_ID, null, callback);

        //MovieSyncUtils.startImmediateSync(this, mSortBy);

        //new FetchMovieTask().execute(sortOrder);
    }

    private void showMoiveDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        if (mSortBy.equals(SORT_POPULARITY))
            menu.getItem(0).setChecked(true);
        else if (mSortBy.equals(SORT_RATED))
            menu.getItem(1).setChecked(true);
        else
            menu.getItem(2).setChecked(true);

        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_most_popular) {

            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);

            mMovieApdapter.setMovieData(null);
            savePreferences(SORT_POPULARITY);

            loadMovieData(mSortBy);
            return true;
        } else if (id == R.id.action_top_rated) {

            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);

            mMovieApdapter.setMovieData(null);
            savePreferences(SORT_RATED);

            loadMovieData(mSortBy);
            return true;
        } else if (id == R.id.action_favorite) {

            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);

            mMovieApdapter.setMovieData(null);
            //mSortBy = SORT_FAVORITE;
            savePreferences(SORT_FAVORITE);

            loadMovieData(mSortBy);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected ArrayList<Movie> doInBackground(String... params) {
//
//            if (params.length == 0) {
//                return null;
//            }
//
//            String sortOrder = params[0];
//            URL movieRequestUrl = NetworkUtils.buildUrl(sortOrder);
//            //Log.v(TAG, "Parsed data " + weatherRequestUrl);
//            try {
//                // get joson format movie data from server
//                String jsonMoiveResponse = NetworkUtils
//                        .getResponseFromHttpUrl(movieRequestUrl);
//
//                Log.v(TAG, "Json Response - " + jsonMoiveResponse);
//                ArrayList<Movie> parsedMovieData = MovieJsonUtils
//                        .getSimpleMovieStringsFromJson(MainActivity.this, jsonMoiveResponse);
//
//                return parsedMovieData;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Movie> movieData) {
//            mLoadingIndicator.setVisibility(View.INVISIBLE);
//            if (movieData != null) {
//                showMoiveDataView();
//                mMovieApdapter.setMovieData(movieData);
//            } else {
//                showErrorMessage();
//            }
//        }
//
//    }
}
