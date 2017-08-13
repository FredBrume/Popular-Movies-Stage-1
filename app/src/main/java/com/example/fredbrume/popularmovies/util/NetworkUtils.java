package com.example.fredbrume.popularmovies.util;


import android.net.Uri;
import android.util.Log;

import com.example.fredbrume.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String YOUTUBE_TRAILER_IMG_BASE_URL = "https://img.youtube.com/vi";

    private static final String YOUTUBE_TRAILER_VIDEO_BASE_URL= "http://www.youtube.com/watch";

    private static final String YOUTUBE_TRAILER_FILE_FORMAT = "hqdefault.jpg";

    private static final String POPULAR_MOVIES_BASE_URL =
            "http://api.themoviedb.org/3/movie";

    private static final String POSTER_BASE_PATH = "http://image.tmdb.org/t/p";
    private static final String DIRECTORY_VIDEOS = "videos";
    private static final String DIRECTORY_REVIEWS = "reviews";
    private static final String POSTEER_SIZE = "w500";

    final private static String MOVIE_API_KEY = "api_key";


    public static URL buildSortUrl(String SortQuery) {

        Uri builtUri = Uri.parse(POPULAR_MOVIES_BASE_URL).buildUpon()
                .appendPath(SortQuery)
                .appendQueryParameter(MOVIE_API_KEY, BuildConfig.API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static String buildPosterURL() {

        Uri builtUri = Uri.parse(POSTER_BASE_PATH).buildUpon()
                .appendEncodedPath(POSTEER_SIZE).build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return String.valueOf(url);
    }

    public static String buildYoutubeThumbnailURL(String trailer_Id) {

        Uri builtUri = Uri.parse(YOUTUBE_TRAILER_IMG_BASE_URL).buildUpon()
                .appendPath(trailer_Id)
                .appendPath(YOUTUBE_TRAILER_FILE_FORMAT)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return String.valueOf(url);
    }

    public static String buildYoutubeVideolURL(String trailer_Id) {

        Uri builtUri = Uri.parse(YOUTUBE_TRAILER_VIDEO_BASE_URL).buildUpon()
                .appendPath(trailer_Id)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return String.valueOf(url);
    }

    public static URL buildTrailerIDURL(String poster_id) {
        Uri builtUri = Uri.parse(POPULAR_MOVIES_BASE_URL).buildUpon()
                .appendPath(poster_id)
                .appendPath(DIRECTORY_VIDEOS)
                .appendQueryParameter(MOVIE_API_KEY, BuildConfig.API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildReviewIDURL(String poster_id) {
        Uri builtUri = Uri.parse(POPULAR_MOVIES_BASE_URL).buildUpon()
                .appendPath(poster_id)
                .appendPath(DIRECTORY_REVIEWS)
                .appendQueryParameter(MOVIE_API_KEY, BuildConfig.API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000);

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
