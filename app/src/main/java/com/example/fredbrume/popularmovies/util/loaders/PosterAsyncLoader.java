package com.example.fredbrume.popularmovies.util.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.fredbrume.popularmovies.model.MoviePoster;
import com.example.fredbrume.popularmovies.util.ForeignDB.MovieDBjsonUtils;
import com.example.fredbrume.popularmovies.util.ForeignDB.NetworkUtils;
import com.example.fredbrume.popularmovies.view.MainActivity;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by fredbrume on 8/11/17.
 */

public class PosterAsyncLoader implements LoaderManager.LoaderCallbacks<ArrayList<MoviePoster>> {

    public PosterTaskHandler posterTaskHandler;

    private Bundle bundle;

    private Context context;

    private LoaderManager mLoaderManager;



    public PosterAsyncLoader(PosterTaskHandler posterTaskHandler, LoaderManager loadermanger, Bundle bundle, Context context) {

        this.posterTaskHandler = posterTaskHandler ;
        this.mLoaderManager = loadermanger;
        this.bundle = bundle;
        this.context = context;

        Loader<String> loader = mLoaderManager.getLoader((Integer) bundle.get(MainActivity.POP_OR_TOP_LOADER_ID));

        if (loader == null) {
            mLoaderManager.initLoader((Integer) bundle.get(MainActivity.POP_OR_TOP_LOADER_ID), bundle, this);
        } else {
            mLoaderManager.restartLoader((Integer) bundle.get(MainActivity.POP_OR_TOP_LOADER_ID), bundle, this);
        }
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<ArrayList<MoviePoster>>(context) {

            ArrayList<MoviePoster> poster = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null) {
                    return;
                }

                if (poster != null) {
                    deliverResult(poster);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(ArrayList<MoviePoster> data) {
                super.deliverResult(data);
                poster =data;
            }

            @Override
            public ArrayList<MoviePoster> loadInBackground() {

                String poster_sort = args.getString(MainActivity.POSTER_SORT);

                URL posterRequestUrl = NetworkUtils.buildSortUrl(poster_sort);

                try {
                    String jsonPosterResponse = NetworkUtils
                            .getResponseFromHttpUrl(posterRequestUrl);

                    ArrayList<MoviePoster> simpleJsonPosterData = MovieDBjsonUtils.getMovieDetailsArrayListFromJson(jsonPosterResponse);

                    return simpleJsonPosterData;

                } catch (Exception e) {
                    e.printStackTrace();

                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MoviePoster>> loader, ArrayList<MoviePoster> data) {

        posterTaskHandler.postExecutePoster(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public interface PosterTaskHandler {

        void postExecutePoster(ArrayList<MoviePoster> poster);
    }
}
