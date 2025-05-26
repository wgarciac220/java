/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.vistas;

import gt.edu.miumg.programacion1.biblioteca.modelos.HistorialPrestamo;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.createButton;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.createField;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.createLabel;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.createTextArea;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author wgarciac
 */
public class DevolucionesForm extends JPanel {

    public JTextField tituloField;
    public JTextField historialIdFied;
    public JTextField fechaPrestamoField;
    public JTextField fechaLimiteField;
    public JTextField multaField;
    public JTextArea resenaField;
    public JComboBox ratingCombobox;
    public JButton devolverBtn;
    public JTable tablaHistorial;
    public DefaultTableModel modeloTabla;
    public JTextField filtroField;
    public JButton aplicarFiltroBtn;
    public TableRowSorter<DefaultTableModel> tablaSorter;

    public DevolucionesForm() {

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
        detailsPanel.setPreferredSize(new Dimension(610, 280));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 10), // Espacio alrededor
                BorderFactory.createTitledBorder("Administrador de Devoluciones") // Borde interno
        ));

        // Fila 1: Historial
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("ID Prestamo", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        historialIdFied = createField(10, regularFont);
        historialIdFied.setEnabled(false);
        detailsPanel.add(historialIdFied, gbc);

        // Fila 2: Libro
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Libro seleccionado", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        tituloField = createField(10, regularFont);
        tituloField.setEnabled(false);
        detailsPanel.add(tituloField, gbc);

        // Fila 3: Fecha Prestamo
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Fecha Prestamo", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        fechaPrestamoField = createField(10, regularFont);
        fechaPrestamoField.setEnabled(false);
        detailsPanel.add(fechaPrestamoField, gbc);

        // Fila 4: Fecha Devolucion
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Fecha Limite", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        fechaLimiteField = createField(10, regularFont);
        fechaLimiteField.setEnabled(false);
        detailsPanel.add(fechaLimiteField, gbc);

        // Fila 5: Multa
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Multa", boldFont), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        multaField = createField(10, regularFont);
        multaField.setEnabled(false);
        detailsPanel.add(multaField, gbc);

        // Fila 6: Reseña (ocupa 3 filas: 5, 6, 7)
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        detailsPanel.add(createLabel("Reseña", boldFont), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        resenaField = createTextArea(regularFont);
        JScrollPane scrollPane = new JScrollPane(resenaField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailsPanel.add(scrollPane, gbc);

        // Fila 8: Rating (después de la reseña)
        gbc.gridheight = 1;
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        detailsPanel.add(createLabel("Rating", boldFont), gbc);

        gbc.gridx = 1;
        ratingCombobox = new JComboBox<>();
        ratingCombobox.setPreferredSize(new Dimension(100, 25));
        detailsPanel.add(ratingCombobox, gbc);

        // Panel centrado para el botón Devolver
        JPanel botonesPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBoton = new GridBagConstraints();
        gbcBoton.gridx = 0;
        gbcBoton.gridy = 0;
        gbcBoton.insets = new Insets(0, 175, 0, 175);
        gbcBoton.fill = GridBagConstraints.HORIZONTAL;
        gbcBoton.weightx = 1.0;

        devolverBtn = createButton("Devolver", regularFont);
        botonesPanel.add(devolverBtn, gbcBoton);

        // Tabla
        String[] columnas = {"ID", "Titulo", "Fecha Prestamo", "Estado", "Fecha Devolucion", "Rating", "Multa", "Reseña", "Libro ID"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evitar edición directa
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaSorter = new TableRowSorter<>(modeloTabla);
        tablaHistorial.setRowSorter(tablaSorter);
        tablaHistorial.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tablaHistorial.getColumnModel().getColumn(1).setPreferredWidth(200);  // Titulo
        tablaHistorial.getColumnModel().getColumn(2).setPreferredWidth(150);  // Fecha Prestamo
        tablaHistorial.getColumnModel().getColumn(3).setPreferredWidth(130);  // Estado
        tablaHistorial.getColumnModel().getColumn(4).setPreferredWidth(150);  // Fecha Devolucion
        tablaHistorial.getColumnModel().getColumn(5).setPreferredWidth(120);  // Rating
        tablaHistorial.getColumnModel().getColumn(6).setPreferredWidth(120);  // Multa
        tablaHistorial.getColumnModel().getColumn(7).setPreferredWidth(200);  // Reseña
        tablaHistorial.getColumnModel().getColumn(8).setMinWidth(0);  // columna AutorID oculta
        tablaHistorial.getColumnModel().getColumn(8).setMaxWidth(0);
        tablaHistorial.getColumnModel().getColumn(8).setWidth(0);

        JScrollPane tablaScroll = new JScrollPane(tablaHistorial);
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
            frame.setSize(800, 625);
            List<HistorialPrestamo> historial = new ArrayList<>();
//            roles.add(new Rol((short) 1, "Rol 1"));
//            roles.add(new Rol((short) 1, "Rol 2"));
//            roles.add(new Rol((short) 1, "Rol 3"));

            DevolucionesForm form = new DevolucionesForm();
            form.setVisible(true);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(form);
            frame.setVisible(true);
        });
    }

}
