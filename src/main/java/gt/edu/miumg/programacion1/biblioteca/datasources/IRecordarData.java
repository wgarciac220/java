/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import gt.edu.miumg.programacion1.biblioteca.modelos.RecordarUsuario;
import java.sql.SQLException;

/**
 *
 * @author willc
 */
public interface IRecordarData {

    RecordarUsuario getRememberedUser() throws SQLException;

    void registerRememberedUser(RecordarUsuario usuario) throws SQLException;

    void removeRememberedUser() throws SQLException;

}
