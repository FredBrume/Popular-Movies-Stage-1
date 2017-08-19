package com.example.fredbrume.popularmovies.util.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.fredbrume.popularmovies.model.MovieTrailer;
import com.example.fredbrume.popularmovies.util.ForeignDB.MovieDBjsonUtils;
import com.example.fredbrume.popularmovies.util.ForeignDB.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by fredbrume on 8/11/17.
 */

public class TrailerAsyncLoader implements LoaderManager.LoaderCallbacks<ArrayList<MovieTrailer>> {

    public TrailerTaskHandler trailerTaskHandler;

    private Bundle bundle;

    private Context context;

    private static final int LOADER_ID = 22;

    private LoaderManager mLoaderManager;

    private final static String POSTER_ID = "poster_id";


    public TrailerAsyncLoader(TrailerTaskHandler trailerTaskHandler,LoaderManager loadermanger, Bundle bundle, Context context) {

        this.trailerTaskHandler = trailerTaskHandler ;
        this.mLoaderManager = loadermanger;
        this.bundle = bundle;
        this.context = context;

        Loader<String> loader = mLoaderManager.getLoader(LOADER_ID);

        if (loader == null) {
            mLoaderManager.initLoader(LOADER_ID, bundle, this);
        } else {
            mLoaderManager.restartLoader(LOADER_ID, bundle, this);
        }
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<ArrayList<MovieTrailer>>(context) {

            ArrayList<MovieTrailer> trailers = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null) {
                    return;
                }

                if (trailers != null) {
                    deliverResult(trailers);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(ArrayList<MovieTrailer> data) {
                super.deliverResult(data);

                trailers =data;
            }

            @Override
            public ArrayList<MovieTrailer> loadInBackground() {

                String poster_id = args.getString(POSTER_ID);

                URL trailerRequestUrl = NetworkUtils.buildTrailerIDURL(poster_id);

                try {
                    String jsonTrailerResponse = NetworkUtils
                            .getResponseFromHttpUrl(trailerRequestUrl);

                    ArrayList<MovieTrailer> simpleJsonTrailerData = MovieDBjsonUtils.getMovieTrailerArrayListFromJson(jsonTrailerResponse);

                    return simpleJsonTrailerData;

                } catch (Exception e) {
                    e.printStackTrace();

                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieTrailer>> loader, ArrayList<MovieTrailer> data) {

        trailerTaskHandler.postExecuteTrailer(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public interface TrailerTaskHandler {

        void postExecuteTrailer(ArrayList<MovieTrailer> trailers);
    }
}
