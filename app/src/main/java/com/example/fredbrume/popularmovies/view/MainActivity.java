package com.example.fredbrume.popularmovies.view;

import com.example.fredbrume.popularmovies.model.MoviePoster;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fredbrume.popularmovies.adapter.PosterAdapter;
import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.util.MovieDBjsonUtils;
import com.example.fredbrume.popularmovies.util.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements PosterAdapter.MovietAdapterOnClickHandler {

    private PosterAdapter mAdapter;
    private RecyclerView mPosterList;
    private static String sortType = "popular";
    private ProgressBar mLoadingIndicator;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mPosterList = (RecyclerView) findViewById(R.id.rv_posters);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mPosterList.setLayoutManager(layoutManager);

        mPosterList.setHasFixedSize(true);

        mAdapter = new PosterAdapter(this);

        mPosterList.setAdapter(mAdapter);

        loadPosterData();
    }


    private void loadPosterData() {

        showPosterView();

        URL sortUrl = NetworkUtils.buildSortUrl(sortType);
        new FetchPosterTask().execute(sortUrl);
    }

    private void showPosterView() {
        mPosterList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        mPosterList.setVisibility(View.INVISIBLE);
        Toast.makeText(MainActivity.this, "Error: Unable to connect to server", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(MoviePoster mPosterDetails) {

        Context context = this;
        Class destinationClass = MovieDetailActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, mPosterDetails);
        startActivity(intentToStartDetailActivity);
    }

    private class FetchPosterTask extends AsyncTask<URL, Void, ArrayList<MoviePoster>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MoviePoster> doInBackground(URL... params) {

            URL url = params[0];

            ArrayList<MoviePoster> simpleJsonPosterData;

            try {
                String jsonPosterResponse = NetworkUtils.getResponseFromHttpUrl(url);

                simpleJsonPosterData = MovieDBjsonUtils
                        .getMovieDetailsArrayListFromJson(jsonPosterResponse);

                return simpleJsonPosterData;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<MoviePoster> list) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (list != null) {
                showPosterView();
                mAdapter.setPosterData(list);
            } else {
                showErrorMessage();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_popular:

                sortType = "popular";
                loadPosterData();
                break;

            case R.id.action_top_rated:

                sortType = "top_rated";
                loadPosterData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);

        return true;
    }
}
