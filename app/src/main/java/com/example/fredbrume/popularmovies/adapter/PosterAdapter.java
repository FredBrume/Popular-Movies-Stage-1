package com.example.fredbrume.popularmovies.adapter;

import com.example.fredbrume.popularmovies.model.MoviePoster;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    private ArrayList mPosterData;
    private Context context;
    MovietAdapterOnClickHandler mClickHandler;


    public interface MovietAdapterOnClickHandler {

        void onClick(MoviePoster mPosterDetails);
    }

    public PosterAdapter(MovietAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.poster_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PosterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final PosterViewHolder viewHolder, int position) {

        MoviePoster poster = (MoviePoster) mPosterData.get(position);
        Picasso.with(context).load(NetworkUtils.buildPosterURL() + poster.getPoster_path())
                .into(viewHolder.mImage);

    }

    @Override
    public int getItemCount() {
        return mPosterData == null ? 0 : mPosterData.size();

    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        public ImageView mImage;

        public PosterViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.tv_item_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            MoviePoster poster = (MoviePoster) mPosterData.get(adapterPosition);
            mClickHandler.onClick(poster);
        }
    }

    public void setPosterData(ArrayList<MoviePoster> mPosterDataList) {

        this.mPosterData = mPosterDataList;
        notifyDataSetChanged();
    }
}


