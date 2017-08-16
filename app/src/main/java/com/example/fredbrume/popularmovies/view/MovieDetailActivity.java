package com.example.fredbrume.popularmovies.view;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.util.adapter.ReviewAdapter;
import com.example.fredbrume.popularmovies.util.adapter.TrailerAdapter;
import com.example.fredbrume.popularmovies.util.loaders.ReviewAsyncLoader;
import com.example.fredbrume.popularmovies.util.loaders.TrailerAsyncLoader;
import com.example.fredbrume.popularmovies.model.MoviePoster;
import com.example.fredbrume.popularmovies.model.MovieTrailer;
import com.example.fredbrume.popularmovies.model.MoviewReview;
import com.example.fredbrume.popularmovies.util.NetworkUtils;
import com.example.fredbrume.popularmovies.util.localDB.FavoriteContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAsyncLoader.TrailerTaskHandler, ReviewAsyncLoader.ReviewTaskHandler,
        TrailerAdapter.TrailerAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView bannerPoster;
    private MoviePoster posterDetails;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private FloatingActionButton fab;
    private ImageView poster;
    private TextView overview;
    private TextView title;
    private TextView year;
    private RatingBar rating;
    private static String movie_id;
    private LoaderManager loaderManager;
    private RecyclerView recyclerViewReview;

    private static String INTENT_ACTION = "favorite_on_click";
    private static String INTENT_KEY = "favorite";
    private static String INTENT_KEY_VALUE = "Movie added to favorite";

    private static String POSTER_ID = "poster_id";

    FavoriteBroadcastReceiver favoriteBroadcastReceiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Drawable navIcon = toolbar.getNavigationIcon();
        navIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        Intent intent = getIntent();
        poster = (ImageView) findViewById(R.id.review_item_poster);
        bannerPoster = (ImageView) findViewById(R.id.posterView);
        title = (TextView) findViewById(R.id.movie_title);
        overview = (TextView) findViewById(R.id.overview);
        year = (TextView) findViewById(R.id.year);
        rating = (RatingBar) findViewById(R.id.rating);
        posterDetails = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        RecyclerView recyclerViewTrailer = (RecyclerView) findViewById(R.id.rv_trailer);
        recyclerViewReview = (RecyclerView) findViewById(R.id.rv_reviewlist);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (intent != null) {

            if (intent.hasExtra(Intent.EXTRA_TEXT)) {

                title.setText(String.valueOf(posterDetails.getMovieTitle()));
                overview.setText(String.valueOf(posterDetails.getMovie_overview()));
                rating.setRating(Float.parseFloat(posterDetails.getMovie_rating()));

                year.setText(String.valueOf(posterDetails.getMovie_year() + " " + "(Released)"));

                movie_id = posterDetails.getMovie_id();

                Picasso.with(this).load(NetworkUtils.buildPosterURL() + posterDetails.getBackdrop_path())
                        .into(bannerPoster);

                loadTrailers();

                loadReviews();

                collapsingToolbarLayout.setTitle((posterDetails.getMovieTitle()));


            }

        }

        toolbarTextAppernce();

        loadToolbarColourFromPoster();

        trailerAdapter = new TrailerAdapter(this);
        recyclerViewTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTrailer.setAdapter(trailerAdapter);

        reviewAdapter = new ReviewAdapter(this);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewReview.setAdapter(reviewAdapter);

        favoriteBroadcastReceiver = new FavoriteBroadcastReceiver();
        intentFilter = new IntentFilter(INTENT_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(favoriteBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(favoriteBroadcastReceiver);
    }

    public void onClickAddFavorite(View view) {

        Bitmap bitmapPoster = ((BitmapDrawable) poster.getDrawable()).getBitmap();
        ByteArrayOutputStream baosPoster = new ByteArrayOutputStream();
        bitmapPoster.compress(Bitmap.CompressFormat.JPEG, 100, baosPoster);
        byte[] posterInByte = baosPoster.toByteArray();

        Bitmap bitmapBackdrop = ((BitmapDrawable) bannerPoster.getDrawable()).getBitmap();
        ByteArrayOutputStream baosBackdrop = new ByteArrayOutputStream();
        bitmapBackdrop.compress(Bitmap.CompressFormat.JPEG, 100, baosBackdrop);
        byte[] bitmapBackdropInByte = baosBackdrop.toByteArray();

        ContentValues contentValues = new ContentValues();

        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_BACKDROP, bitmapBackdropInByte);
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER, posterInByte);
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_TITLE, title.getText().toString());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_SYPNOSIS, overview.getText().toString());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING, String.valueOf(rating.getRating()));
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_DATE, year.getText().toString());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, movie_id);


        Uri uri = getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(INTENT_KEY, INTENT_KEY_VALUE);
        sendBroadcast(intent);

    }


    private void loadTrailers() {

        Bundle bundle = new Bundle();
        bundle.putString(POSTER_ID, posterDetails.getMovie_id());

        loaderManager = getSupportLoaderManager();

        new TrailerAsyncLoader(this, loaderManager, bundle, getBaseContext());

    }

    private void loadReviews() {
        Bundle bundle = new Bundle();
        bundle.putString(POSTER_ID, posterDetails.getMovie_id());

        new ReviewAsyncLoader(this, loaderManager, bundle, getBaseContext());
    }


    private void loadToolbarColourFromPoster() {

        Picasso.with(this)
                .load(NetworkUtils.buildPosterURL() + posterDetails.getPoster_path())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        assert poster != null;
                        poster.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getVibrantSwatch();

                                        int mutedColor = 0;

                                        if (textSwatch != null) {

                                            mutedColor = palette.getVibrantSwatch().getRgb();
                                        }

                                        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(mutedColor));
                                        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(mutedColor));
                                        fab.setBackgroundTintList(ColorStateList.valueOf(palette.getMutedColor(mutedColor)));

                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }

                });
    }

    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));

    }

    @Override
    public void onTrailerClick(MovieTrailer mTrailerDetails) {


        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse((NetworkUtils.buildYoutubeVideolURL(mTrailerDetails.getTrailer_id()))));

        startActivity(intent);
    }

    @Override
    public void postExecuteTrailer(ArrayList<MovieTrailer> trailers) {

        trailerAdapter.setTrailerData(trailers);
    }

    @Override
    public void postExecuteReview(ArrayList<MoviewReview> reviews) {

        reviewAdapter.setReviewData(reviews);
    }

    @Override
    public void onMovieShowmoreClick(String layoutType) {

        if (layoutType != null && layoutType.equals("Vertical")) {

            loadReviews();

            reviewAdapter = new ReviewAdapter(this);
            recyclerViewReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerViewReview.setAdapter(reviewAdapter);

            ViewGroup.LayoutParams params = recyclerViewReview.getLayoutParams();
            params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
            recyclerViewReview.setLayoutParams(params);

        }
    }

    private class FavoriteBroadcastReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context, intent.getStringExtra(INTENT_KEY), Toast.LENGTH_SHORT).show();
            fab.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));

        }
    }
}
