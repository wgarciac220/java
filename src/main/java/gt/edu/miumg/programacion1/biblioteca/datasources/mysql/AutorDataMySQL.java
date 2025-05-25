/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.mysql;

import gt.edu.miumg.programacion1.biblioteca.datasources.IAutorData;
import gt.edu.miumg.programacion1.biblioteca.modelos.Autor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author willc
 */
public class AutorDataMySQL implements IAutorData {

    private final String url;
    private final String user;
    private final String password;

    public AutorDataMySQL(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public List<Autor> getAllAuthors() throws SQLException {
        List<Autor> autores = new ArrayList<>();

        String sql = """
            SELECT id, nombre, biografia, paisOrigen, fotografia
            FROM autor
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                autores.add(new Autor(
                        rs.getShort("id"),
                        rs.getString("nombre"),
                        rs.getString("biografia"),
                        rs.getString("paisOrigen"),
                        rs.getString("fotografia")
                ));
            }

        }

        return autores;
    }

    @Override
    public Short registerAuthor(Autor autor) throws SQLException {
        String sql = """
            INSERT INTO autor (nombre, paisOrigen, fotografia, biografia)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getPaisOrigen());
            stmt.setString(3, autor.getFotografia());
            stmt.setString(4, autor.getBiografia());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getShort(1); // ID generado
                }
            }
        }

        return null;
    }

    @Override
    public void updateAuthor(Autor autor) throws SQLException {
        String sql = """
            UPDATE autor
            SET nombre=?, paisOrigen=?, fotografia=?, biografia=?
            WHERE id=?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getPaisOrigen());
            stmt.setString(3, autor.getFotografia());
            stmt.setString(4, autor.getBiografia());
            stmt.setShort(5, autor.getId());
            stmt.executeUpdate();

        }
    }

    @Override
    public void removeAuthor(Short id) throws SQLException {
        String sql = """
            DELETE FROM autor
            WHERE id = ?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, id);
            stmt.executeUpdate();

        }
    }

}
