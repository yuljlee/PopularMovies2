package com.nanodegree.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.nanodegree.android.popularmovies.utilities.MovieJsonUtils;
import com.nanodegree.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieApdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private String mSortBy;
    private static final String SORT_ORDER_KEY = "sortBy";
    private static final String SORT_POPULARITY = "popular";
    private static final String SORT_RATED = "rated";
    private static final int COL_NUM = 3;

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
        mMovieApdapter = new MovieAdapter(this);
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

        loadMovieData(mSortBy);
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
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("movie", movie);

        startActivity(intentToStartDetailActivity);
    }

    private void loadMovieData(String sortOrder) {
        new FetchMovieTask().execute(sortOrder);
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
        else
            menu.getItem(1).setChecked(true);

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
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortOrder = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(sortOrder);
            //Log.v(TAG, "Parsed data " + weatherRequestUrl);
            try {
                // get joson format movie data from server
                String jsonMoiveResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                //Log.v(TAG, "Json Response - " + jsonMoiveResponse);
                ArrayList<Movie> parsedMovieData = MovieJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, jsonMoiveResponse);

                return parsedMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMoiveDataView();
                mMovieApdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }

    }
}
