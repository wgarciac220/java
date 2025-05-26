/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.mysql;

import gt.edu.miumg.programacion1.biblioteca.datasources.IPrestamoData;
import gt.edu.miumg.programacion1.biblioteca.dto.HistorialConLibro;
import gt.edu.miumg.programacion1.biblioteca.modelos.HistorialPrestamo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author willc
 */
public class PrestamoDataMySQL implements IPrestamoData {

    private final String url;
    private final String user;
    private final String password;

    public PrestamoDataMySQL(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public List<HistorialConLibro> getAllBorrowings() throws SQLException {
        List<HistorialConLibro> historial = new ArrayList<>();

        String sql = """
            SELECT historialprestamos.id, usuarioId, libroId, fechaPrestamo, historialprestamos.estado, fechaDevolucion, multa, libro.titulo, resena, historialprestamos.rating
            FROM historialprestamos
            LEFT JOIN libro ON historialprestamos.libroId = libro.id
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                historial.add(new HistorialConLibro(
                        rs.getShort("historialprestamos.id"),
                        rs.getShort("usuarioId"),
                        rs.getShort("libroId"),
                        rs.getTimestamp("fechaPrestamo").toLocalDateTime(),
                        rs.getString("historialprestamos.estado"),
                        rs.getTimestamp("fechaDevolucion").toLocalDateTime(),
                        rs.getBigDecimal("multa"),
                        rs.getString("resena"),
                        rs.getByte("historialprestamos.rating"),
                        rs.getString("libro.titulo")
                ));
            }
        }

        return historial;
    }

    @Override
    public Short registerBorrowing(HistorialPrestamo prestamo) throws SQLException {
        String sql = """
            INSERT INTO historialprestamos (usuarioId, libroId, fechaPrestamo, estado, fechaDevolucion, multa, resena, rating)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setShort(1, prestamo.getUsuarioId());
            stmt.setShort(2, prestamo.getLibroId());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.setString(4, prestamo.getEstado());
            stmt.setTimestamp(5, Timestamp.valueOf(prestamo.getFechaDevolucion()));
            stmt.setBigDecimal(6, prestamo.getMulta());
            stmt.setString(7, prestamo.getResena());
            stmt.setByte(8, prestamo.getRating());
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
    public void updateBorrowing(HistorialPrestamo prestamo) throws SQLException {
        String sql = """
            UPDATE historialprestamos
            SET usuarioId=?, libroId=?, fechaPrestamo=?, estado=?, fechaDevolucion=?, multa=?, resena=?, rating=?
            WHERE id=?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, prestamo.getUsuarioId());
            stmt.setShort(2, prestamo.getLibroId());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.setString(4, prestamo.getEstado());
            stmt.setTimestamp(5, Timestamp.valueOf(prestamo.getFechaDevolucion()));
            stmt.setBigDecimal(6, prestamo.getMulta());
            stmt.setString(7, prestamo.getResena());
            stmt.setByte(8, prestamo.getRating());
            stmt.setShort(9, prestamo.getId());
            stmt.executeUpdate();

        }
    }

    @Override
    public void removeBorrowing(Short id) throws SQLException {
        String sql = """
            DELETE FROM historialprestamos
            WHERE id = ?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean hasActiveBorrowing(short usuarioId, short libroId) throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM historialprestamos
            WHERE usuarioId = ? AND libroId = ? AND LOWER(estado) = 'prestado'
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, usuarioId);
            stmt.setShort(2, libroId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }
}
