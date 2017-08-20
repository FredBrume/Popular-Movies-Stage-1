package com.example.fredbrume.popularmovies.adapter;

import com.example.fredbrume.popularmovies.model.MoviePoster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


        int layoutIdForListItem = (viewType == 2) ? R.layout.poster_list_item_small : R.layout.poster_list_item_large;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PosterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final PosterViewHolder viewHolder, int position) {

        MoviePoster poster = (MoviePoster) mPosterData.get(position);

        System.out.println(getItemViewType(position));

        switch (getItemViewType(position)) {

            case 2:
                viewHolder.rating.setText(poster.getMovie_rating() + "");
                Picasso.with(context).load(NetworkUtils.buildPosterURL() + poster.getPoster_path())
                        .into(viewHolder.sImage);
                break;
            case 1:

                Picasso.with(context).load(NetworkUtils.buildPosterURL() + poster.getPoster_path())
                        .into(viewHolder.lImage);

                viewHolder.titleView.setText(poster.getMovieTitle());
                viewHolder.overviewView.setText(poster.getMovie_overview());
                break;
        }


    }

    @Override
    public int getItemViewType(int position) {

        if ((position + 1) % 4 == 0) // remember Position returns 0 or 1 before computation
            return 1;
        else
            return 2;
    }

    @Override
    public int getItemCount() {
        return mPosterData == null ? 0 : mPosterData.size();

    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        public ImageView sImage, lImage;
        public TextView rating;
        private TextView titleView, overviewView, readMoreView;

        public PosterViewHolder(View itemView) {
            super(itemView);

            sImage = (ImageView) itemView.findViewById(R.id.tv_item_poster);
            lImage = (ImageView) itemView.findViewById(R.id.image);
            rating = (TextView) itemView.findViewById(R.id.poster_rating);
            titleView = (TextView) itemView.findViewById(R.id.title);
            overviewView = (TextView) itemView.findViewById(R.id.overview);
            readMoreView = (TextView) itemView.findViewById(R.id.read_more);

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


