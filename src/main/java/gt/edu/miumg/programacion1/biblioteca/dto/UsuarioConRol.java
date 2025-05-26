/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.dto;

import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import java.time.LocalDate;

/**
 *
 * @author willc
 */
public class UsuarioConRol {

    /**
     * @return the id
     */
    public Short getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Short id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the rolId
     */
    public Short getRolId() {
        return rolId;
    }

    /**
     * @param rolId the rolId to set
     */
    public void setRolId(Short rolId) {
        this.rolId = rolId;
    }

    /**
     * @return the fotografia
     */
    public String getFotografia() {
        return fotografia;
    }

    /**
     * @param fotografia the fotografia to set
     */
    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    /**
     * @return the fechaRegistro
     */
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * @param fechaRegistro the fechaRegistro to set
     */
    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @return the contrasena
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * @param contrasena the contrasena to set
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * @return the rolNombre
     */
    public String getRolNombre() {
        return rolNombre;
    }

    /**
     * @param rolNombre the rolNombre to set
     */
    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public UsuarioConRol(Short id, String name, String email, Short rolId, String fotografia, LocalDate fechaRegistro, String salt, String contrasena, String rolNombre) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.rolId = rolId;
        this.fotografia = fotografia;
        this.fechaRegistro = fechaRegistro;
        this.salt = salt;
        this.contrasena = contrasena;
        this.rolNombre = rolNombre;
    }

    public Usuario toUsuario() {
        return new Usuario(
                this.id,
                this.name,
                this.email,
                this.rolId,
                this.fotografia,
                this.fechaRegistro,
                this.salt,
                this.contrasena
        );
    }

    private Short id;
    private String name;
    private String email;
    private Short rolId;
    private String fotografia;
    private LocalDate fechaRegistro;
    private String salt;
    private String contrasena;
    private String rolNombre;
}
