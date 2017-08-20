package com.example.fredbrume.popularmovies.util;

import android.content.Context;
import android.database.Cursor;

import com.example.fredbrume.popularmovies.model.MoviePoster;

import java.util.ArrayList;

/**
 * Created by fredbrume on 8/20/17.
 */

public class FavoriteContentProviderHelper {

    public static ArrayList<MoviePoster> MoviePosterListFromDB(Context context) {

        MoviePoster moviePoster = new MoviePoster();
        ArrayList<MoviePoster> arrayListMoviePoster = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                moviePoster.setMovieTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_TITLE)));
                moviePoster.setMovie_id(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID)));
                moviePoster.setSypnosis(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_SYPNOSIS)));
                moviePoster.setBackdropDBImg(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_BACKDROP)));
                moviePoster.setPosterDBImg(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER)));
                moviePoster.setMovie_year(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_DATE)));
                moviePoster.setMovie_rating(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING)));

                arrayListMoviePoster.add(moviePoster);
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return arrayListMoviePoster;
    }

    public static boolean checkForMovieFavorite(Context context, String movie_id) {

        ArrayList<MoviePoster> list = MoviePosterListFromDB(context);

        System.out.println(list.toString());

        for (int i = 0; i < list.size(); ++i) {

            if (list.get(i).getMovie_id().equals(movie_id)) {

                return true;
            }
        }

        return false;
    }

    public static MoviePoster MoviePosterFromDB(Context context, int position) {

        MoviePoster moviePoster = new MoviePoster();

        Cursor cursor = context.getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                null, null, null, null);

        if (cursor.moveToPosition(position)) {

            moviePoster.setMovieTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_TITLE)));
            moviePoster.setMovie_id(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID)));
            moviePoster.setSypnosis(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_SYPNOSIS)));
            moviePoster.setBackdropDBImg(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_BACKDROP)));
            moviePoster.setPosterDBImg(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER)));
            moviePoster.setMovie_year(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_DATE)));
            moviePoster.setMovie_rating(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING)));
        }


        cursor.close();

        return moviePoster;
    }
}
