/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.mysql;

import gt.edu.miumg.programacion1.biblioteca.datasources.ILibroData;
import gt.edu.miumg.programacion1.biblioteca.dto.LibroConAutor;
import gt.edu.miumg.programacion1.biblioteca.modelos.Libro;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author willc
 */
public class LibroDataMySQL implements ILibroData {

    private final String url;
    private final String user;
    private final String password;

    public LibroDataMySQL(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public List<LibroConAutor> getAllBooks() throws SQLException {
        List<LibroConAutor> books = new ArrayList<>();

        String sql = """
            SELECT libro.id, titulo, isbn, genero, cantidad, portada, anoPublicacion, editorial, idioma, rating, estado, autorId, autor.nombre
            FROM libro
            LEFT JOIN autor ON libro.autorId=autor.id
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new LibroConAutor(
                        rs.getShort("libro.id"),
                        rs.getString("titulo"),
                        rs.getString("isbn"),
                        rs.getString("genero"),
                        rs.getShort("cantidad"),
                        rs.getString("portada"),
                        Year.of(rs.getInt("anoPublicacion")),
                        rs.getString("editorial"),
                        rs.getString("idioma"),
                        rs.getFloat("rating"),
                        rs.getShort("autorId"),
                        rs.getString("estado"),
                        rs.getString("autor.nombre")
                ));
            }
        }

        return books;
    }

    @Override
    public Short registerBook(Libro libro) throws SQLException {
        String sql = """
            INSERT INTO libro (titulo, isbn, genero, cantidad, portada, anoPublicacion, editorial, idioma, rating, autorId, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getIsbn());
            stmt.setString(3, libro.getGenero());
            stmt.setShort(4, libro.getCantidad());
            stmt.setString(5, libro.getPortada());
            stmt.setString(6, libro.getAnoPublicacion().toString());
            stmt.setString(7, libro.getEditorial());
            stmt.setString(8, libro.getIdioma());
            stmt.setFloat(9, libro.getRating());
            stmt.setShort(10, libro.getAutorId());
            stmt.setString(11, libro.getEstado());
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
    public void updateBook(Libro libro) throws SQLException {
        String sql = """
            UPDATE libro
            SET titulo=?, isbn=?, genero=?, cantidad=?, portada=?, anoPublicacion=?, editorial=?, idioma=?, rating=?, estado=?, autorId=?
            WHERE id=?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getIsbn());
            stmt.setString(3, libro.getGenero());
            stmt.setShort(4, libro.getCantidad());
            stmt.setString(5, libro.getPortada());
            stmt.setString(6, libro.getAnoPublicacion().toString());
            stmt.setString(7, libro.getEditorial());
            stmt.setString(8, libro.getIdioma());
            stmt.setFloat(9, libro.getRating());
            stmt.setString(10, libro.getEstado());
            stmt.setShort(11, libro.getAutorId());
            stmt.setShort(12, libro.getId());
            stmt.executeUpdate();

        }
    }

    @Override
    public void removeBook(Short id) throws SQLException {
        String sql = """
            DELETE FROM libro
            WHERE id = ?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, id);
            stmt.executeUpdate();

        }
    }
}
