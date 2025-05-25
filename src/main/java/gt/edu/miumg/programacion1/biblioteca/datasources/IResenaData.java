/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import gt.edu.miumg.programacion1.biblioteca.modelos.Resena;
import java.util.List;

/**
 *
 * @author willc
 */
public interface IResenaData {

    List<Resena> getAllReviews();

    void registerReview(Resena resena);

    void updateReview(Resena resena);

    void removeReview(Short id);

}
