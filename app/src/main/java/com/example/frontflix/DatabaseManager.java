package com.example.frontflix;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://your_database_host:3306/your_database_name"; //aqui muda host e nome do database
    private static final String USER = "your_database_user"; // bota o user aqui
    private static final String PASSWORD = "your_database_password";// bota a senha aqui

    public DatabaseManager() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
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

    public void addFavorite(int pessoaId, int filmeId) {
        String query = "INSERT INTO favoritos (pessoa_id, filme_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, pessoaId);
            stmt.setInt(2, filmeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
