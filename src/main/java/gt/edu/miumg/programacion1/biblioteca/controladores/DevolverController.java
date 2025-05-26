/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.ILibroData;
import gt.edu.miumg.programacion1.biblioteca.datasources.IPrestamoData;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.LibroDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.PrestamoDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.dto.HistorialConLibro;
import gt.edu.miumg.programacion1.biblioteca.dto.LibroConAutor;
import gt.edu.miumg.programacion1.biblioteca.dto.UsuarioConRol;
import gt.edu.miumg.programacion1.biblioteca.modelos.HistorialPrestamo;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.setTextAndResetCaret;
import gt.edu.miumg.programacion1.biblioteca.vistas.DevolucionesForm;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author wgarciac
 */
public class DevolverController {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "dimrnyW-9";

    private ILibroData dataLibro;
    private List<LibroConAutor> libros;
    private IPrestamoData dataPrestamo;
    private List<HistorialConLibro> prestamos;
    private UsuarioConRol usuarioLogueado;

    private DevolucionesForm devolverForm;

    public DevolverController(UsuarioConRol usuario) {

        try {
            this.usuarioLogueado = usuario;

            this.dataLibro = new LibroDataMySQL(URL, USER, PASSWORD);
            this.libros = dataLibro.getAllBooks();
            this.dataPrestamo = new PrestamoDataMySQL(URL, USER, PASSWORD);
            this.prestamos = dataPrestamo.getAllBorrowings();

            this.devolverForm = new DevolucionesForm();
            this.devolverForm.devolverBtn.addActionListener(e -> this.SaveData());
            this.devolverForm.aplicarFiltroBtn.addActionListener(e -> this.ApplyFilter());
            this.devolverForm.tablaHistorial.getSelectionModel().addListSelectionListener(e -> this.LlenarDetallesHistorial(e));

            this.LlenarComboboxRating();
            this.LlenarTablaHistorial();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.devolverForm, "Ocurrió un error al cargar los prestamos:\n" + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    public DevolucionesForm Mostrar() {
        return devolverForm;
    }

    private void ClearFieldsData() {
        this.devolverForm.tituloField.setText("");
        this.devolverForm.historialIdFied.setText("");
        this.devolverForm.fechaLimiteField.setText("");
        this.devolverForm.multaField.setText("");
        this.devolverForm.resenaField.setText("");
        this.devolverForm.ratingCombobox.setSelectedIndex(0);
    }

    private void SaveData() {
        try {
            Short idHistorial = (short) Integer.parseInt(this.devolverForm.historialIdFied.getText().trim());
            HistorialConLibro prestamoExistente = this.prestamos.stream()
                    .filter(p -> p.getId().equals(idHistorial))
                    .findFirst()
                    .orElse(null);

            if ("Devuelto".equalsIgnoreCase(prestamoExistente.getEstado())) {
                JOptionPane.showMessageDialog(devolverForm, "Este libro ya fue devuelto", "No es posible cambiar estado", JOptionPane.WARNING_MESSAGE);
                this.devolverForm.tablaHistorial.clearSelection(); // Quita la selección
                return;
            }

            LocalDateTime fechaDevolucion = LocalDateTime.now();
            Short idTitulo = prestamoExistente.getLibroId();
            Short idUsuario = prestamoExistente.getUsuarioId();
            LocalDateTime fechaPrestamo = prestamoExistente.getFechaPrestamo();

            String resenaText = this.devolverForm.resenaField.getText().trim();
            BigDecimal multa = new BigDecimal(this.devolverForm.multaField.getText().trim());
            byte rating = Byte.parseByte(this.devolverForm.ratingCombobox.getSelectedItem().toString());

            if (resenaText.isEmpty()) {
                JOptionPane.showMessageDialog(this.devolverForm, "Por favor llena los campos obligatorios:\nReseña, Rating.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            HistorialPrestamo historial = new HistorialPrestamo(idHistorial, idUsuario, idTitulo, fechaPrestamo, "Devuelto", fechaDevolucion, multa, resenaText, rating);

            this.dataPrestamo.updateBorrowing(historial);
            JOptionPane.showMessageDialog(this.devolverForm, "Prestamo actualizado exitosamente.", "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);

            this.prestamos = dataPrestamo.getAllBorrowings();
            this.ClearFieldsData();
            this.LlenarTablaHistorial();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.devolverForm, "Error en la base de datos: " + ex.getMessage(), "Error de guardado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ApplyFilter() {
        String texto = this.devolverForm.filtroField.getText().toLowerCase();

        if (texto.trim().isEmpty()) {
            this.devolverForm.tablaSorter.setRowFilter(null);
        } else {
            this.devolverForm.tablaSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    private void LlenarTablaHistorial() {
        this.devolverForm.tablaHistorial.clearSelection();
        this.devolverForm.modeloTabla.setRowCount(0); // Limpiar tabla

        List<HistorialConLibro> userHistorial = this.prestamos.stream()
                .filter(historial -> historial.getUsuarioId().equals(usuarioLogueado.getId()))
                .collect(Collectors.toList());

        for (HistorialConLibro prestamo : userHistorial) {
            Object[] fila = {
                prestamo.getId(),
                prestamo.getLibroTitulo(),
                prestamo.getFechaPrestamo().toString(),
                prestamo.getEstado(),
                prestamo.getFechaDevolucion().toString(),
                prestamo.getRating(),
                prestamo.getMulta().toString(),
                prestamo.getResena(),
                prestamo.getLibroId()
            };
            this.devolverForm.modeloTabla.addRow(fila);
        }
    }

    private void LlenarDetallesHistorial(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = this.devolverForm.tablaHistorial.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = this.devolverForm.tablaHistorial.convertRowIndexToModel(selectedRow);   // indice a modelo
        String estado = this.devolverForm.modeloTabla.getValueAt(modelRow, 3).toString();
        Object rawValue = this.devolverForm.modeloTabla.getValueAt(modelRow, 5);
        int rating = (rawValue instanceof Byte) ? ((Byte) rawValue).intValue() : Integer.parseInt(rawValue.toString());

        setTextAndResetCaret(this.devolverForm.historialIdFied, this.devolverForm.modeloTabla.getValueAt(modelRow, 0).toString());
        setTextAndResetCaret(this.devolverForm.tituloField, this.devolverForm.modeloTabla.getValueAt(modelRow, 1).toString());
        setTextAndResetCaret(this.devolverForm.fechaPrestamoField, this.devolverForm.modeloTabla.getValueAt(modelRow, 2).toString());
        setTextAndResetCaret(this.devolverForm.fechaLimiteField, this.devolverForm.modeloTabla.getValueAt(modelRow, 4).toString());
        setTextAndResetCaret(this.devolverForm.resenaField, this.devolverForm.modeloTabla.getValueAt(modelRow, 7).toString());

        BigDecimal multa = this.CalcularMulta();
        setTextAndResetCaret(this.devolverForm.multaField, multa.toPlainString());
        this.devolverForm.ratingCombobox.setSelectedItem(rating);
        this.devolverForm.devolverBtn.setEnabled(!estado.equalsIgnoreCase("devuelto"));

    }

    private void LlenarComboboxRating() {
        Integer[] ratings = {0, 1, 2, 3, 4, 5};
        for (Integer r : ratings) {
            this.devolverForm.ratingCombobox.addItem(r);
        }
    }

    private BigDecimal CalcularMulta() {
        String fechaTexto = this.devolverForm.fechaLimiteField.getText().trim();

        try {
            if (fechaTexto.length() > 10) {
                fechaTexto = fechaTexto.substring(0, 10);
            }

            LocalDate fechaLimite = LocalDate.parse(fechaTexto);
            LocalDate hoy = LocalDate.now();

            long diasRetraso = ChronoUnit.DAYS.between(fechaLimite, hoy);

            if (diasRetraso > 0) {
                return BigDecimal.valueOf(diasRetraso * 10);
            }
        } catch (DateTimeParseException e) {
            System.err.println("Fecha inválida: " + fechaTexto);
        }

        return BigDecimal.ZERO;
    }
}
