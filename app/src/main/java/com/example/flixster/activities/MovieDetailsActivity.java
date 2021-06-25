package com.example.flixster.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubePlayer;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.net.URL;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity  {

    private static final String TAG = "MovieDetailsActivity";
    // the movie to display
    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivPosterDetail;
    ImageView ivBackdrop;
    RelativeLayout rlView;
    MovieTrailerActivity movieTrailerActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivPosterDetail = (ImageView) findViewById((R.id.ivPosterDetail));
        ivBackdrop = (ImageView) findViewById((R.id.ivBackdrop));
        rlView = (RelativeLayout) findViewById((R.id.rlView));
        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getVideoId()));
        // String id = movie.getVideoId();
//        movieTrailerActivity = new MovieTrailerActivity();
//        YouTubePlayer youTubePlayer = (YouTubePlayer) findViewById(R.id.player);
        // Log.i("MovieDetailsActivity", String.format("ID is '%s'", id));
        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        //  Log.d(TAG, "onCreate: "+movie.getVideoId());
        //This sets the image and backdrop
        String imageBackdropURL = movie.getBackdropPath();

        String imagePosterURL = movie.getPosterPath();


        Palette palette;
        try {
            Glide.with(this)
                    .asBitmap()
                    .load(imageBackdropURL)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                           Palette palette = Palette.from(resource).maximumColorCount(16).generate();

                            Palette.Swatch vibrant = palette.getDominantSwatch();
                            if(vibrant != null){ rlView.setBackgroundColor(vibrant.getRgb());
                                // Update the title TextView with the proper text color
                                tvTitle.setTextColor(vibrant.getTitleTextColor());
                                tvOverview.setTextColor(vibrant.getTitleTextColor());}

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });



        } catch (Exception e) {
            e.printStackTrace();
        }




        //then imageURL = backdrop image
        //else imageURL = poster image
        int radius = 30;
        int margin = 30;
        Glide.with(this).load(imagePosterURL).fitCenter().transform(new RoundedCornersTransformation(radius, margin)).into(ivPosterDetail);
        Glide.with(this).load(imageBackdropURL).into(ivBackdrop);

        ivBackdrop.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Log.d(TAG, "onCreate: Clicked!");

                                              //Log.d(TAG, "onCreate: " + movie_Trailer_URL);
                                              String movie_Trailer_URL = movie.getVideoId();
                                              AsyncHttpClient client = new AsyncHttpClient();
                                              client.get(movie_Trailer_URL, new JsonHttpResponseHandler() {
                                                  @Override
                                                  public void onSuccess(int i, Headers headers, JSON json) {
                                                      Log.d(TAG, "onSuccess");
                                                      JSONObject jsonObject = json.jsonObject;
                                                      try {
                                                          Log.d(TAG, jsonObject.toString());
                                                          String movieIDArrayString = jsonObject.getJSONArray("results").getString(0);
                                                          JSONObject movieIDJSON = new JSONObject(movieIDArrayString);
                                                          String movieID = movieIDJSON.getString("key");
                                                          Log.d(TAG, movieID);

                                                          Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                                                          intent.putExtra("movieURL", movieID);
                                                          startActivity(intent);
                                                      } catch (JSONException e) {
                                                          e.printStackTrace();
                                                      }
                                                      // JSONArray results = jsonObject.getJSONArray("results");

                                                  }

                                                  @Override
                                                  public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                                                      Log.d(TAG, "onFailure");
                                                  }
                                              });

                                          }
                                      }


        );


    }
}