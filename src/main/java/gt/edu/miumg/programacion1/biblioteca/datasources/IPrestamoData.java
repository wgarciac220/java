/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import gt.edu.miumg.programacion1.biblioteca.dto.HistorialConLibro;
import gt.edu.miumg.programacion1.biblioteca.modelos.HistorialPrestamo;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author willc
 */
public interface IPrestamoData {

    List<HistorialConLibro> getAllBorrowings() throws SQLException;

    Short registerBorrowing(HistorialPrestamo prestamo) throws SQLException;

    void updateBorrowing(HistorialPrestamo prestamo) throws SQLException;

    void removeBorrowing(Short id) throws SQLException;

    boolean hasActiveBorrowing(short usuarioId, short libroId) throws SQLException;

}
