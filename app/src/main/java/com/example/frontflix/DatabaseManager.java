package com.example.frontflix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "banco_de_usuarios.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "pessoas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_SENHA = "senha";
    private static final String TABLE_FAVORITES = "favoritos";
    private static final String COLUMN_USER_ID = "pessoa_id";
    private static final String COLUMN_MOVIE_ID = "filme_id";
    private static final String TABLE_MOVIES = "movies";

    private Context context;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, "
                + COLUMN_SENHA + " TEXT NOT NULL)";

        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " ("
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_MOVIE_ID + " INTEGER, "
                + "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_MOVIE_ID + "),"
                + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";

        String createMoviesTable = "CREATE TABLE " + TABLE_MOVIES + " ("
                + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, "
                + "title TEXT, "
                + "poster_path TEXT)";

        db.execSQL(createUserTable);
        db.execSQL(createFavoritesTable);
        db.execSQL(createMoviesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public void addPerson(String email, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_SENHA, senha);
        long result = db.insert(TABLE_USERS, null, values);
        if (result == -1) {
            Toast.makeText(context, "Erro ao inserir registro", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public boolean authenticateUser(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + "=? AND " + COLUMN_SENHA + "=?",
                new String[]{email, senha}, null, null, null);
        boolean authenticated = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return authenticated;
    }

    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID}, COLUMN_EMAIL + "=?",
                new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            cursor.close();
            return userId;
        }
        return -1;
    }

    public void addFavorite(int userId, int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_MOVIE_ID, movieId);
        long result = db.insert(TABLE_FAVORITES, null, values);
        if (result == -1) {
            Toast.makeText(context, "Erro ao adicionar favorito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Favorito adicionado com sucesso", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void removeFavorite(int userId, int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_USER_ID + "=? AND " + COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(movieId)});
        db.close();
    }

    public List<MovieItem> getFavorites(int userId) {
        List<MovieItem> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT f." + COLUMN_MOVIE_ID + ", m.title, m.poster_path FROM " + TABLE_FAVORITES + " f"
                + " JOIN " + TABLE_MOVIES + " m ON f." + COLUMN_MOVIE_ID + " = m.id"
                + " WHERE f." + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            int movieId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow("poster_path"));
            favorites.add(new MovieItem(movieId, title, posterPath, null, posterPath));
        }
        cursor.close();
        db.close();
        return favorites;
    }
}
