package com.example.fredbrume.popularmovies.view;

import android.content.ContentValues;
import android.content.Intent;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
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

import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.adapter.ReviewAdapter;
import com.example.fredbrume.popularmovies.adapter.TrailerAdapter;
import com.example.fredbrume.popularmovies.util.ReviewAsyncLoader;
import com.example.fredbrume.popularmovies.util.TrailerAsyncLoader;
import com.example.fredbrume.popularmovies.model.MoviePoster;
import com.example.fredbrume.popularmovies.model.MovieTrailer;
import com.example.fredbrume.popularmovies.model.MoviewReview;
import com.example.fredbrume.popularmovies.util.NetworkUtils;
import com.example.fredbrume.popularmovies.util.FavoriteContentProviderHelper;
import com.example.fredbrume.popularmovies.util.FavoriteContract;
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

    private static String POSTER_ID = "poster_id";

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
                rating.setRating(Float.parseFloat(posterDetails.getMovie_rating())/2);
                year.setText(String.valueOf(posterDetails.getMovie_year() + " " + "(Released)"));
                movie_id = posterDetails.getMovie_id();

                Picasso.with(this).load(NetworkUtils.buildPosterURL() + posterDetails.getBackdrop_path())
                        .into(bannerPoster);

                checkMovieFavoriteInDB();

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
    }

    private void checkMovieFavoriteInDB()
    {
        if(FavoriteContentProviderHelper.checkForMovieFavorite(getBaseContext(),movie_id)){

            fab.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_favorite_like));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onClickAddFavorite(View view) {


        if(FavoriteContentProviderHelper.checkForMovieFavorite(getBaseContext(),movie_id))
        {
            getContentResolver().delete(FavoriteContract.FavoriteEntry.CONTENT_URI,"id=?",new String[]{movie_id.toString()});

                fab.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_favorite_border));

                Snackbar.make(view, getResources().getString(R.string.removed_from_favorite), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


        }else {

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
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_favorite_like));
                Snackbar.make(view, getResources().getString(R.string.added_to_favorite), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

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
}
