/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.mysql;

import gt.edu.miumg.programacion1.biblioteca.datasources.IRolData;
import gt.edu.miumg.programacion1.biblioteca.modelos.Rol;
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
public class RolDataMySQL implements IRolData {

    private final String url;
    private final String user;
    private final String password;

    public RolDataMySQL(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public List<Rol> getAllRoles() {
        List<Rol> roles = new ArrayList<>();

        String sql = """
            SELECT id, nombreRol
            FROM rol
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(new Rol(
                        rs.getShort("id"),
                        rs.getString("nombreRol")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    @Override
    public void registerRole(Rol rol) {
        String sql = """
            INSERT INTO rol (nombreRol)
            VALUES (?)
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rol.getNombreRol());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRole(Rol rol) {
        String sql = """
            UPDATE rol
            SET nombreRol=?
            WHERE id=?
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rol.getNombreRol());
            stmt.setShort(5, rol.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeRole(Short id) {
        String sql = """
            DELETE FROM rol
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
