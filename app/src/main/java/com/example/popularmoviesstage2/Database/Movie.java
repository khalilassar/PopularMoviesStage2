package com.example.popularmoviesstage2.Database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
@Entity
public class Movie {
    @PrimaryKey
    private int id;
    private double popularity, vote_average;
    private int vote_count;
    private boolean video, adult;
    private String title, original_language, original_title, backdrop_path, overview, poster_path, release_date;
    @Ignore
    private ArrayList<Integer> genre_ids;
    @Ignore
    public Movie() {

    }
    @Ignore
    public Movie(double popularity, double vote_average, int id, int vote_count, boolean video, boolean adult, String title, String original_language, String original_title, String backdrop_path, String overview, String poster_path, String release_date, ArrayList<Integer> genre_ids) {
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.id = id;
        this.vote_count = vote_count;
        this.video = video;
        this.adult = adult;
        this.title = title;
        this.original_language = original_language;
        this.original_title = original_title;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
    }
    //WITHOUT ID AND GENRES
    public Movie(double popularity, double vote_average, int vote_count, boolean video, boolean adult, String title, String original_language, String original_title, String backdrop_path, String overview, String poster_path, String release_date) {
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.video = video;
        this.adult = adult;
        this.title = title;
        this.original_language = original_language;
        this.original_title = original_title;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public boolean getVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean getAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }
}
