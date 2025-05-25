/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import gt.edu.miumg.programacion1.biblioteca.modelos.Autor;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author willc
 */
public interface IAutorData {

    List<Autor> getAllAuthors() throws SQLException;

    Short registerAuthor(Autor autor) throws SQLException;

    void updateAuthor(Autor autor) throws SQLException;

    void removeAuthor(Short id) throws SQLException;
}
