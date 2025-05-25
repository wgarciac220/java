/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import gt.edu.miumg.programacion1.biblioteca.modelos.HistorialPrestamo;
import java.util.List;

/**
 *
 * @author willc
 */
public interface IPrestamoData {

    List<HistorialPrestamo> getAllBorrowings();

    void registerBorrowing(HistorialPrestamo prestamo);

    void updateBorrowing(HistorialPrestamo prestamo);

    void removeBorrowing(Short id);

}
