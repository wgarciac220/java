/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import java.util.List;

/**
 *
 * @author willc
 */
public interface IUsuarioData {

    List<Usuario> getAllUsers();

    void registerUser(Usuario usuario);

    void updateUser(Usuario usuario);

    void removeUser(Short id);

}
