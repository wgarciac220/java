/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.json.LibroData;
import gt.edu.miumg.programacion1.biblioteca.datasources.json.PrestamoData;
import gt.edu.miumg.programacion1.biblioteca.datasources.json.ResenaData;
import gt.edu.miumg.programacion1.biblioteca.modelos.HistorialPrestamo;
import gt.edu.miumg.programacion1.biblioteca.modelos.Libro;
import gt.edu.miumg.programacion1.biblioteca.modelos.Resena;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import static gt.edu.miumg.programacion1.biblioteca.util.SwingControls.setTextAndResetCaret;
import gt.edu.miumg.programacion1.biblioteca.vistas.DevolucionesForm;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author wgarciac
 */
public class DevolverController {

    private static final String DATA_LIBROS = "data/libros.json";
    private static final String DATA_RESENAS = "data/resenas.json";
    private static final String DATA_PRESTAMOS = "data/prestamos.json";
    private static final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String user = "root";
    private static final String password = "dimrnyW-9";

    private LibroData data;
    private List<Libro> libros;
    private PrestamoData prestamoData;
    private List<HistorialPrestamo> prestamos;
    private ResenaData resenaData;
    private List<Resena> resenas;
    private Usuario usuarioLogueado;

    private DevolucionesForm devolverForm;

    public DevolverController(Usuario usuario) {

        try {
            this.usuarioLogueado = usuario;

            this.data = new LibroData(DATA_LIBROS);
            this.libros = data.cargarLibros();
            this.prestamoData = new PrestamoData(DATA_PRESTAMOS);
            this.prestamos = prestamoData.cargarHistorial();
            this.resenaData = new ResenaData(DATA_RESENAS);
            this.resenas = resenaData.cargaResenas();

            this.devolverForm = new DevolucionesForm();
            this.devolverForm.devolverBtn.addActionListener(e -> this.SaveData());
            this.devolverForm.aplicarFiltroBtn.addActionListener(e -> this.ApplyFilter());
            this.devolverForm.tablaHistorial.getSelectionModel().addListSelectionListener(e -> this.LlenarDetallesHistorial(e));

            this.LlenarComboboxRating();
            this.LlenarTablaHistorial();
        } catch (IOException ex) {
            Logger.getLogger(DevolverController.class.getName()).log(Level.SEVERE, null, ex);
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
        String titulo = this.devolverForm.tituloField.getText().trim();

        Short idHistorial = (short) Integer.parseInt(this.devolverForm.historialIdFied.getText().trim());
        Short idUsuario = usuarioLogueado.getId();
        Short idTitulo = Short.valueOf(titulo.split(" - ")[0]);
        String estado = "Devuelto";
        LocalDateTime fechaPrestamo = LocalDateTime.parse(this.devolverForm.fechaPrestamoField.getText().trim());
        LocalDateTime fechaDevolucion = LocalDateTime.now();
        BigDecimal multa = new BigDecimal(this.devolverForm.multaField.getText().trim());
        String resenaText = this.devolverForm.resenaField.getText().trim();
        byte rating = Byte.parseByte(this.devolverForm.ratingCombobox.getSelectedItem().toString());

        if (resenaText.isEmpty()) {
            JOptionPane.showMessageDialog(this.devolverForm, "Por favor llena los campos obligatorios:\nReseña, Rating.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        HistorialPrestamo historial = new HistorialPrestamo(idHistorial, idUsuario, idTitulo, fechaPrestamo, estado, fechaDevolucion, multa);
        Resena resena = new Resena(idTitulo, idUsuario, idTitulo, resenaText, rating);

        this.prestamos = this.prestamos.stream()
                .map(p -> p.getId().equals(idUsuario) ? historial : p)
                .collect(Collectors.toList());

        boolean existe = this.resenas.stream()
                .anyMatch(r -> r.getUsuarioId().equals(idUsuario) && r.getLibroId().equals(idTitulo));

        if (existe) {
            this.resenas = this.resenas.stream()
                    .map(r -> (r.getUsuarioId().equals(idUsuario) && r.getLibroId().equals(idTitulo)) ? resena : r)
                    .collect(Collectors.toList());
        } else {
            this.resenas.add(resena);
        }

        this.prestamoData.guardarHistorial(this.prestamos);
        this.resenaData.guardarResenas(this.resenas);
        this.ClearFieldsData();
        this.LlenarTablaHistorial();
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

        List<HistorialPrestamo> userHistorial = this.prestamos.stream()
                .filter(h -> h.getUsuarioId().equals(usuarioLogueado.getId()))
                .collect(Collectors.toList());

        for (HistorialPrestamo prestamo : userHistorial) {
            Resena existingResena = this.resenas.stream()
                    .filter(r -> r.getLibroId().equals(prestamo.getLibroId())
                    && r.getUsuarioId().equals(usuarioLogueado.getId())
                    )
                    .findFirst()
                    .orElse(null);

            byte rating = (existingResena != null) ? existingResena.getRating() : 0;
            String textoResena = (existingResena != null) ? existingResena.getResena() : "";

            Object[] fila = {
                prestamo.getId(),
                this.getLibroById(prestamo.getLibroId()),
                prestamo.getFechaPrestamo().toString(),
                prestamo.getEstado(),
                prestamo.getFechaDevolucion().toString(),
                rating,
                prestamo.getMulta().toString(),
                textoResena
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
        if ("Devuelto".equalsIgnoreCase(estado.trim())) {
            JOptionPane.showMessageDialog(devolverForm,
                    "Este préstamo ya fue devuelto y no puede ser seleccionado.",
                    "Préstamo no válido", JOptionPane.WARNING_MESSAGE);
            this.devolverForm.tablaHistorial.clearSelection(); // Quita la selección
            return;
        }

        setTextAndResetCaret(this.devolverForm.historialIdFied, this.devolverForm.modeloTabla.getValueAt(modelRow, 0).toString());
        setTextAndResetCaret(this.devolverForm.tituloField, this.devolverForm.modeloTabla.getValueAt(modelRow, 1).toString());
        setTextAndResetCaret(this.devolverForm.fechaPrestamoField, this.devolverForm.modeloTabla.getValueAt(modelRow, 2).toString());
        setTextAndResetCaret(this.devolverForm.fechaLimiteField, this.devolverForm.modeloTabla.getValueAt(modelRow, 4).toString());
        setTextAndResetCaret(this.devolverForm.resenaField, this.devolverForm.modeloTabla.getValueAt(modelRow, 7).toString());

        BigDecimal multa = this.CalcularMulta();
        setTextAndResetCaret(this.devolverForm.multaField, multa.toPlainString());

        Object rawValue = this.devolverForm.modeloTabla.getValueAt(modelRow, 5);
        int rating = (rawValue instanceof Byte) ? ((Byte) rawValue).intValue() : Integer.parseInt(rawValue.toString());
        this.devolverForm.ratingCombobox.setSelectedItem(rating);
    }

    private void LlenarComboboxRating() {
        Integer[] ratings = {1, 2, 3, 4, 5};
        for (Integer r : ratings) {
            this.devolverForm.ratingCombobox.addItem(r);
        }
    }

    private String getLibroById(short libroId) {
        for (Libro libro : this.libros) {
            if (libro.getId().equals(libroId)) {
                return libro.getId().toString() + " - " + libro.getTitulo();
            }
        }
        return null; // Si no se encuentra
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
