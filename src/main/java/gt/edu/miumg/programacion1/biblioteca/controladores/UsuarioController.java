/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.IRolData;
import gt.edu.miumg.programacion1.biblioteca.datasources.IUsuarioData;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.RolDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.UsuarioDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.dto.UsuarioConRol;
import gt.edu.miumg.programacion1.biblioteca.modelos.Rol;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import gt.edu.miumg.programacion1.biblioteca.util.Password;
import gt.edu.miumg.programacion1.biblioteca.vistas.UsuarioForm;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author wgarciac
 */
public class UsuarioController {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "dimrnyW-9";

    private List<UsuarioConRol> usuarios;
    private List<Rol> roles;
    private UsuarioForm userForm;
    private IUsuarioData dataUsuario;

    public UsuarioController() throws SQLException {
        try {
            IRolData rolData = new RolDataMySQL(URL, USER, PASSWORD);
            this.roles = rolData.getAllRoles();
            this.dataUsuario = new UsuarioDataMySQL(URL, USER, PASSWORD);
            this.usuarios = this.dataUsuario.getAllUsers();

            this.userForm = new UsuarioForm(roles);
            this.userForm.fotografiaField.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    UsuarioController.this.SeleccionarFotografia();
                }
            });
            this.userForm.nuevoBtn.addActionListener(e -> this.ClearFieldsData());
            this.userForm.guardarBtn.addActionListener(e -> this.SaveData());
            this.userForm.eliminarBtn.addActionListener(e -> this.DeleteData());
            this.userForm.aplicarFiltroBtn.addActionListener(e -> this.ApplyFilter());
            this.userForm.tablaUsuarios.getSelectionModel().addListSelectionListener(e -> this.LlenarDetallesUsuarios(e));

            this.LlenarComboboxRol();
            this.LlenarTablaUsuarios();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.userForm, "Ocurrió un error al cargar los usuarios:\n" + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    public UsuarioForm Mostrar() {
        return this.userForm;
    }

    private void ClearFieldsData() {
        this.userForm.idField.setText("");
        this.userForm.nameField.setText("");
        this.userForm.emailField.setText("");
        this.userForm.puestoComboBox.setSelectedIndex(0);
        this.userForm.fotografiaField.setText("");
        this.userForm.passwordField.setText("");
    }

    private void SaveData() {
        try {
            Rol rolSeleccionado = (Rol) this.userForm.puestoComboBox.getSelectedItem();

            String userName = this.userForm.nameField.getText().trim();
            Short idRol = rolSeleccionado.getId();
            String userMail = this.userForm.emailField.getText().trim();
            String userPhoto = this.userForm.fotografiaField.getText().trim();
            String userPassword = this.userForm.passwordField.getText().trim();

            if (userName.isEmpty() || userMail.isEmpty()) {
                JOptionPane.showMessageDialog(this.userForm, "Por favor llena los campos obligatorios:\nNombre, Email y Contraseña.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean isNewRecord = this.userForm.idField.getText().isBlank();
            Short idUser = isNewRecord ? null : Short.valueOf(this.userForm.idField.getText());

            String salt;
            String passwordHash;
            LocalDate userRegisterOn;
            if (isNewRecord) {
                if (userPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this.userForm, "El campo Contraseña es obligatorio.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                salt = Password.generarSalt();
                passwordHash = Password.hashPassword(userPassword, salt);
                userRegisterOn = LocalDate.now();
            } else {
                UsuarioConRol existingUser = this.usuarios.stream()
                        .filter(usuario -> usuario.getId().equals(idUser))
                        .findFirst()
                        .orElse(null);

                userRegisterOn = existingUser.getFechaRegistro();

                if (userPassword.isEmpty()) {
                    salt = existingUser.getSalt();
                    passwordHash = existingUser.getContrasena();
                } else {
                    salt = Password.generarSalt();
                    passwordHash = Password.hashPassword(userPassword, salt);
                }
            }

            Usuario usuario = new Usuario(idUser, userName, userMail, idRol, userPhoto, userRegisterOn, salt, passwordHash);

            if (isNewRecord) {
                Short nuevoId = this.dataUsuario.registerUser(usuario);
                if (nuevoId != null) {
                    JOptionPane.showMessageDialog(this.userForm, "Usuario registrado exitosamente con ID: " + nuevoId, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this.userForm, "No se pudo registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this.dataUsuario.updateUser(usuario);
                JOptionPane.showMessageDialog(this.userForm, "Usuario actualizado exitosamente.", "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);

            }

            this.usuarios = this.dataUsuario.getAllUsers();
            this.LlenarTablaUsuarios();
            ClearFieldsData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.userForm, "Error en la base de datos: " + ex.getMessage(), "Error de guardado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void DeleteData() {
        try {
            Short idUser = Short.valueOf(this.userForm.idField.getText());
            this.dataUsuario.removeUser(idUser);

            this.usuarios = this.dataUsuario.getAllUsers();
            this.LlenarTablaUsuarios();
            this.ClearFieldsData();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("foreign key constraint fails")) {
                JOptionPane.showMessageDialog(userForm, "No se puede eliminar el usuario porque tiene libros y reseñas asociados.\nElimine o reasigne esos libros/reseñas primero.", "Error de integridad referencial", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.userForm, "Ocurrió un error al eliminar el libro:\n" + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ApplyFilter() {
        String texto = this.userForm.filtroField.getText().toLowerCase();

        if (texto.trim().isEmpty()) {
            this.userForm.tablaSorter.setRowFilter(null);
        } else {
            this.userForm.tablaSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    private void LlenarComboboxRol() {
        for (Rol rol : this.roles) {
            this.userForm.puestoComboBox.addItem(rol);
        }
    }

    private void LlenarTablaUsuarios() {
        this.userForm.tablaUsuarios.clearSelection();
        this.userForm.modeloTabla.setRowCount(0); // Limpiar tabla

        for (UsuarioConRol usuario : this.usuarios) {
            Object[] fila = {
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                usuario.getFotografia(),
                usuario.getRolNombre(),
                usuario.getRolId()
            };
            this.userForm.modeloTabla.addRow(fila);
        }
    }

    private void LlenarDetallesUsuarios(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = this.userForm.tablaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        int modelRow = this.userForm.tablaUsuarios.convertRowIndexToModel(selectedRow);   // indice a modelo
        int indice = obtenerIndiceRolPorId(Short.valueOf(this.userForm.modeloTabla.getValueAt(modelRow, 5).toString()));

        this.userForm.idField.setText(this.userForm.modeloTabla.getValueAt(modelRow, 0).toString());
        this.userForm.nameField.setText(this.userForm.modeloTabla.getValueAt(modelRow, 1).toString());
        this.userForm.emailField.setText(this.userForm.modeloTabla.getValueAt(modelRow, 2).toString());
        this.userForm.fotografiaField.setText(this.userForm.modeloTabla.getValueAt(modelRow, 3).toString());
        this.userForm.puestoComboBox.setSelectedIndex(indice);
        this.userForm.passwordField.setText("");

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

        int resultado = fileChooser.showOpenDialog(this.userForm);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String rutaSeleccionada = fileChooser.getSelectedFile().getAbsolutePath();
            this.userForm.fotografiaField.setText(rutaSeleccionada);
        }
    }

    private int obtenerIndiceRolPorId(Short rolId) {
        for (int i = 0; i < this.userForm.puestoComboBox.getItemCount(); i++) {
            Rol rol = (Rol) this.userForm.puestoComboBox.getItemAt(i);
            if (rol.getId().equals(rolId)) {
                return i;
            }
        }
        return -1; // No encontrado
    }
}
