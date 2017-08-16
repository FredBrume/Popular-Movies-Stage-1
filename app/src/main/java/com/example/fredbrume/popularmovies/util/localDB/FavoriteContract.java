package com.example.fredbrume.popularmovies.util.localDB;

import android.content.ContentProvider;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fredbrume on 8/15/17.
 */

public class FavoriteContract {

    public static final String AUTHORITY = "com.example.fredbrume.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_TASKS = "favorites";


    public static final class FavoriteEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER="poster";
        public static final String COLUMN_MOVIE_BACKDROP="backdrop";
        public static final String COLUMN_MOVIE_SYPNOSIS = "sypnosis";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_DATE = "date";

    }

}
