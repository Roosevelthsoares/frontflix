package com.example.frontflix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MovieViewHolder> {
    private Context context;
    private List<MovieItem> movieList;

    public Adapter(Context context, List<MovieItem> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_sugestoes, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieItem movie = movieList.get(position);
        holder.tvMovieTitle.setText(movie.getTitle());

        if (!movie.getPosterPath().isEmpty()) {
            String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            Picasso.get().load(imageUrl).into(holder.imgPoster);
        } else {
            holder.imgPoster.setImageResource(R.drawable.logo); // Substitua por uma imagem placeholder
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovieList(List<MovieItem> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvMovieTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgUrl);
            tvMovieTitle = itemView.findViewById(R.id.name);
        }
    }
}