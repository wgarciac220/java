/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.vistas;

//import gt.edu.miumg.programacion1.biblioteca.util.LookAndFeel;
import gt.edu.miumg.programacion1.biblioteca.util.LookAndFeel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author wgarciac
 */
public class MenuForm extends JFrame {

    public JButton usuarioButton;
    public JButton libroButton;
    public JButton autorButton;
    public JButton prestarButton;
    public JButton devolverButton;
    public JPanel splashPanel;
    public JButton botonPrueba;

    public MenuForm() throws IOException {
        setTitle("Gestor de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 625);

        Font titleFont = new Font("Nunito", Font.BOLD, 36);
        Font regularFont = new Font("Nunito", Font.PLAIN, 14);

        // Menu bar
        JMenuBar barMenuBar = new JMenuBar();

        JMenu archiveMenu = new JMenu("Archivo");
        archiveMenu.setFont(regularFont);
        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.setFont(regularFont);
        exitItem.addActionListener(e -> System.exit(0));
        archiveMenu.add(exitItem);

        // Menú Ayuda
        JMenu helpMenu = new JMenu("Ayuda");
        helpMenu.setFont(regularFont);
        JMenuItem aboutItem = new JMenuItem("Acerca de...");
        aboutItem.setFont(regularFont);
        aboutItem.addActionListener(e -> {
            Font fuenteActual = UIManager.getFont("OptionPane.messageFont");

            // Asignar la fuente personalizada
            UIManager.put("OptionPane.messageFont", regularFont);
            UIManager.put("OptionPane.buttonFont", regularFont); // También cambia la fuente de los botones

            JOptionPane.showMessageDialog(this, "Sistema de Gestión de Biblioteca\nVersión 1.0\nAll Rights Reserved.", "Acerca de", JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);

        LookAndFeel lookAndFeel = new LookAndFeel(regularFont);

        barMenuBar.add(archiveMenu);
        barMenuBar.add(helpMenu);
        barMenuBar.add(lookAndFeel.crearMenuLookAndFeel());

        setJMenuBar(barMenuBar);

        // Panel de botones
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        buttonsPanel.setPreferredSize(new Dimension(190, 600));
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        usuarioButton = createButton("Usuarios", "/icons/usuarios.png", regularFont);
        buttonsPanel.add(usuarioButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        libroButton = createButton("Libros", "/icons/libro.png", regularFont);
        buttonsPanel.add(libroButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        autorButton = createButton("Autores", "/icons/autor.png", regularFont);
        buttonsPanel.add(autorButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        prestarButton = createButton("Prestar Libro", "/icons/prestar.png", regularFont);
        buttonsPanel.add(prestarButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        devolverButton = createButton("Devolver Libro", "/icons/devolver.png", regularFont);
        buttonsPanel.add(devolverButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Panel de informacion
        splashPanel = new JPanel(new BorderLayout());
        splashPanel.setPreferredSize(new Dimension(610, 600));
        // splashPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        splashPanel.add(new JButton("Test"));

        add(buttonsPanel, BorderLayout.LINE_START);
        add(splashPanel, BorderLayout.LINE_END);

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String text, String iconPath, Font font) throws IOException {
        JButton button = new JButton(text);

        button.setFont(font);
        button.setMaximumSize(new Dimension(150, 80));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {

            button.setIcon(new ImageIcon(ImageIO.read(getClass().getResource(iconPath))));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
        } catch (IOException e) {
            System.err.println("No se pudo encontrar la ruta indicada: " + iconPath);
        } catch (Exception e) {
            System.err.println("Ocurrio un error al asignar icono: " + e);
        }

        return button;
    }

    public void mostrarPanelEnSplash(JPanel nuevoPanel) {
        splashPanel.removeAll();
        splashPanel.add(nuevoPanel, BorderLayout.CENTER);
        splashPanel.revalidate();
        splashPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MenuForm().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(MenuForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
