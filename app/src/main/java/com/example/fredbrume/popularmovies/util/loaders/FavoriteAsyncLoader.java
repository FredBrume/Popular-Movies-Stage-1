package com.example.fredbrume.popularmovies.util.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;

import com.example.fredbrume.popularmovies.util.localDB.FavoriteContract;
import com.example.fredbrume.popularmovies.view.MainActivity;


/**
 * Created by fredbrume on 8/17/17.
 */

public class FavoriteAsyncLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = FavoriteAsyncLoader.class.getSimpleName();
    private FavoriteTaskHandler favoriteTaskHandler; //Sharing interface with PosterAsyncLoader class
    private Context context;
    private Bundle bundle;
    private LoaderManager manager;
    public final static String FAVORITE_SORT = "favorite_sort";

    public FavoriteAsyncLoader(FavoriteTaskHandler favoriteTaskHandler, LoaderManager loaderManager, Bundle bundle, Context context) {

        this.favoriteTaskHandler = favoriteTaskHandler;
        this.context = context;
        this.manager = loaderManager;
        this.bundle = bundle;

        Loader<String> loader = loaderManager.getLoader((Integer) bundle.get(MainActivity.FAVORITE_LOADER_ID));

        if (loader == null) {
            manager.initLoader((Integer) bundle.get(MainActivity.FAVORITE_LOADER_ID), bundle, this);
        } else {
            manager.restartLoader((Integer) bundle.get(MainActivity.FAVORITE_LOADER_ID), bundle, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<Cursor>(context) {
            Cursor cursorPoster;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                Toast.makeText(context, "I GOT THE FUCK HERE", Toast.LENGTH_SHORT).show();

                if (cursorPoster != null) {
                    deliverResult(cursorPoster);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                super.deliverResult(data);
                cursorPoster = data;
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return context.getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        favoriteTaskHandler.onFavoriteFinishTask(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface FavoriteTaskHandler{

         void onFavoriteFinishTask(Cursor cursor);
    }
}
