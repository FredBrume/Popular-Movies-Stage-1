package com.example.fredbrume.popularmovies.util;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.fredbrume.popularmovies.util.FavoriteContract;
import com.example.fredbrume.popularmovies.util.FavoriteDBHelper;

import static com.example.fredbrume.popularmovies.util.FavoriteContract.FavoriteEntry.TABLE_NAME;

/**
 * Created by fredbrume on 8/15/17.
 */

public class FavoriteContentProvider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_TASKS, FAVORITES);
        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_TASKS + "/#", FAVORITES_WITH_ID);

        return uriMatcher;
    }

    private FavoriteDBHelper mFavoriteDbHelper;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mFavoriteDbHelper = new FavoriteDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFavoriteDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {

            case FAVORITES:

                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITES:

                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteContract.FavoriteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        int favoriteDeleted;

        favoriteDeleted = db.delete(TABLE_NAME, selection, selectionArgs);

        if (favoriteDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favoriteDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
