package com.example.frontflix;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloActivity extends AppCompatActivity {
    private TextView textView;
    private EditText editTextMovieTitle;
    private ImageButton imageButtonFetchMovie;
    private ExecutorService executorService;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_inicial);

        textView = findViewById(R.id.textView);
        editTextMovieTitle = findViewById(R.id.editText);
        imageButtonFetchMovie = findViewById(R.id.imageButton);
        imageView =findViewById(R.id.imageView2);
        executorService = Executors.newSingleThreadExecutor();


        imageButtonFetchMovie.setOnClickListener(v -> {
            String movieTitle = editTextMovieTitle.getText().toString();
            if (!movieTitle.isEmpty()) {
                fetchMovieData(movieTitle).observe(HelloActivity.this, response -> {
                    if (response != null) {
                        textView.setText(response);
                    } else {
                        Toast.makeText( HelloActivity.this, "Fetch error", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(HelloActivity.this, "Por favor, digite o nome de um filme.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private LiveData<String> fetchMovieData(String movieTitle) {
        MutableLiveData<String> liveData = new MutableLiveData<>();

        Integer id = null;

        executorService.execute(() -> {
            try {
                URL url = new URL("https://api.themoviedb.org/3/search/movie?&query=" + movieTitle + "&language=pt-BR");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NjVhZmExM2I4ZjRiZTFlMmUxYjVkN2JkYzlhYzQ0OCIsInN1YiI6IjY2Njg0OTkxOTE0Yjg4OTA3YWU5Zjg0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.uaQuHYRaxcCdeuhIItTGLGStMGybUH0xZx1HVgLJBbk");

                JSONArray resultados = getJsonArray(conn);

                StringBuilder output = new StringBuilder();
                for (int i = 0; i < resultados.length(); i++) {
                    JSONObject filme = resultados.getJSONObject(i);
                    output.append("Title: ").append(filme.getString("title")).append("\n");
//                    output.append("Overview: ").append(filme.getString("overview")).append("\n\n");
                    output.append("Id: ").append(filme.getInt("id")).append("\n\n");
                }

                liveData.postValue(output.toString());
            } catch (Exception e) {
                liveData.postValue(null);
            }
        });

        return liveData;
    }

    @NonNull
    private static JSONArray getJsonArray(HttpURLConnection conn) throws IOException, JSONException {
        URL url2 = new URL("https://api.themoviedb.org/3/movie/"+ 671 +"/images");
        HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
        conn2.setRequestMethod("GET");
        conn2.setRequestProperty("Accept", "application/json");
        conn2.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NjVhZmExM2I4ZjRiZTFlMmUxYjVkN2JkYzlhYzQ0OCIsInN1YiI6IjY2Njg0OTkxOTE0Yjg4OTA3YWU5Zjg0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.uaQuHYRaxcCdeuhIItTGLGStMGybUH0xZx1HVgLJBbk");


        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();

        JSONObject json = new JSONObject(response.toString());
        JSONArray resultados = json.getJSONArray("results");
        return resultados;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
