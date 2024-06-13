package com.example.frontflix;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritos);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Use 3 columns in grid layout

        Button buttonVoltar = findViewById(R.id.button7);
        buttonVoltar.setOnClickListener(v -> finish());

        loadFavoriteMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteMovies();
    }

    private void loadFavoriteMovies() {
        SharedPreferences sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favoriteMovies", "");
        Type type = new TypeToken<List<MovieItem>>() {}.getType();
        List<MovieItem> favoriteMovies = gson.fromJson(json, type);

        if (favoriteMovies == null || favoriteMovies.isEmpty()) {
            Toast.makeText(this, "Nenhum filme favorito encontrado", Toast.LENGTH_SHORT).show();
        } else {
            favoritesAdapter = new Adapter(this, favoriteMovies, true);
            recyclerView.setAdapter(favoritesAdapter);
        }
    }
}
