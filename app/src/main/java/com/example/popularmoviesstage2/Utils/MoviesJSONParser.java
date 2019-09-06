package com.example.popularmoviesstage2.Utils;


import com.example.popularmoviesstage2.Database.Movie;
import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.Review;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MoviesJSONParser {
    private ArrayList<Movie> movies = new ArrayList<>();

    public List<Movie> parseMoviesJSON(String response) {
        JSONObject jSONObject = null;
        try {
            jSONObject = new JSONObject(response);
        } catch (JSONException e) {
            Log.d("ddd", e.toString());
        }
        JSONArray moviesJSONArray = null;
        try {
            moviesJSONArray = jSONObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("ddd", e.toString());
        }
        Log.d("ddd", "start parsing");
        for (int i = 0; i < moviesJSONArray.length(); i++) {
            try {
                toMovieObject(moviesJSONArray.getJSONObject(i));
            } catch (JSONException e) {
                Log.d("ddd", e.toString());
            }
        }
        Log.d("ddd", "end parsing");
        return movies;
    }

    public static List<String> parseMoviesTrailers(String response) {
        ArrayList<String> trailersIds = new ArrayList<String>();
        JSONObject jSONObject = null;
        try {
            jSONObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray moviesJSONArray = null;
        try {
            moviesJSONArray = jSONObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < moviesJSONArray.length(); i++) {
            try {
                JSONObject trailerJson = (JSONObject) moviesJSONArray.get(i);
                if (trailerJson.optString("type").equals("Trailer") && trailerJson.optString("site").equals("YouTube")) {
                    trailersIds.add(trailerJson.getString("key"));
                }
            } catch (JSONException e) {
                Log.d("ddd", e.toString());
            }
        }
        Log.d("ddd", "end parsing");
        return trailersIds;
    }
    public static List<Review> parseMoviesReviews(String response) {
        ArrayList<Review> ReviewsList = new ArrayList<Review>();
        JSONObject jSONObject = null;
        try {
            jSONObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray moviesJSONArray = null;
        try {
            moviesJSONArray = jSONObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < moviesJSONArray.length(); i++) {
            try {
                JSONObject reviewJson = (JSONObject) moviesJSONArray.get(i);
                Review review=new Review(reviewJson.getString("author"),reviewJson.getString("content"));
                ReviewsList.add(review);
            } catch (JSONException e) {
                Log.d("ddd", e.toString());
            }
        }
        Log.d("ddd", "end parsing");
        return ReviewsList;
    }



    private void toMovieObject(JSONObject movieJSONObject) {
        //genre not parssed
        Movie movie = new Movie();
        movie.setPoster_path(movieJSONObject.optString("poster_path"));
        movie.setAdult(movieJSONObject.optBoolean("adult"));
        movie.setId(movieJSONObject.optInt("id"));
        movie.setOverview(movieJSONObject.optString("overview"));
        movie.setRelease_date(movieJSONObject.optString("release_date"));
        movie.setOriginal_title(movieJSONObject.optString("original_title"));
        movie.setOriginal_language(movieJSONObject.optString("original_language"));
        movie.setBackdrop_path(movieJSONObject.optString("backdrop_path"));
        movie.setVote_count(movieJSONObject.optInt("vote_count"));
        movie.setVideo(movieJSONObject.optBoolean("video"));
        movie.setTitle(movieJSONObject.optString("title"));
        movie.setPopularity(movieJSONObject.optDouble("popularity"));
        movie.setVote_average(movieJSONObject.optDouble("vote_average"));
        movies.add(movie);

    }

}
