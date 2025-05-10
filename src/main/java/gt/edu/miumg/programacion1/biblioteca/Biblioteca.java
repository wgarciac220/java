/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package gt.edu.miumg.programacion1.biblioteca;

import gt.edu.miumg.programacion1.biblioteca.controladores.LoginController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author wgarciac
 */
public class Biblioteca {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");

            SwingUtilities.invokeLater(() -> {
                LoginController controller;
                // MenuController controller;
                controller = new LoginController();
                controller.Mostrar();
            });
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Biblioteca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
