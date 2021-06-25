package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.activities.MovieDetailsActivity;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the movie at the passed in position
       // Log.d("MovieAdapter", "onBindViewHolder" + position);
        Movie movie = movies.get(position);
        //Bind the movie data into the ViewHolder
        holder.bind(movie);
    }

    //Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());

            tvOverview.setText(movie.getOverview());
            String movieOverview = movie.getOverview();
            int maxLine = tvOverview.getMaxLines();
            tvOverview.post(new Runnable() {
                @Override
                public void run() {
                    int lineEndIndex = tvOverview.getLineCount();
                    Log.i("TVOverview", String.valueOf(lineEndIndex));
                    if (lineEndIndex > maxLine - 2) {
                        String readMore = "(Read More...)";

                       int startOfLine = tvOverview.getLayout().getLineStart(maxLine-2);
                        int endOfLine = tvOverview.getLayout().getLineEnd(maxLine-2);
                       String lastTextLine = (String) tvOverview.getText().subSequence(0, endOfLine-readMore.length())+" "+readMore;
                        tvOverview.setText(lastTextLine);
                    }
                }
            });


//            String lastLine = "";
//
            String imageURL;

           // if phone is in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
            } else {
                imageURL = movie.getPosterPath();
            }
            //then imageURL = backdrop image
            //else imageURL = poster image
            int radius = 30;
            int margin = 5;

            Glide.with(context)
                    .load(imageURL)
                    .circleCrop().placeholder(R.drawable.flicks_movie_placeholder)

                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);
            ivPoster.setMaxHeight(20);
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getBindingAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}