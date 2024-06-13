package com.example.frontflix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter favoritesAdapter;
    private DatabaseManager databaseManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritos);

        databaseManager = new DatabaseManager(this);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

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
        if (userId == -1) {
            Toast.makeText(this, "Erro ao carregar favoritos: usuário não identificado", Toast.LENGTH_SHORT).show();
            return;
        }
        List<MovieItem> favoriteMovies = databaseManager.getFavorites(userId);
        if (favoriteMovies == null || favoriteMovies.isEmpty()) {
            Toast.makeText(this, "Nenhum filme favorito encontrado", Toast.LENGTH_SHORT).show();
        } else {
            favoritesAdapter = new Adapter(this, favoriteMovies, true);
            recyclerView.setAdapter(favoritesAdapter);
        }
    }
}
