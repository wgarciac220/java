/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.controladores;

import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import gt.edu.miumg.programacion1.biblioteca.vistas.MenuForm;
import gt.edu.miumg.programacion1.biblioteca.vistas.UsuarioForm;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author wgarciac
 */
public class MenuController {

    private static final String DATA_USUARIOS = "data/usuarios.json";
    private static final String DATA_ROLES = "data/roles.json";
    private static final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String user = "root";
    private static final String password = "dimrnyW-9";

    private List<Usuario> usuarios;
    private MenuForm menuForm;
    private UsuarioForm userForm;
    private Usuario usuarioLogueado;

    private static final Map<Short, String> ROLES = Map.of(
            (short) 1, "Administrador",
            (short) 2, "Bibliotecario",
            (short) 3, "Miembro"
    );

    public void setUsuario(Usuario usuario) {
        this.usuarioLogueado = usuario;
        aplicarRestriccionesPorRol();
        mostrarBienvenida();
    }

    public MenuController() throws IOException {
        menuForm = new MenuForm();
        menuForm.usuarioButton.addActionListener(e -> {
            UsuarioController usuarioController = new UsuarioController();
            menuForm.mostrarPanelEnSplash(usuarioController.Mostrar());
        });
        menuForm.libroButton.addActionListener(e -> {
            LibroController libroController = new LibroController();
            menuForm.mostrarPanelEnSplash(libroController.Mostrar());
        });
        menuForm.autorButton.addActionListener(e -> {
            AutorController autorController = new AutorController();
            menuForm.mostrarPanelEnSplash(autorController.Mostrar());
        });
        menuForm.prestarButton.addActionListener(e -> {
            PrestarContoller prestarContoller = new PrestarContoller();
            menuForm.mostrarPanelEnSplash(prestarContoller.Mostrar());
        });
        menuForm.devolverButton.addActionListener(e -> {
            DevolverController devolverController = new DevolverController(usuarioLogueado);
            menuForm.mostrarPanelEnSplash(devolverController.Mostrar());
        });
    }

    public void Mostrar() {
        menuForm.setVisible(true);
    }

    private void mostrarBienvenida() {
        if (usuarioLogueado != null) {
            JPanel panel = new JPanel(new GridBagLayout()); // Centrado absoluto
            String rolNombre = obtenerNombreRol(usuarioLogueado.getRolId());

            JLabel mensaje = new JLabel(
                    "<html><div style='text-align: center;'>¡Bienvenido, "
                    + usuarioLogueado.getName() + "<br>(" + rolNombre + ")</div></html>"
            );
            mensaje.setFont(new Font("Nunito", Font.BOLD, 28));

            panel.add(mensaje); // Por defecto, GridBagLayout lo centra

            menuForm.mostrarPanelEnSplash(panel);
        }
    }

    private void aplicarRestriccionesPorRol() {
        if (usuarioLogueado == null) {
            return;
        }

        short rolId = usuarioLogueado.getRolId();

        switch (rolId) {
            case 1: // Admin
                break;
            case 2: // Bibliotecario
                menuForm.usuarioButton.setVisible(false);
                menuForm.devolverButton.setVisible(false);
                break;
            case 3: // Miembro
                menuForm.usuarioButton.setVisible(false);
                menuForm.autorButton.setVisible(false);
                menuForm.libroButton.setVisible(false);
                menuForm.prestarButton.setVisible(false);
                break;
            default:
                menuForm.usuarioButton.setVisible(false);
                menuForm.libroButton.setVisible(false);
                menuForm.autorButton.setVisible(false);
        }
    }

    private String obtenerNombreRol(short rolId) {
        return ROLES.getOrDefault(rolId, "Rol desconocido");
    }
}
