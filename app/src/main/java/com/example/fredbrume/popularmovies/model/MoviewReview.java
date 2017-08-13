package com.example.fredbrume.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fredbrume on 8/10/17.
 */

public class MoviewReview implements Parcelable{

    private String reviewer;
    private String review;

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reviewer);
        dest.writeString(this.review);
    }

    public MoviewReview() {
    }

    protected MoviewReview(Parcel in) {
        this.reviewer = in.readString();
        this.review = in.readString();
    }

    public static final Creator<MoviewReview> CREATOR = new Creator<MoviewReview>() {
        @Override
        public MoviewReview createFromParcel(Parcel source) {
            return new MoviewReview(source);
        }

        @Override
        public MoviewReview[] newArray(int size) {
            return new MoviewReview[size];
        }
    };
}
