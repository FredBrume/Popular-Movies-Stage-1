package com.example.fredbrume.popularmovies.util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.model.MovieTrailer;
import com.example.fredbrume.popularmovies.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private ArrayList mTrailerData;
    private Context context;
    TrailerAdapterOnClickHandler mClickHandler;


    public interface TrailerAdapterOnClickHandler {

        void onTrailerClick(MovieTrailer mTrailerDetails);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder viewHolder, int position) {

        MovieTrailer trailer = (MovieTrailer) mTrailerData.get(position);

        System.out.println("MOVIE TRAILER: " + trailer);
        Picasso.with(context).load(NetworkUtils.buildYoutubeThumbnailURL(trailer.getTrailer_id()))
                .into(viewHolder.mImage);

    }

    @Override
    public int getItemCount() {
        return mTrailerData == null ? 0 : mTrailerData.size();

    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        public ImageView mImage;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.trailer_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            MovieTrailer trailer = (MovieTrailer) mTrailerData.get(adapterPosition);
            mClickHandler.onTrailerClick(trailer);
        }
    }

    public void setTrailerData(ArrayList<MovieTrailer> mTrailerDataList) {

        this.mTrailerData = mTrailerDataList;
        notifyDataSetChanged();
    }
}


