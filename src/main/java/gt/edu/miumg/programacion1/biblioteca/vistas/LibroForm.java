/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.vistas;

import gt.edu.miumg.programacion1.biblioteca.modelos.Rol;
import gt.edu.miumg.programacion1.biblioteca.util.ImageRenderer;
import gt.edu.miumg.programacion1.biblioteca.util.StarRatingRenderer;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.createButton;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.createField;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.createLabel;
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
public class LibroForm extends JPanel {

    public JTextField idField;
    public JTextField isbnField;
    public JTextField tituloField;
    public JTextField portadaField;
    public JTextField generoField;
    public JTextField yearField;
    public JTextField editorialField;
    public JTextField idiomaField;
    public JTextField cantidadField;
    public JComboBox<String> estadoCombobox;
    public JTextField ratingField;
    public JComboBox<String> autorCombobox;
    public JButton nuevoBtn;
    public JButton guardarBtn;
    public JButton eliminarBtn;
    public JTable tablaLibros;
    public DefaultTableModel modeloTabla;
    public JTextField filtroField;
    public JButton aplicarFiltroBtn;
    public TableRowSorter<DefaultTableModel> tablaSorter;

    public LibroForm() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(610, 600));

        Font regularFont = new Font("Nunito", Font.PLAIN, 14);
        Font boldFont = new Font("Nunito", Font.BOLD, 14);

        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.insets = new Insets(2, 10, 3, 5);
        labelGbc.anchor = GridBagConstraints.WEST;
        labelGbc.fill = GridBagConstraints.NONE;
        labelGbc.weightx = 0;
        labelGbc.gridwidth = 1;

        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.insets = new Insets(2, 0, 3, 20);
        fieldGbc.anchor = GridBagConstraints.WEST;
        fieldGbc.fill = GridBagConstraints.HORIZONTAL;
        fieldGbc.weightx = 1;
        fieldGbc.gridwidth = 1;

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setPreferredSize(new Dimension(610, 275));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 10), // Espacio alrededor
                BorderFactory.createTitledBorder("Administrador de Autores") // Borde interno
        ));

        // ID
        labelGbc.gridy = fieldGbc.gridy = 0;
        labelGbc.gridx = 0;
        fieldGbc.gridwidth = 1;
        detailsPanel.add(createLabel("ID Libro", boldFont), labelGbc);
        fieldGbc.gridx = 1;
        idField = createField(10, regularFont);
        idField.setEnabled(false);
        detailsPanel.add(idField, fieldGbc);

        // Título
        labelGbc.gridy = fieldGbc.gridy = 1;
        labelGbc.gridx = 0;
        detailsPanel.add(createLabel("Título", boldFont), labelGbc);
        fieldGbc.gridx = 1;
        fieldGbc.gridwidth = 3; // 3 celdas
        tituloField = createField(25, regularFont);
        detailsPanel.add(tituloField, fieldGbc);

        // Autor
        labelGbc.gridy = fieldGbc.gridy = 2;
        labelGbc.gridx = 0;
        detailsPanel.add(createLabel("Autor", boldFont), labelGbc);
        fieldGbc.gridx = 1;
        autorCombobox = new JComboBox<>();
        detailsPanel.add(autorCombobox, fieldGbc);

        // Editorial
        labelGbc.gridy = fieldGbc.gridy = 3;
        labelGbc.gridx = 0;
        detailsPanel.add(createLabel("Editorial", boldFont), labelGbc);
        fieldGbc.gridx = 1;
        editorialField = createField(10, regularFont);
        detailsPanel.add(editorialField, fieldGbc);

        // Portada
        labelGbc.gridy = fieldGbc.gridy = 4;
        labelGbc.gridx = 0;
        detailsPanel.add(createLabel("Portada", boldFont), labelGbc);
        fieldGbc.gridx = 1;
        portadaField = createField(25, regularFont);
        detailsPanel.add(portadaField, fieldGbc);

        // ISBN + Idioma
        labelGbc.gridy = fieldGbc.gridy = 5;
        labelGbc.gridx = 0;
        detailsPanel.add(createLabel("ISBN", boldFont), labelGbc);
        fieldGbc.gridx = 1;
        fieldGbc.gridwidth = 1; // 1 celda
        isbnField = createField(10, regularFont);
        detailsPanel.add(isbnField, fieldGbc);

        labelGbc.gridx = 2;
        detailsPanel.add(createLabel("Idioma", boldFont), labelGbc);
        fieldGbc.gridx = 3;
        idiomaField = createField(10, regularFont);
        detailsPanel.add(idiomaField, fieldGbc);

        // Genero + Año Publicacion
        labelGbc.gridy = fieldGbc.gridy = 6;
        labelGbc.gridx = 0;
        detailsPanel.add(createLabel("Genero", boldFont), labelGbc);
        fieldGbc.gridx = 1;
        generoField = createField(10, regularFont);
        detailsPanel.add(generoField, fieldGbc);

        labelGbc.gridx = 2;
        detailsPanel.add(createLabel("Año Publicacion", boldFont), labelGbc);
        fieldGbc.gridx = 3;
        yearField = createField(10, regularFont);
        detailsPanel.add(yearField, fieldGbc);

        // Estado + Cantidad
        labelGbc.gridy = fieldGbc.gridy = 7;
        labelGbc.gridx = 0;
        detailsPanel.add(createLabel("Estado", boldFont), labelGbc);
        fieldGbc.gridx = 1;
        estadoCombobox = new JComboBox<>();
        detailsPanel.add(estadoCombobox, fieldGbc);

        labelGbc.gridx = 2;
        detailsPanel.add(createLabel("Cantidad", boldFont), labelGbc);
        fieldGbc.gridx = 3;
        cantidadField = createField(10, regularFont);
        detailsPanel.add(cantidadField, fieldGbc);

        // Botones
        JPanel botonesPanel = new JPanel(new GridBagLayout());
        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.insets = new Insets(5, 15, 0, 15);
        btnGbc.gridy = 0;
        btnGbc.weightx = 1.0;
        btnGbc.fill = GridBagConstraints.HORIZONTAL;

        btnGbc.gridx = 0;
        nuevoBtn = createButton("Nuevo", regularFont);
        botonesPanel.add(nuevoBtn, btnGbc);

        btnGbc.gridx = 1;
        guardarBtn = createButton("Guardar", regularFont);
        botonesPanel.add(guardarBtn, btnGbc);

        btnGbc.gridx = 2;
        eliminarBtn = createButton("Eliminar", regularFont);
        botonesPanel.add(eliminarBtn, btnGbc);

        // Tabla
        String[] columnas = {"ID", "Titulo", "Autor", "Editorial", "Portada", "ISBN", "Idioma", "Genero", "Publicacion", "Estado", "Cantidad", "Rating"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evitar edición directa
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaSorter = new TableRowSorter<>(modeloTabla);
        tablaLibros.setRowSorter(tablaSorter);
        tablaLibros.setRowHeight(65); // para que la imagen quepa bien
        tablaLibros.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaLibros.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tablaLibros.getColumnModel().getColumn(1).setPreferredWidth(200);  // Titulo
        tablaLibros.getColumnModel().getColumn(2).setPreferredWidth(150);  // Autor
        tablaLibros.getColumnModel().getColumn(3).setPreferredWidth(130);  // Editorial
        tablaLibros.getColumnModel().getColumn(4).setPreferredWidth(80);  // Portada
        tablaLibros.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
        tablaLibros.getColumnModel().getColumn(5).setPreferredWidth(120);  // ISBN
        tablaLibros.getColumnModel().getColumn(6).setPreferredWidth(100);  // Idioma
        tablaLibros.getColumnModel().getColumn(7).setPreferredWidth(100);  // Genero
        tablaLibros.getColumnModel().getColumn(8).setPreferredWidth(100);  // Publicacion
        tablaLibros.getColumnModel().getColumn(9).setPreferredWidth(100);  // Estado
        tablaLibros.getColumnModel().getColumn(10).setPreferredWidth(80);  // Cantidad
        tablaLibros.getColumnModel().getColumn(11).setPreferredWidth(100);  // Rating
        tablaLibros.getColumnModel().getColumn(11).setCellRenderer(new StarRatingRenderer());

        JScrollPane tablaScroll = new JScrollPane(tablaLibros);
        tablaScroll.setPreferredSize(new Dimension(610, 200));

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
            frame.setSize(610, 600);
            List<Rol> roles = new ArrayList<>();

            LibroForm form = new LibroForm();
            form.setVisible(true);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(form);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

}
