package com.example.fredbrume.popularmovies.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.fredbrume.popularmovies.model.MoviePoster;
import com.example.fredbrume.popularmovies.model.MovieTrailer;
import com.example.fredbrume.popularmovies.model.MoviewReview;


public class MovieDBjsonUtils {

    public static ArrayList<MoviePoster> getMovieDetailsArrayListFromJson(String posterUrlString) throws JSONException {


        ArrayList<MoviePoster> posterStringObj;

        JSONObject posterJsonObject = new JSONObject(posterUrlString);

        JSONArray movieDetailsArray = posterJsonObject.getJSONArray("results");

        if (movieDetailsArray != null) {

            posterStringObj = new ArrayList<>(movieDetailsArray.length());

            for (int i = 0; i < movieDetailsArray.length(); ++i) {

                JSONObject posterObject = movieDetailsArray.getJSONObject(i);

                MoviePoster mm = new MoviePoster();

                mm.setMovieTitle(String.valueOf(posterObject.get("title")));
                mm.setMovie_id(String.valueOf(posterObject.get("id")));
                mm.setMovie_year(String.valueOf(posterObject.get("release_date")));
                mm.setMovie_overview(String.valueOf(posterObject.get("overview")));
                mm.setMovie_rating(String.valueOf(posterObject.get("vote_average")));
                mm.setPoster_path(String.valueOf(posterObject.get("poster_path")));
                mm.setBackdrop_path(String.valueOf(posterObject.get("backdrop_path")));
                mm.setSypnosis(String.valueOf(posterObject.get("backdrop_path")));
                mm.setSypnosis(String.valueOf(posterObject.get("backdrop_path")));

                posterStringObj.add(mm);

            }

            return posterStringObj;
        }

        return null;
    }


    public static ArrayList<MovieTrailer> getMovieTrailerArrayListFromJson(String trailerUrlString) throws JSONException {

        ArrayList<MovieTrailer> trailerStringObj = null;

        JSONObject posterJsonObject = new JSONObject(trailerUrlString);

        JSONArray trailerDetailsArray = posterJsonObject.getJSONArray("results");


        if (trailerDetailsArray != null) {

            trailerStringObj = new ArrayList<>(trailerDetailsArray.length());

            for (int i = 0; i < trailerDetailsArray.length(); ++i) {

                JSONObject posterObject = trailerDetailsArray.getJSONObject(i);

                MovieTrailer trailer = new MovieTrailer();

                trailer.setTrailer_id(String.valueOf(posterObject.get("key")));

                trailerStringObj.add(trailer);

            }

            return trailerStringObj;
        }

        return null;
    }

    public static ArrayList<MoviewReview> getMovieReviewArrayListFromJson(String reviewUrlString) throws JSONException {

        ArrayList<MoviewReview> reviewStringObj = null;

        JSONObject posterJsonObject = new JSONObject(reviewUrlString);

        JSONArray reviewDetailsArray = posterJsonObject.getJSONArray("results");


        if (reviewDetailsArray != null) {

            reviewStringObj = new ArrayList<>(reviewDetailsArray.length());

            for (int i = 0; i < reviewDetailsArray.length(); ++i) {

                JSONObject posterObject = reviewDetailsArray.getJSONObject(i);

                MoviewReview review = new MoviewReview();

                review.setReviewer(String.valueOf(posterObject.get("author")));
                review.setReview(String.valueOf(posterObject.get("content")));

                reviewStringObj.add(review);

            }

            return reviewStringObj;
        }

        return null;
    }

}
