package com.example.fredbrume.popularmovies.util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fredbrume.popularmovies.R;
import com.example.fredbrume.popularmovies.model.MoviewReview;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList mReviewData;
    private Context context;
    private static boolean check;
    ReviewAdapterOnClickHandler mClickHandler;


    public interface ReviewAdapterOnClickHandler {

        void onMovieShowmoreClick(String Layout);

    }

    public ReviewAdapter(ReviewAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);


        return new ReviewViewHolder(view, mClickHandler);


    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder viewHolder, int position) {

        MoviewReview review = (MoviewReview) mReviewData.get(position);

        viewHolder.reviewer.setText(review.getReviewer());
        viewHolder.review.setText(review.getReview());

        if (check == true) viewHolder.review.setMaxLines(Integer.MAX_VALUE);
        check = false;


    }

    @Override
    public int getItemCount() {
        return mReviewData == null ? 0 : mReviewData.size();

    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        public TextView reviewer;
        public TextView review;


        public ReviewViewHolder(View itemView, ReviewAdapterOnClickHandler handler) {
            super(itemView);

            reviewer = (TextView) itemView.findViewById(R.id.reviewer);
            review = (TextView) itemView.findViewById(R.id.review);

            if (check == true) review.setMaxLines(Integer.MAX_VALUE);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            final String layoutType = "Vertical";
            mClickHandler.onMovieShowmoreClick(layoutType);

            check = true;


        }

    }

    public void setReviewData(ArrayList<MoviewReview> mReviewDataList) {

        this.mReviewData = mReviewDataList;
        notifyDataSetChanged();
    }
}


