/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.json.RolData;
import gt.edu.miumg.programacion1.biblioteca.datasources.json.UsuarioData;
import gt.edu.miumg.programacion1.biblioteca.modelos.Rol;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import gt.edu.miumg.programacion1.biblioteca.util.Password;
import gt.edu.miumg.programacion1.biblioteca.vistas.UsuarioForm;
import java.io.IOException;
import java.time.LocalDate;
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
public class UsuarioController {

    private static final String DATA_USUARIOS = "data/usuarios.json";
    private static final String DATA_ROLES = "data/roles.json";
    private static final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String user = "root";
    private static final String password = "dimrnyW-9";

    private List<Usuario> usuarios;
    private List<Rol> roles;
    private UsuarioForm userForm;
    private UsuarioData data;

    public UsuarioController() {
        try {
            RolData rolData = new RolData(DATA_ROLES);
            roles = rolData.cargarRoles();

            data = new UsuarioData(DATA_USUARIOS);
            this.usuarios = data.cargarUsuarios();

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
        } catch (IOException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
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
        String userName = this.userForm.nameField.getText().trim();
        String userMail = this.userForm.emailField.getText().trim();
        String userPhoto = this.userForm.fotografiaField.getText().trim();
        String userPassword = this.userForm.passwordField.getText().trim();

        if (userName.isEmpty() || userMail.isEmpty()) {
            JOptionPane.showMessageDialog(this.userForm, "Por favor llena los campos obligatorios:\nNombre, Email y Contraseña.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean isNewRecord = this.userForm.idField.getText().isBlank();

        Short idUser;
        String salt;
        String passwordHash;
        LocalDate userRegisterOn;
        if (isNewRecord) {
            if (userPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this.userForm, "El campo Contraseña es obligatorio.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            idUser = (short) (this.usuarios.stream().mapToInt(usuario -> usuario.getId()).max().orElse(0) + 1);
            salt = Password.generarSalt();
            passwordHash = Password.hashPassword(userPassword, salt);
            userRegisterOn = LocalDate.now();
        } else {
            idUser = Short.valueOf(this.userForm.idField.getText());
            Usuario existingUser = this.usuarios.stream()
                    .filter(usuario -> usuario.getId().equals(idUser))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            userRegisterOn = existingUser.getFechaRegistro();

            if (userPassword.isEmpty()) {
                salt = existingUser.getSalt();
                passwordHash = existingUser.getContrasena();
            } else {
                salt = Password.generarSalt();
                passwordHash = Password.hashPassword(userPassword, salt);
            }
        }

        Short idRol = roles.stream()
                .filter(role -> role.getNombreRol().equals(this.userForm.puestoComboBox.getSelectedItem()))
                .map(Rol::getId)
                .findFirst()
                .orElse((short) 0);

        Usuario usuario = new Usuario(idUser, userName, userMail, idRol, userPhoto, userRegisterOn, salt, passwordHash);

        if (isNewRecord) {
            this.usuarios.add(usuario);
        } else {
            this.usuarios = this.usuarios.stream()
                    .map(u -> u.getId().equals(usuario.getId()) ? usuario : u)
                    .collect(Collectors.toList());

        }

        ClearFieldsData();
        data.guardarUsuarios(this.usuarios);
        this.LlenarTablaUsuarios();
    }

    private void DeleteData() {
        Short idUser = Short.valueOf(this.userForm.idField.getText());
        this.usuarios = this.usuarios.stream()
                .filter(usuario -> !Objects.equals(usuario.getId(), idUser))
                .collect(Collectors.toList());

        this.ClearFieldsData();
        data.guardarUsuarios(this.usuarios);
        this.LlenarTablaUsuarios();
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
            this.userForm.puestoComboBox.addItem(rol.getNombreRol());
        }
    }

    private void LlenarTablaUsuarios() {
        this.userForm.tablaUsuarios.clearSelection();
        this.userForm.modeloTabla.setRowCount(0); // Limpiar tabla

        for (Usuario usuario : this.usuarios) {
            Object[] fila = {
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                usuario.getFotografia(),
                getNombreRolById(usuario.getRolId())
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

        this.userForm.idField.setText(this.userForm.modeloTabla.getValueAt(modelRow, 0).toString());
        this.userForm.nameField.setText(this.userForm.modeloTabla.getValueAt(modelRow, 1).toString());
        this.userForm.emailField.setText(this.userForm.modeloTabla.getValueAt(modelRow, 2).toString());
        this.userForm.fotografiaField.setText(this.userForm.modeloTabla.getValueAt(modelRow, 3).toString());
        this.userForm.passwordField.setText("");

        String nombreRol = this.userForm.modeloTabla.getValueAt(modelRow, 4).toString();
        this.userForm.puestoComboBox.setSelectedItem(nombreRol);
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

    private String getNombreRolById(Short id) {
        for (Rol rol : this.roles) {
            if (rol.getId().equals(id)) {
                return rol.getNombreRol();
            }
        }
        return null; // Si no se encuentra
    }
}
