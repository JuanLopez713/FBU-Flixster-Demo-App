package com.example.flixster.models;

import android.util.Log;

import com.example.flixster.BuildConfig;
import com.example.flixster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
public class Movie {
    private static final String TAG = "Movie";
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    Integer id;
    String movieId;
    // no-arg, empty constructor required for Parceler
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
      //  Log.d(TAG, String.valueOf(jsonObject));
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
        movieId = getVideoId();
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i =0; i< movieJsonArray.length();i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));

        }
        return movies;
    }

    public Integer getId() {
        Log.d(TAG, "getId: " + id);
        return id;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getVideoId(){

        String videoId = "https://api.themoviedb.org/3/movie/"+id.toString()+"/videos?api_key="+ BuildConfig.TMDB_KEY+"&language=en-US";



        return String.format(videoId);
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
