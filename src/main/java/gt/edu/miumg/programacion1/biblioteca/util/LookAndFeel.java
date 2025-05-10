/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.util;

import java.awt.Font;
import java.awt.Window;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author wgarciac
 */
public class LookAndFeel extends JFrame {

    private Font font;

    public LookAndFeel(Font font) {
        this.font = font;

    }

    private void aplicarLookAndFeel(String lookAndFeel) {
        try {
            switch (lookAndFeel) {
                case "Metal":
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                case "Nimbus":
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case "CDE/Motif":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
                case "Windows":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
                case "Windows Classic":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    break;
                case "GTK+":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                    break;
                case "Aqua":
                    UIManager.setLookAndFeel("apple.laf.AquaLookAndFeel");
                    break;
                case "FlatLaf Light":
                    UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
                    break;
                case "FlatLaf Dark":
                    UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
                    break;
                case "System":
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                default:
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JMenu crearMenuLookAndFeel() {
        JMenu menuLookAndFeel = new JMenu("Apariencia");
        menuLookAndFeel.setFont(this.font);

        // Lista de Look and Feels disponibles
        String[] lookAndFeels = {
            "Metal",
            "Nimbus",
            "CDE/Motif",
            "Windows",
            "Windows Classic",
            "GTK+",
            "Aqua (solo Mac)",
            "FlatLaf Light",
            "FlatLaf Dark",
            "System"
        };

        ButtonGroup group = new ButtonGroup();

        for (String laf : lookAndFeels) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(laf);
            item.setFont(this.font);
            item.addActionListener(e -> cambiarLookAndFeel(laf));
            group.add(item);
            menuLookAndFeel.add(item);

            // Seleccionar Nimbus por defecto
            if (laf.equals("Nimbus")) {
                item.setSelected(true);
            }
        }

        // Añadir al menú principal
        //getJMenuBar().add(menuLookAndFeel);
        return menuLookAndFeel;
    }

    private void cambiarLookAndFeel(String lookAndFeel) {
        try {
            switch (lookAndFeel) {
                case "Metal":
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                case "Nimbus":
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case "CDE/Motif":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
                case "Windows":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
                case "Windows Classic":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    break;
                case "GTK+":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                    break;
                case "Aqua (solo Mac)":
                    UIManager.setLookAndFeel("apple.laf.AquaLookAndFeel");
                    break;
                case "FlatLaf Light":
                    UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
                    break;
                case "FlatLaf Dark":
                    UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
                    break;
                case "System":
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                default:
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }

            // Actualizar la UI de todas las ventanas abiertas
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cambiar la apariencia: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
