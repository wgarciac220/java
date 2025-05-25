/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.mysql;

import gt.edu.miumg.programacion1.biblioteca.datasources.IResenaData;
import gt.edu.miumg.programacion1.biblioteca.modelos.Resena;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author willc
 */
public class ResenaDataMySQL implements IResenaData {

    private final String url;
    private final String user;
    private final String password;

    public ResenaDataMySQL(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public List<Resena> getAllReviews() {
        List<Resena> resenas = new ArrayList<>();

        String sql = """
            SELECT id, usuarioId, libroId, resena, rating
            FROM resena
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resenas.add(new Resena(
                        rs.getShort("id"),
                        rs.getShort("usuarioId"),
                        rs.getShort("libroId"),
                        rs.getString("resena"),
                        rs.getByte("rating")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resenas;
    }

    @Override
    public void registerReview(Resena resena) {
        String sql = """
            INSERT INTO resena (usuarioId, libroId, resena, rating)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, resena.getUsuarioId());
            stmt.setShort(2, resena.getLibroId());
            stmt.setString(3, resena.getResena());
            stmt.setByte(4, resena.getRating());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateReview(Resena resena) {
        String sql = """
            UPDATE resena
            SET usuarioId=?, libroId=?, resena=?, rating=?
            WHERE id=?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, resena.getUsuarioId());
            stmt.setShort(2, resena.getLibroId());
            stmt.setString(3, resena.getResena());
            stmt.setByte(4, resena.getRating());
            stmt.setShort(5, resena.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeReview(Short id) {
        String sql = """
            DELETE FROM resena
            WHERE id = ?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
