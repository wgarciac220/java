/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import gt.edu.miumg.programacion1.biblioteca.dto.UsuarioConRol;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author willc
 */
public interface IUsuarioData {

    List<UsuarioConRol> getAllUsers() throws SQLException;

    Short registerUser(Usuario usuario) throws SQLException;

    void updateUser(Usuario usuario) throws SQLException;

    void removeUser(Short id) throws SQLException;

}
