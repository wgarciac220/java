/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import gt.edu.miumg.programacion1.biblioteca.dto.LibroConAutor;
import gt.edu.miumg.programacion1.biblioteca.modelos.Libro;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author willc
 */
public interface ILibroData {

    List<LibroConAutor> getAllBooks() throws SQLException;

    Short registerBook(Libro libro) throws SQLException;

    void updateBook(Libro libro) throws SQLException;

    void removeBook(Short id) throws SQLException;

}
