/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.datasources.json.RecordarUsuarioData;
import gt.edu.miumg.programacion1.biblioteca.datasources.json.UsuarioData;
import gt.edu.miumg.programacion1.biblioteca.modelos.RecordarUsuario;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import gt.edu.miumg.programacion1.biblioteca.util.Password;
import gt.edu.miumg.programacion1.biblioteca.vistas.LoginForm;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author wgarciac
 */
public class LoginController {

    private static final String DATA_USUARIOS = "data/usuarios.json";
    private static final String DATA_RECORDAR_USUARIO = "data/recordar_usuario.json";
    private static final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String user = "root";
    private static final String password = "dimrnyW-9";

    private LoginForm loginForm;
    private UsuarioData data;
    private List<Usuario> usuarios;

    private RecordarUsuarioData recordarData;

    public LoginController() {

        try {
            data = new UsuarioData(DATA_USUARIOS);
            usuarios = data.cargarUsuarios();
            recordarData = new RecordarUsuarioData(DATA_RECORDAR_USUARIO);

            loginForm = new LoginForm();
            RecordarUsuario recordado = recordarData.cargar();
            if (recordado.isRecordar()) {
                loginForm.usernameField.setText(recordado.getEmail());
                loginForm.recordarUsuario.setSelected(true);
            }

            loginForm.cancelButton.addActionListener(e -> loginForm.dispose());
            loginForm.submitButton.addActionListener(e -> validarCredenciales());
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Mostrar() {
        loginForm.setVisible(true);
    }

    private void validarCredenciales() {
        String email = loginForm.usernameField.getText();
        String password = new String(loginForm.passwordField.getPassword());

        Usuario usuarioEncontrado = getUsuarios().stream().
                filter(usuario -> usuario.getEmail().equalsIgnoreCase(email)).
                findFirst().
                orElse(null);

        if (usuarioEncontrado != null) {
            String salt = usuarioEncontrado.getSalt();
            String hashAlmacenado = usuarioEncontrado.getContrasena();
            String hashCalculado = Password.hashPassword(password, salt);

            if (hashCalculado.equals(hashAlmacenado)) {
                if (loginForm.recordarUsuario.isSelected()) {
                    recordarData.guardar(new RecordarUsuario(email, true));
                } else {
                    recordarData.borrar();
                }
                JOptionPane.showMessageDialog(loginForm, "Bienvenido " + usuarioEncontrado.getName(), "Acceso correcto", JOptionPane.INFORMATION_MESSAGE);
                loginForm.dispose();

                try {
                    MenuController controller = new MenuController();
                    controller.setUsuario(usuarioEncontrado);
                    controller.Mostrar();

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
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
