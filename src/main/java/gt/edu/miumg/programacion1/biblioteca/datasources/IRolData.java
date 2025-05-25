/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import gt.edu.miumg.programacion1.biblioteca.modelos.Rol;
import java.util.List;

/**
 *
 * @author willc
 */
public interface IRolData {

    List<Rol> getAllRoles();

    void registerRole(Rol rol);

    void updateRole(Rol rol);

    void removeRole(Short id);

}
