/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.AutorData;
import gt.edu.miumg.programacion1.biblioteca.modelos.Autor;
import gt.edu.miumg.programacion1.biblioteca.vistas.AutorForm;
import java.io.IOException;
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
public class AutorController {

    private AutorForm autorForm;

    private AutorData data;
    private List<Autor> autores;
    private static final String DATA_AUTORES = "data/autores.json";

    public AutorController() {

        try {
            this.data = new AutorData(DATA_AUTORES);
            this.autores = data.cargarAutores();

            this.autorForm = new AutorForm();
            this.autorForm.fotografiaField.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    AutorController.this.SeleccionarFotografia();
                }
            });
            this.autorForm.nuevoBtn.addActionListener(e -> this.ClearFieldsData());
            this.autorForm.guardarBtn.addActionListener(e -> this.SaveData());
            this.autorForm.eliminarBtn.addActionListener(e -> this.DeleteData());
            this.autorForm.aplicarFiltroBtn.addActionListener(e -> this.ApplyFilter());
            this.autorForm.tablaAutores.getSelectionModel().addListSelectionListener(e -> this.LlenarDetallesAutor(e));

            this.LlenarTablaAutores();

        } catch (IOException ex) {
            Logger.getLogger(AutorController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public AutorForm Mostrar() {
        return this.autorForm;
    }

    private void ClearFieldsData() {
        this.autorForm.idField.setText("");
        this.autorForm.nombreField.setText("");
        this.autorForm.paisField.setText("");
        this.autorForm.fotografiaField.setText("");
        this.autorForm.biografiaField.setText("");
    }

    private void SaveData() {

        String nombre = this.autorForm.nombreField.getText().trim();
        String pais = this.autorForm.paisField.getText().trim();
        String fotografia = this.autorForm.fotografiaField.getText().trim();
        String biografia = this.autorForm.biografiaField.getText().trim();

        if (nombre.isEmpty() || pais.isEmpty() || biografia.isEmpty()) {
            JOptionPane.showMessageDialog(this.autorForm, "Por favor llena los campos obligatorios:\nNombre, Pais y Biografia.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean isNewRecord = this.autorForm.idField.getText().isBlank();

        Short idAutor;
        if (isNewRecord) {
            idAutor = (short) (this.autores.stream().mapToInt(autor -> autor.getId()).max().orElse(0) + 1);
        } else {
            idAutor = Short.valueOf(this.autorForm.idField.getText().trim());
        }

        Autor autor = new Autor(idAutor, nombre, biografia, pais, fotografia);

        if (isNewRecord) {
            this.autores.add(autor);
        } else {
            this.autores = this.autores.stream()
                    .map(a -> a.getId().equals(autor.getId()) ? autor : a)
                    .collect(Collectors.toList());
        }

        this.ClearFieldsData();
        data.guardarAutores(this.autores);
        this.LlenarTablaAutores();
    }

    private void DeleteData() {
        Short idAutor = Short.valueOf(this.autorForm.idField.getText());
        this.autores = this.autores.stream()
                .filter(autor -> !Objects.equals(autor.getId(), idAutor))
                .collect(Collectors.toList());

        this.ClearFieldsData();
        data.guardarAutores(this.autores);
        this.LlenarTablaAutores();
    }

    private void ApplyFilter() {
        String texto = this.autorForm.filtroField.getText().toLowerCase();

        if (texto.trim().isEmpty()) {
            this.autorForm.tablaSorter.setRowFilter(null);
        } else {
            this.autorForm.tablaSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    private void LlenarDetallesAutor(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = this.autorForm.tablaAutores.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = this.autorForm.tablaAutores.convertRowIndexToModel(selectedRow);   // indice a modelo

        this.autorForm.idField.setText(this.autorForm.modeloTabla.getValueAt(modelRow, 0).toString());
        this.autorForm.nombreField.setText(this.autorForm.modeloTabla.getValueAt(modelRow, 1).toString());
        this.autorForm.paisField.setText(this.autorForm.modeloTabla.getValueAt(modelRow, 2).toString());
        this.autorForm.fotografiaField.setText(this.autorForm.modeloTabla.getValueAt(modelRow, 3).toString());
        this.autorForm.biografiaField.setText(this.autorForm.modeloTabla.getValueAt(modelRow, 4).toString());
    }

    private void LlenarTablaAutores() {
        this.autorForm.tablaAutores.clearSelection();
        this.autorForm.modeloTabla.setRowCount(0); // Limpiar tabla

        for (Autor autor : this.autores) {
            Object[] fila = {
                autor.getId(),
                autor.getNombre(),
                autor.getPaisOrigen(),
                autor.getFotografia(),
                autor.getBiografia()
            };
            this.autorForm.modeloTabla.addRow(fila);
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

        int resultado = fileChooser.showOpenDialog(this.autorForm);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String rutaSeleccionada = fileChooser.getSelectedFile().getAbsolutePath();
            this.autorForm.fotografiaField.setText(rutaSeleccionada);
        }
    }

}
