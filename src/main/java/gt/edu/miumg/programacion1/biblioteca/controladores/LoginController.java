/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.IRecordarData;
import gt.edu.miumg.programacion1.biblioteca.datasources.IUsuarioData;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.RecordarUsuarioDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.datasources.mysql.UsuarioDataMySQL;
import gt.edu.miumg.programacion1.biblioteca.dto.UsuarioConRol;
import gt.edu.miumg.programacion1.biblioteca.modelos.RecordarUsuario;
import gt.edu.miumg.programacion1.biblioteca.util.Password;
import gt.edu.miumg.programacion1.biblioteca.vistas.LoginForm;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author wgarciac
 */
public class LoginController {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "dimrnyW-9";

    private LoginForm loginForm;
    private IUsuarioData dataUsuario;
    private List<UsuarioConRol> usuarios;

    private IRecordarData recordarData;

    public LoginController() {

        try {
            dataUsuario = new UsuarioDataMySQL(URL, USER, PASSWORD);
            usuarios = dataUsuario.getAllUsers();
            recordarData = new RecordarUsuarioDataMySQL(URL, USER, PASSWORD);

            loginForm = new LoginForm();
            RecordarUsuario recordado = recordarData.getRememberedUser();
            if (recordado != null && recordado.isRecordar()) {
                loginForm.usernameField.setText(recordado.getEmail());
                loginForm.recordarUsuario.setSelected(true);
            }

            loginForm.cancelButton.addActionListener(e -> loginForm.dispose());
            loginForm.submitButton.addActionListener(e -> validarCredenciales());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.loginForm, "Ocurrió un error al cargar los usuarios:\n" + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void Mostrar() {
        loginForm.setVisible(true);
    }

    private void validarCredenciales() {
        String email = loginForm.usernameField.getText();
        String password = new String(loginForm.passwordField.getPassword());

        UsuarioConRol usuarioEncontrado = getUsuarios().stream().
                filter(usuario -> usuario.getEmail().equalsIgnoreCase(email)).
                findFirst().
                orElse(null);

        if (usuarioEncontrado != null) {
            String salt = usuarioEncontrado.getSalt();
            String hashAlmacenado = usuarioEncontrado.getContrasena();
            String hashCalculado = Password.hashPassword(password, salt);

            if (hashCalculado.equals(hashAlmacenado)) {
                try {
                    if (loginForm.recordarUsuario.isSelected()) {
                        recordarData.registerRememberedUser(new RecordarUsuario(email, true));
                    } else {
                        recordarData.removeRememberedUser();
                    }

                    JOptionPane.showMessageDialog(loginForm, "Bienvenido " + usuarioEncontrado.getName(), "Acceso correcto", JOptionPane.INFORMATION_MESSAGE);
                    loginForm.dispose();

                    MenuController controller = new MenuController();
                    controller.setUsuario(usuarioEncontrado);
                    controller.Mostrar();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this.loginForm, "Ocurrió un error al cargar los datos de usuario:\n" + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al abrir el sistema: " + e.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(loginForm, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(loginForm, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return the usuarios
     */
    public List<UsuarioConRol> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<UsuarioConRol> usuarios) {
        this.usuarios = usuarios;
    }
}
