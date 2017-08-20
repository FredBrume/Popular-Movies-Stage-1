package com.example.fredbrume.popularmovies.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.model.MoviePoster;
import com.example.fredbrume.popularmovies.adapter.ReviewAdapter;
import com.example.fredbrume.popularmovies.adapter.TrailerAdapter;

public class FavoriteDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView bannerPoster;
    private MoviePoster posterDetails;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private FloatingActionButton fab;
    private ImageView poster;
    private static String movie_id;
    private TextView overview;
    private TextView title;
    private TextView year;
    private RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);


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


        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (intent != null) {

            if (intent.hasExtra(Intent.EXTRA_TEXT)) {

                title.setText(String.valueOf(posterDetails.getMovieTitle()));
                overview.setText(String.valueOf(posterDetails.getSypnosis()));
                rating.setRating(Float.parseFloat(posterDetails.getMovie_rating()) / 2);
                year.setText(String.valueOf(posterDetails.getMovie_year() + " " + "(Released)"));

                final byte[] bytesPoster = posterDetails.getPosterDBImg();
                final byte[] bytesBackDrop = posterDetails.getBackdropDBImg();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytesPoster, 0, bytesPoster.length);
                Bitmap bitmapBackdrop = BitmapFactory.decodeByteArray(bytesBackDrop, 0, bytesBackDrop.length);

                poster.setImageBitmap(bitmap);
                bannerPoster.setImageBitmap(bitmapBackdrop);

                movie_id = posterDetails.getMovie_id();

            }
        }
    }
}
