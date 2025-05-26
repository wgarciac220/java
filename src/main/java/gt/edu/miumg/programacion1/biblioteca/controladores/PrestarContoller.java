/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.ILibroData;
import gt.edu.miumg.programacion1.biblioteca.datasources.IPrestamoData;
import gt.edu.miumg.programacion1.biblioteca.datasources.IUsuarioData;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.LibroDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.PrestamoDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.UsuarioDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.dto.HistorialConLibro;
import gt.edu.miumg.programacion1.biblioteca.dto.LibroConAutor;
import gt.edu.miumg.programacion1.biblioteca.dto.UsuarioConRol;
import gt.edu.miumg.programacion1.biblioteca.modelos.HistorialPrestamo;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import gt.edu.miumg.programacion1.biblioteca.vistas.PrestamosForm;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author wgarciac
 */
public class PrestarContoller {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "dimrnyW-9";

    private ILibroData dataLibro;
    private List<LibroConAutor> libros;
    private IUsuarioData dataUsuario;
    private List<UsuarioConRol> usuarios;
    private IPrestamoData dataPrestamo;
    private List<HistorialConLibro> prestamos;

    private PrestamosForm prestarForm;

    public PrestarContoller() {
        try {
            this.dataLibro = new LibroDataMySQL(URL, USER, PASSWORD);
            this.libros = dataLibro.getAllBooks();
            this.dataUsuario = new UsuarioDataMySQL(URL, USER, PASSWORD);
            this.usuarios = dataUsuario.getAllUsers();
            this.dataPrestamo = new PrestamoDataMySQL(URL, USER, PASSWORD);
            this.prestamos = dataPrestamo.getAllBorrowings();

            LocalDate fechaDevolucion = LocalDate.now().plusDays(15);
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String fechaTexto = fechaDevolucion.format(formato);

            this.prestarForm = new PrestamosForm();
            this.prestarForm.tituloField.setEnabled(false);
            this.prestarForm.fechaField.setText(fechaTexto);
            this.prestarForm.fechaField.setEnabled(false);
            this.prestarForm.prestarBtn.addActionListener(e -> this.SaveData());
            this.prestarForm.aplicarFiltroBtn.addActionListener(e -> this.ApplyFilter());
            this.prestarForm.tablaLibros.getSelectionModel().addListSelectionListener(e -> this.LlenarDetallesLibro(e));

            this.LlenarTablaLibros();
            this.LlenarComboboxUsuario();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.prestarForm, "Ocurrió un error al cargar los libros:\n" + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    public PrestamosForm Mostrar() {
        return prestarForm;
    }

    private void ClearFieldsData() {
        this.prestarForm.tituloField.setText("");
        this.prestarForm.usuarioCombobox.setSelectedIndex(0);
        this.prestarForm.fechaField.setText("");
    }

    private void SaveData() {
        try {
            String idTitulo = this.prestarForm.tituloField.getText().trim();
            Usuario usuarioSeleccionado = (Usuario) this.prestarForm.usuarioCombobox.getSelectedItem();
            String devolucion = this.prestarForm.fechaField.getText().trim();

            if (idTitulo.isEmpty() || usuarioSeleccionado.getName().isEmpty() || devolucion.isEmpty()) {
                JOptionPane.showMessageDialog(this.prestarForm, "Por favor llena los campos obligatorios:\nId Libro, Usuario.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDateTime fechaPrestamo = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate fecha = LocalDate.parse(devolucion, formato);

            Short idPrestamo = null;
            BigDecimal multa = BigDecimal.valueOf(0.0);
            LocalDateTime fechaDevolucion = fecha.atStartOfDay();
            Short idLibro = (short) Integer.parseInt(idTitulo);
            Short idUsuario = usuarioSeleccionado.getId();

            HistorialPrestamo historial = new HistorialPrestamo(idPrestamo, idUsuario, idLibro, fechaPrestamo, "Prestado", fechaDevolucion, multa, "", (byte) 0);

            if (dataPrestamo.hasActiveBorrowing(historial.getUsuarioId(), historial.getLibroId())) {
                JOptionPane.showMessageDialog(prestarForm, "El usuario ya tiene un préstamo activo de este libro.", "Préstamo duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Short nuevoId = this.dataPrestamo.registerBorrowing(historial);
            if (nuevoId != null) {
                JOptionPane.showMessageDialog(this.prestarForm, "Prestamo registrado exitosamente con ID: " + nuevoId, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.prestarForm, "No se pudo registrar el prestamo.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            this.prestamos = dataPrestamo.getAllBorrowings();
            this.LlenarTablaLibros();
            this.ClearFieldsData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.prestarForm, "Error en la base de datos: " + ex.getMessage(), "Error de guardado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ApplyFilter() {
        String texto = this.prestarForm.filtroField.getText().toLowerCase();

        if (texto.trim().isEmpty()) {
            this.prestarForm.tablaSorter.setRowFilter(null);
        } else {
            this.prestarForm.tablaSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    private void LlenarTablaLibros() {
        this.prestarForm.tablaLibros.clearSelection();
        this.prestarForm.modeloTabla.setRowCount(0); // Limpiar tabla

        for (LibroConAutor libro : this.libros) {
            Object[] fila = {
                libro.getId(),
                libro.getTitulo(),
                libro.getAutorNombre(),
                libro.getEditorial(),
                libro.getPortada(),
                libro.getIsbn(),
                libro.getIdioma(),
                libro.getGenero(),
                libro.getAnoPublicacion(),
                libro.getEstado(),
                libro.getCantidad() - libro.getPrestados(),
                libro.getRating()
            };
            this.prestarForm.modeloTabla.addRow(fila);
        }
    }

    private void LlenarDetallesLibro(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = this.prestarForm.tablaLibros.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = this.prestarForm.tablaLibros.convertRowIndexToModel(selectedRow);   // indice a modelo
        String estado = this.prestarForm.modeloTabla.getValueAt(modelRow, 9).toString();
        Short cantidad = Short.valueOf(this.prestarForm.modeloTabla.getValueAt(modelRow, 10).toString());

        this.prestarForm.tituloField.setText(this.prestarForm.modeloTabla.getValueAt(modelRow, 0).toString());
        this.prestarForm.prestarBtn.setEnabled(estado.equalsIgnoreCase("disponible") && cantidad > 0);
    }

    private void LlenarComboboxUsuario() {
        for (UsuarioConRol usuario : this.usuarios) {
            this.prestarForm.usuarioCombobox.addItem(usuario.toUsuario());
        }
    }
}
