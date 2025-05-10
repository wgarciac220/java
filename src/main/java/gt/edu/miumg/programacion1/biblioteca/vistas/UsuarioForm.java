/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.vistas;

import gt.edu.miumg.programacion1.biblioteca.modelos.Rol;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import gt.edu.miumg.programacion1.biblioteca.util.ImageRenderer;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author wgarciac
 */
public class UsuarioForm extends JPanel {

    public JTextField idField;
    public JTextField nameField;
    public JTextField emailField;
    public JComboBox<String> puestoComboBox;
    public JTextField fotografiaField;
    public JTextField passwordField;
    public JButton nuevoBtn;
    public JButton guardarBtn;
    public JButton eliminarBtn;
    public JTable tablaUsuarios;
    public List<Rol> listaRoles;
    public List<Usuario> listaUsuarios;
    public DefaultTableModel modeloTabla;
    public JTextField filtroField;
    public JButton aplicarFiltroBtn;
    public TableRowSorter<DefaultTableModel> tablaSorter;

    public UsuarioForm(List<Rol> listaRoles) {
        this.listaRoles = listaRoles;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(610, 600));

        Font regularFont = new Font("Nunito", Font.PLAIN, 14);
        Font boldFont = new Font("Nunito", Font.BOLD, 14);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 3, 20);
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setPreferredSize(new Dimension(610, 225));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 10), // Espacio alrededor
                BorderFactory.createTitledBorder("Administrador de Usuarios") // Borde interno
        ));

        // Fila 1: ID
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("ID Usuario", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        idField = createField(10, regularFont);
        idField.setEnabled(false);
        detailsPanel.add(idField, gbc);

        // Fila 2: Nombre
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Nombre", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nameField = createField(10, regularFont);
        detailsPanel.add(nameField, gbc);

        // Fila 3: Email
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Email", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        emailField = createField(10, regularFont);
        detailsPanel.add(emailField, gbc);

        // Fila 4: Fotografia
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Fotografia", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        fotografiaField = createField(10, regularFont);
        fotografiaField.setEditable(false); // edicion solo con FileChooser
        detailsPanel.add(fotografiaField, gbc);

        // Fila 5: Contraseña
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Contraseña", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        passwordField = new JPasswordField("", 10);
        passwordField.setFont(regularFont);
        detailsPanel.add(passwordField, gbc);

        // Fila 6: Rol
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Rol", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        puestoComboBox = new JComboBox<>();
        detailsPanel.add(puestoComboBox, gbc);

        // Panel de botones
        JPanel botonesPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.insets = new Insets(5, 15, 0, 15);
        gbcBotones.gridwidth = 1;
        gbcBotones.fill = GridBagConstraints.HORIZONTAL;
        gbcBotones.weightx = 1.0;

        gbcBotones.gridx = 0;
        nuevoBtn = createButton("Nuevo", regularFont);
        botonesPanel.add(nuevoBtn, gbcBotones);
        gbcBotones.gridx = 1;
        guardarBtn = createButton("Guardar", regularFont);
        botonesPanel.add(guardarBtn, gbcBotones);
        gbcBotones.gridx = 2;
        eliminarBtn = createButton("Eliminar", regularFont);
        botonesPanel.add(eliminarBtn, gbcBotones);

        // Tabla
        String[] columnas = {"ID", "Nombre", "e-mail", "Fotografia", "Rol"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evitar edición directa
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaSorter = new TableRowSorter<>(modeloTabla);
        tablaUsuarios.setRowSorter(tablaSorter);
        tablaUsuarios.setRowHeight(65); // para que la imagen quepa bien
        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(150);  // Nombre
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(180);  // e-mail
        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(100);  // Fotografía
        tablaUsuarios.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());
        tablaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(120);  // Rol

        JScrollPane tablaScroll = new JScrollPane(tablaUsuarios);
        tablaScroll.setPreferredSize(new Dimension(610, 250));

        JPanel tablaWrapper = new JPanel(new BorderLayout());
        tablaWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10)); // top, left, bottom, right
        tablaWrapper.add(tablaScroll, BorderLayout.CENTER);

        // Panel de filtro
        filtroField = createField(20, regularFont);
        aplicarFiltroBtn = createButton("Filtrar", regularFont);

        JPanel filtroPanel = new JPanel(new BorderLayout());
        filtroPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        filtroPanel.add(filtroField, BorderLayout.CENTER);
        filtroPanel.add(aplicarFiltroBtn, BorderLayout.EAST);
        tablaWrapper.add(filtroPanel, BorderLayout.NORTH);

        add(detailsPanel, BorderLayout.PAGE_START);
        add(botonesPanel, BorderLayout.CENTER);
        add(tablaWrapper, BorderLayout.PAGE_END);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(800, 625);
            List<Rol> roles = new ArrayList<>();
//            roles.add(new Rol((short) 1, "Rol 1"));
//            roles.add(new Rol((short) 1, "Rol 2"));
//            roles.add(new Rol((short) 1, "Rol 3"));

            UsuarioForm form = new UsuarioForm(roles);
            form.setVisible(true);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(form);
            frame.setVisible(true);
        });
    }
}
