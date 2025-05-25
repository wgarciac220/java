/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.IAutorData;
import gt.edu.miumg.programacion1.biblioteca.datasources.ILibroData;
import gt.edu.miumg.programacion1.biblioteca.datasources.json.ResenaData;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.AutorDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.LibroDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.dto.LibroConAutor;
import gt.edu.miumg.programacion1.biblioteca.modelos.Autor;
import gt.edu.miumg.programacion1.biblioteca.modelos.Libro;
import gt.edu.miumg.programacion1.biblioteca.modelos.Resena;
import gt.edu.miumg.programacion1.biblioteca.vistas.LibroForm;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author wgarciac
 */
public class LibroController {

    private static final String DATA_RESENAS = "data/resenas.json";
    private static final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String user = "root";
    private static final String password = "dimrnyW-9";

    private LibroForm libroForm;

    private ILibroData dataLibro;
    private List<LibroConAutor> libros;

    private IAutorData dataAutor;
    private List<Autor> autores;

    private ResenaData resenaData;
    private List<Resena> resenas;

    private List<String> estados;

    public LibroController() {
        try {
            //            this.resenaData = new ResenaData(DATA_RESENAS);
//            this.resenas = resenaData.cargaResenas();

            this.dataLibro = new LibroDataMySQL(url, user, password);
            this.libros = dataLibro.getAllBooks();
            this.dataAutor = new AutorDataMySQL(url, user, password);
            this.autores = dataAutor.getAllAuthors();

            this.estados = new ArrayList<>();
            this.estados.add("Disponible");
            this.estados.add("Retirado");
            this.libroForm = new LibroForm();
            this.libroForm.portadaField.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    LibroController.this.SeleccionarFotografia();
                }
            });
            this.libroForm.nuevoBtn.addActionListener(e -> this.ClearFieldsData());
            this.libroForm.guardarBtn.addActionListener(e -> this.SaveData());
            this.libroForm.eliminarBtn.addActionListener(e -> this.DeleteData());
            this.libroForm.aplicarFiltroBtn.addActionListener(e -> this.ApplyFilter());
            this.libroForm.tablaLibros.getSelectionModel().addListSelectionListener(e -> this.LlenarDetallesLibro(e));
            this.LlenarTablaLibros();
            this.LlenarComboboxAutor();
            this.LlenarComboboxEstado();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.libroForm, "Ocurrió un error al cargar los libros:\n" + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    public LibroForm Mostrar() {
        return libroForm;
    }

    private void ClearFieldsData() {
        this.libroForm.idField.setText("");
        this.libroForm.tituloField.setText("");
        this.libroForm.editorialField.setText("");
        this.libroForm.portadaField.setText("");
        this.libroForm.isbnField.setText("");
        this.libroForm.idiomaField.setText("");
        this.libroForm.generoField.setText("");
        this.libroForm.yearField.setText("");
        this.libroForm.estadoCombobox.setSelectedIndex(0);
        this.libroForm.cantidadField.setText("");
    }

    private void SaveData() {
        try {
            Autor autorSeleccionado = (Autor) this.libroForm.autorCombobox.getSelectedItem();

            String titulo = this.libroForm.tituloField.getText().trim();
            Short idAutor = autorSeleccionado.getId();
            String editorial = this.libroForm.editorialField.getText().trim();
            String portada = this.libroForm.portadaField.getText().trim();
            String isbn = this.libroForm.isbnField.getText().trim();
            String idioma = this.libroForm.idiomaField.getText().trim();
            String genero = this.libroForm.generoField.getText().trim();
            String year = this.libroForm.yearField.getText().trim();
            String estado = this.libroForm.estadoCombobox.getSelectedItem().toString();
            String cantidad = this.libroForm.cantidadField.getText().trim();

            if (titulo.isEmpty() || editorial.isEmpty() || isbn.isEmpty() || idioma.isEmpty() || genero.isEmpty() || year.isEmpty() || estado.isEmpty() || cantidad.isEmpty()) {
                JOptionPane.showMessageDialog(this.libroForm, "Por favor llena los campos obligatorios:\nTitulo, Autor, Editorial, ISBN, Idioma, Genero, Publicacion, Estado y Cantidad.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean isNewRecord = this.libroForm.idField.getText().isBlank();
            Short idLibro = isNewRecord ? null : Short.valueOf(this.libroForm.idField.getText());
            Float rating = 0.0f;
            Short cantidadLibros = Short.valueOf(cantidad);
            Year anoPublicacion = Year.of(Integer.parseInt(year));

            Libro libro = new Libro(idLibro, titulo, isbn, genero, cantidadLibros, portada, anoPublicacion, editorial, idioma, rating, idAutor, estado);

            if (isNewRecord) {
                Short nuevoId = this.dataLibro.registerBook(libro);
                if (nuevoId != null) {
                    JOptionPane.showMessageDialog(this.libroForm, "Libro registrado exitosamente con ID: " + nuevoId, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this.libroForm, "No se pudo registrar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this.dataLibro.updateBook(libro);
                JOptionPane.showMessageDialog(this.libroForm, "Libro actualizado exitosamente.", "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);
            }

            this.libros = this.dataLibro.getAllBooks();
            this.LlenarTablaLibros();
            this.ClearFieldsData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.libroForm, "Error en la base de datos: " + ex.getMessage(), "Error de guardado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void DeleteData() {
        try {
            Short idLibro = Short.valueOf(this.libroForm.idField.getText());
            this.dataLibro.removeBook(idLibro);

            this.libros = this.dataLibro.getAllBooks();
            this.LlenarTablaLibros();
            this.ClearFieldsData();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("foreign key constraint fails")) {
                JOptionPane.showMessageDialog(libroForm, "No se puede eliminar el autor porque tiene libros asociados.\nElimine o reasigne esos libros primero.", "Error de integridad referencial", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.libroForm, "Ocurrió un error al eliminar el libro:\n" + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ApplyFilter() {
        String texto = this.libroForm.filtroField.getText().toLowerCase();

        if (texto.trim().isEmpty()) {
            this.libroForm.tablaSorter.setRowFilter(null);
        } else {
            this.libroForm.tablaSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    private void LlenarTablaLibros() {
        this.libroForm.tablaLibros.clearSelection();
        this.libroForm.modeloTabla.setRowCount(0); // Limpiar tabla

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
                libro.getCantidad(),
                libro.getRating(),
                libro.getAutorId()
            };
            this.libroForm.modeloTabla.addRow(fila);
        }
    }

    private void LlenarDetallesLibro(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = this.libroForm.tablaLibros.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = this.libroForm.tablaLibros.convertRowIndexToModel(selectedRow);   // indice a modelo
        int indice = obtenerIndiceAutorPorId(Short.valueOf(this.libroForm.modeloTabla.getValueAt(modelRow, 12).toString()));
        String estado = this.libroForm.modeloTabla.getValueAt(modelRow, 9).toString();

        this.libroForm.idField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 0).toString());
        this.libroForm.tituloField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 1).toString());
        this.libroForm.autorCombobox.setSelectedIndex(indice);
        this.libroForm.editorialField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 3).toString());
        this.libroForm.portadaField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 4).toString());
        this.libroForm.isbnField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 5).toString());
        this.libroForm.idiomaField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 6).toString());
        this.libroForm.generoField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 7).toString());
        this.libroForm.yearField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 8).toString());
        this.libroForm.cantidadField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 10).toString());
        this.libroForm.estadoCombobox.setSelectedItem(estado);
    }

    private void LlenarComboboxAutor() {
        this.libroForm.autorCombobox.removeAllItems();

        for (Autor autor : this.autores) {
            this.libroForm.autorCombobox.addItem(autor);
        }
    }

    private void LlenarComboboxEstado() {
        for (String estado : this.estados) {
            this.libroForm.estadoCombobox.addItem(estado);
        }
    }

    private void SeleccionarFotografia() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Imagen PNG o JPG");

        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                String nombre = f.getName().toLowerCase();
                return f.isDirectory() || nombre.endsWith(".png") || nombre.endsWith(".jpg") || nombre.endsWith(".jpeg");
            }

            @Override
            public String getDescription() {
                return "Imágenes PNG y JPG (*.png, *.jpg, *.jpeg)";
            }
        });

        int resultado = fileChooser.showOpenDialog(this.libroForm);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String rutaSeleccionada = fileChooser.getSelectedFile().getAbsolutePath();
            this.libroForm.portadaField.setText(rutaSeleccionada);
        }
    }

    private int obtenerIndiceAutorPorId(Short autorId) {
        for (int i = 0; i < this.libroForm.autorCombobox.getItemCount(); i++) {
            Autor autor = (Autor) this.libroForm.autorCombobox.getItemAt(i);
            if (autor.getId().equals(autorId)) {
                return i;
            }
        }
        return -1; // No encontrado
    }

    private float calcularPromedioRating(Short libroId) {
        List<Resena> reseñasDelLibro = this.resenas.stream()
                .filter(r -> r.getLibroId().equals(libroId))
                .collect(Collectors.toList());

        if (reseñasDelLibro.isEmpty()) {
            return 0.0f;
        }

        float total = 0;
        for (Resena r : reseñasDelLibro) {
            total += r.getRating();
        }

        return total / reseñasDelLibro.size();
    }
}
