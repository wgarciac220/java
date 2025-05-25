/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.mysql;

import gt.edu.miumg.programacion1.biblioteca.datasources.IPrestamoData;
import gt.edu.miumg.programacion1.biblioteca.modelos.HistorialPrestamo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public List<HistorialPrestamo> getAllBorrowings() {
        List<HistorialPrestamo> historial = new ArrayList<>();

        String sql = """
            SELECT id, usuarioId, libroId, fechaPrestamo, estado, fechaDevolucion, multa
            FROM historialprestamos
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                historial.add(new HistorialPrestamo(
                        rs.getShort("id"),
                        rs.getShort("usuarioId"),
                        rs.getShort("libroId"),
                        rs.getTimestamp("fechaPrestamo").toLocalDateTime(),
                        rs.getString("estado"),
                        rs.getTimestamp("fechaDevolucion").toLocalDateTime(),
                        rs.getBigDecimal("multa")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historial;
    }

    @Override
    public void registerBorrowing(HistorialPrestamo prestamo) {
        String sql = """
            INSERT INTO historialprestamos (usuarioId, libroId, fechaPrestamo, estado, fechaDevolucion, multa)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, prestamo.getUsuarioId());
            stmt.setShort(2, prestamo.getLibroId());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.setString(4, prestamo.getEstado());
            stmt.setTimestamp(5, Timestamp.valueOf(prestamo.getFechaDevolucion()));
            stmt.setBigDecimal(6, prestamo.getMulta());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBorrowing(HistorialPrestamo prestamo) {
        String sql = """
            UPDATE historialprestamos
            SET usuarioId=?, libroId=?, fechaPrestamo=?, estado=?, fechaDevolucion=?, multa=?
            WHERE id=?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, prestamo.getUsuarioId());
            stmt.setShort(2, prestamo.getLibroId());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.setString(4, prestamo.getEstado());
            stmt.setTimestamp(5, Timestamp.valueOf(prestamo.getFechaDevolucion()));
            stmt.setBigDecimal(6, prestamo.getMulta());
            stmt.setShort(7, prestamo.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeBorrowing(Short id) {
        String sql = """
            DELETE FROM historialprestamos
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
