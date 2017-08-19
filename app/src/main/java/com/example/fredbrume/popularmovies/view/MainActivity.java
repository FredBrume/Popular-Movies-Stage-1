package com.example.fredbrume.popularmovies.view;

import com.example.fredbrume.popularmovies.model.MoviePoster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import com.example.fredbrume.popularmovies.util.adapter.FavoriteCursorAdapter;
import com.example.fredbrume.popularmovies.util.adapter.PosterAdapter;
import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.util.loaders.FavoriteAsyncLoader;
import com.example.fredbrume.popularmovies.util.loaders.PosterAsyncLoader;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements PosterAsyncLoader.PosterTaskHandler,
        PosterAdapter.MovietAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener, FavoriteAsyncLoader.FavoriteTaskHandler {

    private PosterAdapter mAdapter;
    private RecyclerView mPosterList;
    private ProgressBar mLoadingIndicator;
    private LoaderManager loaderManager;
    private String sort;
    private Toolbar toolbar;
    private FavoriteCursorAdapter cursorAdapter;
    public static final String POP_OR_TOP_LOADER_ID = "pop_top_loader_id";
    private static final int FAVORITE_LOADER_value = 66;
    public static final String FAVORITE_LOADER_ID = "pop_top_loader_id";
    private static final int POP_OR_TOP_LOADER_value = 44;

    public final static String POSTER_SORT = "poster_sort";
    private static final String sortFavorite = "favorite";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mPosterList = (RecyclerView) findViewById(R.id.rv_posters);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        getColumnSpanCount(layoutManager);


        mPosterList.setLayoutManager(layoutManager);
        mPosterList.setHasFixedSize(true);

        getSortedObject();
    }

    private void getSortedObject() {

        loadPosterData();

        switch (loadSortSharedPreferences()) {
            case sortFavorite:

                cursorAdapter = new FavoriteCursorAdapter(this);
                mPosterList.setAdapter(cursorAdapter);

                break;

            default:
                mAdapter = new PosterAdapter(this);
                mPosterList.setAdapter(mAdapter);

                break;
        }
    }

    private void getColumnSpanCount(GridLayoutManager gridLayoutManager) {

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((position + 1) % 4 == 0)
                    return 3;
                else
                    return 1;
            }
        });
    }


    private String loadSortSharedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sort = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.popular_value));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        return sort;
    }


    private void loadPosterData() {

        showPosterView();

        Bundle bundlePopularOrTopRated = new Bundle();
        Bundle bundleFavorite = new Bundle();

        bundlePopularOrTopRated.putString(POSTER_SORT, loadSortSharedPreferences());
        bundlePopularOrTopRated.putInt(POP_OR_TOP_LOADER_ID, POP_OR_TOP_LOADER_value);

        bundleFavorite.putInt(FAVORITE_LOADER_ID, FAVORITE_LOADER_value);

        loaderManager = getSupportLoaderManager();

        if (sort.equals(sortFavorite)) {

            new FavoriteAsyncLoader(this, loaderManager, bundleFavorite, getBaseContext());

        } else {
            new PosterAsyncLoader(this, loaderManager, bundlePopularOrTopRated, getBaseContext());
        }
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

    @Override
    public void onFavoriteFinishTask(Cursor cursor) {

        if (cursor != null) {
            cursorAdapter.swapCursor(cursor);
        }
    }
}
