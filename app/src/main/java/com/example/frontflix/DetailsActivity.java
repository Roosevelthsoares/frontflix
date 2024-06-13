package com.example.frontflix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieOverview;
    private Button btnTrailer;
    private Button btnBack;
    private Button btnAddToFavorites;
    private Button btnRemoveFromFavorites;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_page);

        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieOverview = findViewById(R.id.movieOverview);
        btnTrailer = findViewById(R.id.btnTrailer);
        btnBack = findViewById(R.id.btnBack);
        btnAddToFavorites = findViewById(R.id.btnAddToFavorites);
        btnRemoveFromFavorites = findViewById(R.id.btnRemoveFromFavorites);

        // Get data from Intent
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1); // Receber userId
        int movieId = intent.getIntExtra("movieId", -1);
        String title = intent.getStringExtra("movieTitle");
        String overview = intent.getStringExtra("movieOverview");
        String posterPath = intent.getStringExtra("moviePosterPath");
//        userId = intent.getIntExtra("userId", -1); // Recuperar userId

        // Set data to views
        movieTitle.setText(title);
        movieOverview.setText(overview);
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + posterPath).into(moviePoster);

        // Check if the movie is in favorites
        if (isFavorite(movieId)) {
            btnAddToFavorites.setVisibility(View.GONE);
            btnRemoveFromFavorites.setVisibility(View.VISIBLE);
        } else {
            btnAddToFavorites.setVisibility(View.VISIBLE);
            btnRemoveFromFavorites.setVisibility(View.GONE);
        }

        // Set click listener for the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous activity (movie search)
                finish();
            }
        });

        btnAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != -1) {
                    addToFavorites(userId, movieId, title, overview, posterPath);
                } else {
                    Toast.makeText(DetailsActivity.this, "Erro: usuário não identificado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRemoveFromFavorites.setOnClickListener(v -> removeFromFavorites(userId, movieId));

//        // Set click listener for the add to favorites button
//        btnAddToFavorites.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addToFavorites(movieId, title, overview, posterPath);
//            }
//        });
//
//        // Set click listener for the remove from favorites button
//        btnRemoveFromFavorites.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeFromFavorites(movieId);
//            }
//        });

        // Future implementation for trailer button
        btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Future implementation to open YouTube trailer
            }
        });
    }

    private boolean isFavorite(int movieId) {
        SharedPreferences sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favoriteMovies", "");
        Type type = new TypeToken<List<MovieItem>>() {}.getType();
        List<MovieItem> favoriteMovies = gson.fromJson(json, type);

        if (favoriteMovies != null) {
            for (MovieItem movie : favoriteMovies) {
                if (movie.getId() == movieId) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addToFavorites(int userId, int movieId, String title, String overview, String posterPath) {
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.addFavorite(userId, movieId);
        btnAddToFavorites.setVisibility(View.GONE);
        btnRemoveFromFavorites.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Adicionado aos Favoritos", Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorites(int userId, int movieId) {
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.removeFavorite(userId, movieId);
        btnAddToFavorites.setVisibility(View.VISIBLE);
        btnRemoveFromFavorites.setVisibility(View.GONE);
        Toast.makeText(this, "Removido dos Favoritos", Toast.LENGTH_SHORT).show();
    }
}
