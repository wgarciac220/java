/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.vistas;

import gt.edu.miumg.programacion1.biblioteca.modelos.Rol;
import gt.edu.miumg.programacion1.biblioteca.util.ImageRenderer;
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
public class PrestamosForm extends JPanel {

    public JTextField tituloField;
    public JComboBox usuarioCombobox;
    public JTextField fechaField;
    public JButton prestarBtn;
    public JTable tablaLibros;
    public DefaultTableModel modeloTabla;
    public JTextField filtroField;
    public JButton aplicarFiltroBtn;
    public TableRowSorter<DefaultTableModel> tablaSorter;

    public PrestamosForm() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(610, 600));

        Font regularFont = new Font("Nunito", Font.PLAIN, 14);
        Font boldFont = new Font("Nunito", Font.BOLD, 14);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 0, 20);
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setPreferredSize(new Dimension(610, 150));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 10), // Espacio alrededor
                BorderFactory.createTitledBorder("Administrador de Prestamos") // Borde interno
        ));

        // Fila 1: Libro
        gbc.gridy = 1;
        gbc.gridx = 0;
        detailsPanel.add(createLabel("Libro seleccionado", boldFont), gbc);
        gbc.gridx = 1;
        tituloField = createField(10, regularFont);
        detailsPanel.add(tituloField, gbc);

        // Fila 2: Usuario
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        detailsPanel.add(createLabel("Usuario", boldFont), gbc);
        gbc.gridx = 1;
        usuarioCombobox = new JComboBox<>();
        detailsPanel.add(usuarioCombobox, gbc);

        // Fila 3: Pais Origen
        gbc.gridy = 2;
        gbc.gridx = 0;
        detailsPanel.add(createLabel("Fecha devolucion", boldFont), gbc);
        gbc.gridx = 1;
        fechaField = createField(10, regularFont);
        detailsPanel.add(fechaField, gbc);

        // Panel centrado para el único botón
        JPanel botonesPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBoton = new GridBagConstraints();
        gbcBoton.gridx = 0;
        gbcBoton.gridy = 0;
        gbcBoton.insets = new Insets(5, 175, 0, 175);
        gbcBoton.fill = GridBagConstraints.HORIZONTAL;
        gbcBoton.weightx = 1.0;

        prestarBtn = createButton("Prestar", regularFont);
        prestarBtn.setPreferredSize(new Dimension(0, 40));
        botonesPanel.add(prestarBtn, gbcBoton);

        // Tabla
        String[] columnas = {"ID", "Titulo", "Autor", "Editorial", "Portada", "ISBN", "Idioma", "Genero", "Publicacion", "Estado", "Cantidad"};
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

        JScrollPane tablaScroll = new JScrollPane(tablaLibros);
        tablaScroll.setPreferredSize(new Dimension(610, 325));

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

            PrestamosForm form = new PrestamosForm();
            form.setVisible(true);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(form);
            frame.setVisible(true);
        });
    }
}
