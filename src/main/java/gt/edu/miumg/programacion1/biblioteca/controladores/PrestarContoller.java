/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.json.AutorData;
import gt.edu.miumg.programacion1.biblioteca.datasources.json.LibroData;
import gt.edu.miumg.programacion1.biblioteca.datasources.json.PrestamoData;
import gt.edu.miumg.programacion1.biblioteca.datasources.json.UsuarioData;
import gt.edu.miumg.programacion1.biblioteca.modelos.Autor;
import gt.edu.miumg.programacion1.biblioteca.modelos.HistorialPrestamo;
import gt.edu.miumg.programacion1.biblioteca.modelos.Libro;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import gt.edu.miumg.programacion1.biblioteca.vistas.PrestamosForm;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author wgarciac
 */
public class PrestarContoller {

    private static final String DATA_LIBROS = "data/libros.json";
    private static final String DATA_AUTORES = "data/autores.json";
    private static final String DATA_USUARIOS = "data/usuarios.json";
    private static final String DATA_PRESTAMOS = "data/prestamos.json";
    private static final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String user = "root";
    private static final String password = "dimrnyW-9";

    private LibroData data;
    private List<Libro> libros;
    private AutorData dataAutor;
    private List<Autor> autores;
    private UsuarioData usuarioData;
    private List<Usuario> usuarios;
    private PrestamoData prestamoData;
    private List<HistorialPrestamo> prestamos;

    private PrestamosForm prestarForm;

    public PrestarContoller() {
        try {
            this.data = new LibroData(DATA_LIBROS);
            this.libros = data.cargarLibros();
            this.dataAutor = new AutorData(DATA_AUTORES);
            this.autores = dataAutor.cargarAutores();
            this.usuarioData = new UsuarioData(DATA_USUARIOS);
            this.usuarios = usuarioData.cargarUsuarios();
            this.prestamoData = new PrestamoData(DATA_PRESTAMOS);
            this.prestamos = prestamoData.cargarHistorial();

            LocalDate fechaDevolucion = LocalDate.now().plusDays(15);
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String fechaTexto = fechaDevolucion.format(formato);

            this.prestarForm = new PrestamosForm();
            this.prestarForm.fechaField.setText(fechaTexto);
            this.prestarForm.prestarBtn.addActionListener(e -> this.SaveData());
            this.prestarForm.aplicarFiltroBtn.addActionListener(e -> this.ApplyFilter());
            this.prestarForm.tablaLibros.getSelectionModel().addListSelectionListener(e -> this.LlenarDetallesLibro(e));

            this.LlenarTablaLibros();
            this.LlenarComboboxUsuario();
        } catch (IOException ex) {
            Logger.getLogger(PrestarContoller.class.getName()).log(Level.SEVERE, null, ex);
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
        String idTitulo = this.prestarForm.tituloField.getText().trim();
        String usuarioNombre = this.prestarForm.usuarioCombobox.getSelectedItem().toString();
        String devolucion = this.prestarForm.fechaField.getText().trim();

        if (idTitulo.isEmpty() || usuarioNombre.isEmpty() || devolucion.isEmpty()) {
            JOptionPane.showMessageDialog(this.prestarForm, "Por favor llena los campos obligatorios:\nId Libro, Usuario.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime fechaPrestamo = LocalDateTime.now();
//        LocalDate fecha = LocalDate.parse(devolucion);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate fecha = LocalDate.parse(devolucion, formato);

        LocalDateTime fechaDevolucion = fecha.atStartOfDay();
        Short idLibro = (short) Integer.parseInt(idTitulo);
        Short idUsuario = this.getIdUsuariobyName(usuarioNombre);
        Short idPrestamo = (short) (this.prestamos.stream().mapToInt(historial -> historial.getId()).max().orElse(0) + 1);
        BigDecimal multa = BigDecimal.valueOf(0.0);

        HistorialPrestamo historial = new HistorialPrestamo(idPrestamo, idUsuario, idLibro, fechaPrestamo, "Prestado", fechaDevolucion, multa);

        this.prestamos.add(historial);
        prestamoData.guardarHistorial(this.prestamos);
        this.ClearFieldsData();
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

        for (Libro libro : this.libros) {
            Object[] fila = {
                libro.getId(),
                libro.getTitulo(),
                this.getNombreAutorById(libro.getAutorId()),
                libro.getEditorial(),
                libro.getPortada(),
                libro.getIsbn(),
                libro.getIdioma(),
                libro.getGenero(),
                libro.getAnoPublicacion(),
                libro.getEstado(),
                libro.getCantidad(),};
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

        this.prestarForm.tituloField.setText(this.prestarForm.modeloTabla.getValueAt(modelRow, 0).toString());
    }

    private void LlenarComboboxUsuario() {
        for (Usuario usuario : this.usuarios) {
            this.prestarForm.usuarioCombobox.addItem(usuario.getName());
        }
    }

    private String getNombreAutorById(Short id) {
        for (Autor autor : this.autores) {
            if (autor.getId().equals(id)) {
                return autor.getNombre();
            }
        }
        return null; // Si no se encuentra
    }

    private Short getIdUsuariobyName(String nombreUsuario) {
        for (Usuario usuario : this.usuarios) {
            if (usuario.getName().equals(nombreUsuario)) {
                return usuario.getId();
            }
        }
        return null; // Si no se encuentra
    }
}
