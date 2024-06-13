package com.example.frontflix;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/banco_de_usuarios"; //aqui muda host e nome do database
    private static final String USER = "root"; // bota o user aqui
    private static final String PASSWORD = "";// bota a senha aqui

    private Context context;

    public DatabaseManager(Context context) {
        this.context = context;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Toast.makeText(context, "Conectado ao banco de dados", Toast.LENGTH_SHORT).show();
            return conn;
        } catch (SQLException e) {
            Toast.makeText(context, "Erro ao conectar ao banco de dados: " + e.getMessage(), Toast.LENGTH_LONG).show();
            throw e;
        }
    }

    public void addPerson(String email, String senha) {
        String query = "INSERT INTO pessoas (email, senha) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateUser(String email, String senha) {
        String query = "SELECT * FROM pessoas WHERE email = ? AND senha = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeFavorite(int pessoaId, int filmeId) {
        String query = "DELETE FROM favoritos WHERE pessoa_id = ? AND filme_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, pessoaId);
            stmt.setInt(2, filmeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getFavorites(int pessoaId) {
        List<Integer> favorites = new ArrayList<>();
        String query = "SELECT filme_id FROM favoritos WHERE pessoa_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, pessoaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    favorites.add(rs.getInt("filme_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }
}
