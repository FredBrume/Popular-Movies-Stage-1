package com.example.fredbrume.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fredbrume on 7/27/17.
 */

public class MoviePoster implements Parcelable {

    private String movieTitle;
    private String backdrop_path;
    private String sypnosis;
    private String movie_year;
    private String movie_rating;
    private String movie_overview;
    private String movie_id;
    private String poster_path;
    private String duration;

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getSypnosis() {
        return sypnosis;
    }

    public void setSypnosis(String sypnosis) {
        this.sypnosis = sypnosis;
    }

    public String getMovie_year() {
        return movie_year;
    }

    public void setMovie_year(String movie_year) {
        this.movie_year = movie_year;
    }

    public String getMovie_rating() {
        return movie_rating;
    }

    public void setMovie_rating(String movie_rating) {
        this.movie_rating = movie_rating;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public void setMovie_overview(String movie_overview) {
        this.movie_overview = movie_overview;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieTitle);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.sypnosis);
        dest.writeString(this.movie_year);
        dest.writeString(this.movie_rating);
        dest.writeString(this.movie_overview);
        dest.writeString(this.movie_id);
        dest.writeString(this.poster_path);
        dest.writeString(this.duration);
    }

    public MoviePoster() {
    }

    protected MoviePoster(Parcel in) {
        this.movieTitle = in.readString();
        this.backdrop_path = in.readString();
        this.sypnosis = in.readString();
        this.movie_year = in.readString();
        this.movie_rating = in.readString();
        this.movie_overview = in.readString();
        this.movie_id = in.readString();
        this.poster_path = in.readString();
        this.duration = in.readString();
    }

    public static final Creator<MoviePoster> CREATOR = new Creator<MoviePoster>() {
        @Override
        public MoviePoster createFromParcel(Parcel source) {
            return new MoviePoster(source);
        }

        @Override
        public MoviePoster[] newArray(int size) {
            return new MoviePoster[size];
        }
    };
}
