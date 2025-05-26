/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.mysql;

import gt.edu.miumg.programacion1.biblioteca.datasources.IUsuarioData;
import gt.edu.miumg.programacion1.biblioteca.dto.UsuarioConRol;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import java.sql.Connection;
import java.sql.Date;
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
public class UsuarioDataMySQL implements IUsuarioData {

    private final String url;
    private final String user;
    private final String password;

    public UsuarioDataMySQL(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public List<UsuarioConRol> getAllUsers() throws SQLException {
        List<UsuarioConRol> usuarios = new ArrayList<>();

        String sql = """
            SELECT usuario.id, name, email, fotografia, fechaRegistro, contrasena, rolId, salt, rol.nombreRol
            FROM usuario
            LEFT JOIN rol ON usuario.rolId = rol.id
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new UsuarioConRol(
                        rs.getShort("usuario.id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getShort("rolId"),
                        rs.getString("fotografia"),
                        rs.getDate("fechaRegistro").toLocalDate(),
                        rs.getString("salt"),
                        rs.getString("contrasena"),
                        rs.getString("rol.nombreRol")
                ));
            }
        }

        return usuarios;
    }

    @Override
    public Short registerUser(Usuario usuario) throws SQLException {
        String sql = """
            INSERT INTO usuario (name, email, fotografia, fechaRegistro, contrasena, rolId, salt)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getName());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getFotografia());
            stmt.setDate(4, Date.valueOf(usuario.getFechaRegistro()));
            stmt.setString(5, usuario.getContrasena());
            stmt.setShort(6, usuario.getRolId());
            stmt.setString(7, usuario.getSalt());
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
    public void updateUser(Usuario usuario) throws SQLException {
        String sql = """
            UPDATE usuario
            SET name=?, email=?, fotografia=?, fechaRegistro=?, contrasena=?, rolId=?, salt=?
            WHERE id=?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getName());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getFotografia());
            stmt.setDate(4, Date.valueOf(usuario.getFechaRegistro()));
            stmt.setString(5, usuario.getContrasena());
            stmt.setShort(6, usuario.getRolId());
            stmt.setString(7, usuario.getSalt());
            stmt.setShort(8, usuario.getId());
            stmt.executeUpdate();

        }
    }

    @Override
    public void removeUser(Short id) throws SQLException {
        String sql = """
            DELETE FROM usuario
            WHERE id = ?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setShort(1, id);
            stmt.executeUpdate();

        }
    }
}
