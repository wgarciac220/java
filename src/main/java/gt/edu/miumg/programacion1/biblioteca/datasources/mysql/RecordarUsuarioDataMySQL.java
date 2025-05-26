/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.mysql;

import gt.edu.miumg.programacion1.biblioteca.datasources.IRecordarData;
import gt.edu.miumg.programacion1.biblioteca.modelos.RecordarUsuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author willc
 */
public class RecordarUsuarioDataMySQL implements IRecordarData {

    private final String url;
    private final String user;
    private final String password;

    public RecordarUsuarioDataMySQL(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public RecordarUsuario getRememberedUser() throws SQLException {
        RecordarUsuario usuario = null;

        String sql = """
            SELECT email, recordar
            FROM recordarusuario
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuario = new RecordarUsuario(
                        rs.getString("email"),
                        rs.getBoolean("recordar")
                );
            }

        }

        return usuario;
    }

    @Override
    public void registerRememberedUser(RecordarUsuario usuario) throws SQLException {
        String deleteSql = """
            DELETE
            FROM recordarusuario
        """;

        String sql = """
            INSERT INTO recordarusuario (email, recordar)
            VALUES (?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                insertStmt.setString(1, usuario.getEmail());
                insertStmt.setBoolean(2, usuario.isRecordar());
                insertStmt.executeUpdate();
            }

        }
    }

    @Override
    public void removeRememberedUser() throws SQLException {
        String sql = """
            DELETE
            FROM recordarusuario
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();

        }
    }
}
