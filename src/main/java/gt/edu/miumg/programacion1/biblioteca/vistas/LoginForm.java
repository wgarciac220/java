/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.vistas;

import gt.edu.miumg.programacion1.biblioteca.util.TabOrderPolicy;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author wgarciac
 */
public class LoginForm extends javax.swing.JFrame {

    public JButton cancelButton;
    public JButton submitButton;
    public JTextField usernameField;
    public JPasswordField passwordField;
    public JCheckBox recordarUsuario;

    public LoginForm() {
        setTitle("Gestor de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(360, 500);

        Font titleFont = new Font("Nunito", Font.BOLD, 36);
        Font regularFont = new Font("Nunito", Font.PLAIN, 14);

        // Panel superior
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(360, 110));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Login");
        headerLabel.setFont(titleFont);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(headerLabel);
        headerPanel.add(Box.createVerticalGlue());

        // Panel medio
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 1, 10, 5));
        fieldsPanel.setPreferredSize(new Dimension(360, 280));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel usernameLabel = new JLabel("Correo electronico");
        usernameLabel.setFont(regularFont);
        fieldsPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(regularFont);
        fieldsPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Contraseña");
        passwordLabel.setFont(regularFont);
        fieldsPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(regularFont);
        fieldsPanel.add(passwordField);

        recordarUsuario = new JCheckBox("Recordar usuario");
        recordarUsuario.setFont(regularFont);
        fieldsPanel.add(recordarUsuario);

        // Panel inferior
        JPanel submitPanel = new JPanel(new GridBagLayout());
        submitPanel.setPreferredSize(new Dimension(360, 110));

        // Panel para los botones
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(regularFont);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        buttonsPanel.add(cancelButton);

        submitButton = new JButton("OK");
        submitButton.setFont(regularFont);
        submitButton.setPreferredSize(new Dimension(100, 40));
        buttonsPanel.add(submitButton);

        submitPanel.add(buttonsPanel);

        // Agregar todo al frame
        add(headerPanel, BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(submitPanel, BorderLayout.SOUTH);

        setFocusTraversalPolicy(new TabOrderPolicy(List.of(
                usernameField,
                passwordField,
                submitButton,
                recordarUsuario,
                cancelButton
        )));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
