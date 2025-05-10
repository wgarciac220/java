/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.vistas;

import gt.edu.miumg.programacion1.biblioteca.modelos.Autor;
import gt.edu.miumg.programacion1.biblioteca.modelos.Rol;
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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author wgarciac
 */
public class AutorForm extends JPanel {

    public JTextField idField;
    public JTextField nombreField;
    public JTextField paisField;
    public JTextField fotografiaField;
    public JTextArea biografiaField;
    public JButton nuevoBtn;
    public JButton guardarBtn;
    public JButton eliminarBtn;
    public JTable tablaAutores;
    public DefaultTableModel modeloTabla;
    public List<Autor> listaAutores;
    public JTextField filtroField;
    public JButton aplicarFiltroBtn;
    public TableRowSorter<DefaultTableModel> tablaSorter;

    public AutorForm() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(610, 600));

        Font regularFont = new Font("Nunito", Font.PLAIN, 14);
        Font boldFont = new Font("Nunito", Font.BOLD, 14);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 3, 20);
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setPreferredSize(new Dimension(610, 220));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 10), // Espacio alrededor
                BorderFactory.createTitledBorder("Administrador de Autores") // Borde interno
        ));

        // Fila 1: ID
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("ID Autor", boldFont), gbc);
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
        nombreField = createField(10, regularFont);
        detailsPanel.add(nombreField, gbc);

        // Fila 3: Pais Origen
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Pais Origen", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        paisField = createField(10, regularFont);
        detailsPanel.add(paisField, gbc);

        // Fila 4: Fotografia
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Fotografia", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        fotografiaField = createField(10, regularFont);
        fotografiaField.setEditable(false); // edicion solo con FileChooser
        detailsPanel.add(fotografiaField, gbc);

        // Fila 5: Biografia
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Biografia", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weighty = 1.0;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        biografiaField = createTextArea(regularFont);
        JScrollPane scrollPane = new JScrollPane(biografiaField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailsPanel.add(scrollPane, gbc);

        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new GridBagLayout()); // centrar botones

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
        String[] columnas = {"ID", "Nombre", "Pais", "Fotografia", "Biografia"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evitar edición directa
            }
        };

        tablaAutores = new JTable(modeloTabla);
        tablaSorter = new TableRowSorter<>(modeloTabla);
        tablaAutores.setRowSorter(tablaSorter);
        tablaAutores.setRowHeight(65); // para que la imagen quepa bien
        tablaAutores.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tablaAutores.getColumnModel().getColumn(1).setPreferredWidth(150);  // Nombre
        tablaAutores.getColumnModel().getColumn(2).setPreferredWidth(180);  // e-mail
        tablaAutores.getColumnModel().getColumn(3).setPreferredWidth(100);  // Fotografía
        tablaAutores.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());
        tablaAutores.getColumnModel().getColumn(4).setPreferredWidth(120);  // Rol

        JScrollPane tablaScroll = new JScrollPane(tablaAutores);
        tablaScroll.setPreferredSize(new Dimension(610, 250));

        JPanel tablaWrapper = new JPanel(new BorderLayout());
        tablaWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10)); // top, left, bottom, right
        tablaWrapper.add(tablaScroll, BorderLayout.CENTER);

        // Panel superior para filtro
        JPanel filtroPanel = new JPanel(new BorderLayout());
        filtroField = createField(20, regularFont);
        aplicarFiltroBtn = createButton("Filtrar", regularFont);

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
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            List<Rol> roles = new ArrayList<>();
//            roles.add(new Rol((short) 1, "Rol 1"));
//            roles.add(new Rol((short) 1, "Rol 2"));
//            roles.add(new Rol((short) 1, "Rol 3"));

            AutorForm form = new AutorForm();
            form.setVisible(true);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(form);
            frame.setVisible(true);
        });
    }
}
