/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.modelos;

import java.time.LocalDateTime;

/**
 *
 * @author wgarciac
 */
public class Sesion {

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
     * @return the usuarioId
     */
    public Short getUsuarioId() {
        return usuarioId;
    }

    /**
     * @param usuarioId the usuarioId to set
     */
    public void setUsuarioId(Short usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * @return the fechaInicio
     */
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFinal
     */
    public LocalDateTime getFechaFinal() {
        return fechaFinal;
    }

    /**
     * @param fechaFinal the fechaFinal to set
     */
    public void setFechaFinal(LocalDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Sesion(Short id, Short usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFinal, String estado) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.estado = estado;
    }

    private Short id;
    private Short usuarioId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;
    private String estado;
}
