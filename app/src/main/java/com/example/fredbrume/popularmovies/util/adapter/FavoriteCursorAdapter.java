/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.fredbrume.popularmovies.util.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.util.localDB.FavoriteContract;


public class FavoriteCursorAdapter extends RecyclerView.Adapter<FavoriteCursorAdapter.FavoriteViewHolder> {

    private Cursor mCursor;
    private Context mContext;


    public FavoriteCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutIdForListItem = (viewType == 2) ? R.layout.poster_list_item_small : R.layout.poster_list_item_large;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {

        int moviePosterIndex = mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER);
        int movieRatingIndex = mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING);
        int movieTitleIndex = mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_TITLE);
        int movieOverviewIndex = mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_SYPNOSIS);


        mCursor.moveToPosition(position);

        final byte[] bytesPoster = mCursor.getBlob(moviePosterIndex);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytesPoster, 0, bytesPoster.length);

        switch (getItemViewType(position)) {

            case 2:
                holder.rating.setText(mCursor.getString(movieRatingIndex));
                holder.sImage.setImageBitmap(bitmap);
                break;

            case 1:

                holder.lImage.setImageBitmap(bitmap);

                holder.titleView.setText(mCursor.getString(movieTitleIndex));
                holder.overviewView.setText(mCursor.getString(movieOverviewIndex));
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    public Cursor swapCursor(Cursor c) {

        Toast.makeText(mContext, "I GOT THE FUCK HERE", Toast.LENGTH_SHORT).show();
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % 4 == 0)
            return 1;
        else
            return 2;
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {


        public ImageView sImage, lImage;
        public TextView rating;
        private TextView titleView, overviewView, readMoreView;


        public FavoriteViewHolder(View itemView) {
            super(itemView);

            sImage = (ImageView) itemView.findViewById(R.id.tv_item_poster);
            lImage = (ImageView) itemView.findViewById(R.id.image);
            rating = (TextView) itemView.findViewById(R.id.poster_rating);
            titleView = (TextView) itemView.findViewById(R.id.title);
            overviewView = (TextView) itemView.findViewById(R.id.overview);
            readMoreView = (TextView) itemView.findViewById(R.id.read_more);
        }
    }
}