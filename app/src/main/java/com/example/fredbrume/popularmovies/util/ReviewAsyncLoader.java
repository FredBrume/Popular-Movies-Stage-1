package com.example.fredbrume.popularmovies.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.fredbrume.popularmovies.model.MoviewReview;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by fredbrume on 8/11/17.
 */

public class ReviewAsyncLoader implements LoaderManager.LoaderCallbacks<ArrayList<MoviewReview>> {

    public ReviewTaskHandler reviewTaskHandler;

    private Bundle bundle;

    private Context context;

    private LoaderManager mLoaderManager;

    private static final int LOADER_ID = 33;

    private final static String POSTER_ID = "poster_id";


    public ReviewAsyncLoader(ReviewTaskHandler trailerTaskHandler, LoaderManager loadermanger, Bundle bundle, Context context) {

        this.reviewTaskHandler = trailerTaskHandler ;
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

        return new AsyncTaskLoader<ArrayList<MoviewReview>>(context) {

            ArrayList<MoviewReview> reviews = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null) {
                    return;
                }

                if (reviews != null) {
                    deliverResult(reviews);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(ArrayList<MoviewReview> data) {
                super.deliverResult(data);
                reviews =data;
            }

            @Override
            public ArrayList<MoviewReview> loadInBackground() {

                String poster_id = args.getString(POSTER_ID);

                URL trailerRequestUrl = NetworkUtils.buildReviewIDURL(poster_id);

                try {
                    String jsonReviewResponse = NetworkUtils
                            .getResponseFromHttpUrl(trailerRequestUrl);

                    ArrayList<MoviewReview> simpleJsonTrailerData = MovieDBjsonUtils.getMovieReviewArrayListFromJson(jsonReviewResponse);

                    return simpleJsonTrailerData;

                } catch (Exception e) {
                    e.printStackTrace();

                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MoviewReview>> loader, ArrayList<MoviewReview> data) {

        reviewTaskHandler.postExecuteReview(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public interface ReviewTaskHandler {

        void postExecuteReview(ArrayList<MoviewReview> reviews);
    }
}
