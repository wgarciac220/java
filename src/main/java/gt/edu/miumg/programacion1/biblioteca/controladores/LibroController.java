/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.AutorData;
import gt.edu.miumg.programacion1.biblioteca.datasources.LibroData;
import gt.edu.miumg.programacion1.biblioteca.datasources.ResenaData;
import gt.edu.miumg.programacion1.biblioteca.modelos.Autor;
import gt.edu.miumg.programacion1.biblioteca.modelos.Libro;
import gt.edu.miumg.programacion1.biblioteca.modelos.Resena;
import gt.edu.miumg.programacion1.biblioteca.vistas.LibroForm;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final String DATA_LIBROS = "data/libros.json";
    private static final String DATA_AUTORES = "data/autores.json";
    private static final String DATA_RESENAS = "data/resenas.json";

    private LibroForm libroForm;

    private LibroData data;
    private List<Libro> libros;

    private AutorData dataAutor;
    private List<Autor> autores;

    private ResenaData resenaData;
    private List<Resena> resenas;

    private List<String> estados;

    public LibroController() {
        try {
//            libroForm = new LibroForm();

            this.data = new LibroData(DATA_LIBROS);
            this.libros = data.cargarLibros();
            this.dataAutor = new AutorData(DATA_AUTORES);
            this.autores = dataAutor.cargarAutores();
            this.resenaData = new ResenaData(DATA_RESENAS);
            this.resenas = resenaData.cargaResenas();

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
        } catch (IOException ex) {
            Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public LibroForm Mostrar() {
        return libroForm;
    }

    private void ClearFieldsData() {
        this.libroForm.idField.setText("");
        this.libroForm.tituloField.setText("");
        this.libroForm.autorCombobox.setSelectedIndex(0);
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
        String titulo = this.libroForm.tituloField.getText().trim();
        String autor = this.libroForm.autorCombobox.getSelectedItem().toString();
        String editorial = this.libroForm.editorialField.getText().trim();
        String portada = this.libroForm.portadaField.getText().trim();
        String isbn = this.libroForm.isbnField.getText().trim();
        String idioma = this.libroForm.idiomaField.getText().trim();
        String genero = this.libroForm.generoField.getText().trim();
        String year = this.libroForm.yearField.getText().trim();
        String estado = this.libroForm.estadoCombobox.getSelectedItem().toString();
        String cantidad = this.libroForm.cantidadField.getText().trim();

        String autorNombre = this.libroForm.autorCombobox.getSelectedItem().toString();

        if (titulo.isEmpty() || autor.isEmpty() || editorial.isEmpty() || isbn.isEmpty() || idioma.isEmpty() || genero.isEmpty() || year.isEmpty() || estado.isEmpty() || cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(this.libroForm, "Por favor llena los campos obligatorios:\nTitulo, Autor, Editorial, ISBN, Idioma, Genero, Publicacion, Estado y Cantidad.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean isNewRecord = this.libroForm.idField.getText().isBlank();

        Short idLibro;
        if (isNewRecord) {
            idLibro = (short) (this.libros.stream().mapToInt(libro -> libro.getId()).max().orElse(0) + 1);
        } else {
            idLibro = Short.valueOf(this.libroForm.idField.getText());
        }

        Short idAutor = autores.stream()
                .filter(a -> a.getNombre().equals(autorNombre))
                .map(Autor::getId)
                .findFirst()
                .orElse((short) 0);

        Float rating = 0.0f;
        Short cantidadLibros = Short.valueOf(cantidad);
        Year anoPublicacion = Year.of(Integer.parseInt(year));

        Libro libro = new Libro(idLibro, titulo, isbn, genero, cantidadLibros, portada, anoPublicacion, editorial, idioma, rating, idAutor, estado);

        if (isNewRecord) {
            this.libros.add(libro);
        } else {
            Libro existingLibro = this.libros.stream()
                    .filter(l -> l.getId().equals(idLibro))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            libro.setRating(existingLibro.getRating());

            this.libros = this.libros.stream()
                    .map(l -> l.getId().equals(libro.getId()) ? libro : l)
                    .collect(Collectors.toList());
        }

        this.ClearFieldsData();
        data.guardarLibros(this.libros);
        this.LlenarTablaLibros();
    }

    private void DeleteData() {
        Short idLibro = Short.valueOf(this.libroForm.idField.getText());
        this.libros = this.libros.stream()
                .filter(libro -> !Objects.equals(libro.getId(), idLibro))
                .collect(Collectors.toList());

        this.ClearFieldsData();
        data.guardarLibros(this.libros);
        this.LlenarTablaLibros();
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

        for (Libro libro : this.libros) {
            float ratingPromedio = calcularPromedioRating(libro.getId());
            libro.setRating(ratingPromedio);

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
                libro.getCantidad(),
                libro.getRating()
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

        this.libroForm.idField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 0).toString());
        this.libroForm.tituloField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 1).toString());
        this.libroForm.editorialField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 3).toString());
        this.libroForm.portadaField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 4).toString());
        this.libroForm.isbnField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 5).toString());
        this.libroForm.idiomaField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 6).toString());
        this.libroForm.generoField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 7).toString());
        this.libroForm.yearField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 8).toString());
        this.libroForm.cantidadField.setText(this.libroForm.modeloTabla.getValueAt(modelRow, 10).toString());

        String nombreAutor = this.libroForm.modeloTabla.getValueAt(modelRow, 2).toString();
        this.libroForm.autorCombobox.setSelectedItem(nombreAutor);
        String estado = this.libroForm.modeloTabla.getValueAt(modelRow, 9).toString();
        this.libroForm.estadoCombobox.setSelectedItem(estado);
    }

    private void LlenarComboboxAutor() {
        for (Autor autor : this.autores) {
            this.libroForm.autorCombobox.addItem(autor.getNombre());
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

    private String getNombreAutorById(Short id) {
        for (Autor autor : this.autores) {
            if (autor.getId().equals(id)) {
                return autor.getNombre();
            }
        }
        return null; // Si no se encuentra
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
