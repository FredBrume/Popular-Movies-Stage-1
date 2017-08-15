package com.example.fredbrume.popularmovies.view;

import com.example.fredbrume.popularmovies.model.MoviePoster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fredbrume.popularmovies.util.adapter.PosterAdapter;
import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.util.loaders.PosterAsyncLoader;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements PosterAsyncLoader.PosterTaskHandler,
        PosterAdapter.MovietAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

    private PosterAdapter mAdapter;
    private RecyclerView mPosterList;
    private ProgressBar mLoadingIndicator;
    private LoaderManager loaderManager;
    private String sort;
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

        loadSortSharedPreferences();
        loadPosterData();

    }


    private void loadSortSharedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sort = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.popular_value));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }


    private void loadPosterData() {

        showPosterView();

        Bundle bundle = new Bundle();
        bundle.putString(PosterAsyncLoader.POSTER_SORT, sort);

        loaderManager = getSupportLoaderManager();

        new PosterAsyncLoader(this, loaderManager, bundle, getBaseContext());
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

    @Override
    public void postExecutePoster(ArrayList<MoviePoster> posterList) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (posterList != null) {
            showPosterView();
            mAdapter.setPosterData(posterList);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_popular:

                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        loadSortSharedPreferences();
        loadPosterData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
