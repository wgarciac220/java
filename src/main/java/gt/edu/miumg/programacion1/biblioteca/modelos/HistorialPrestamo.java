/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.modelos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author wgarciac
 */
public class HistorialPrestamo {

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
     * @return the libroId
     */
    public Short getLibroId() {
        return libroId;
    }

    /**
     * @param libroId the libroId to set
     */
    public void setLibroId(Short libroId) {
        this.libroId = libroId;
    }

    /**
     * @return the fechaPrestamo
     */
    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    /**
     * @param fechaPrestamo the fechaPrestamo to set
     */
    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
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

    /**
     * @return the fechaDevolucion
     */
    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    /**
     * @param fechaDevolucion the fechaDevolucion to set
     */
    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     * @return the multa
     */
    public BigDecimal getMulta() {
        return multa;
    }

    /**
     * @param multa the multa to set
     */
    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    public HistorialPrestamo(Short id, Short usuarioId, Short libroId, LocalDateTime fechaPrestamo, String estado, LocalDateTime fechaDevolucion, BigDecimal multa) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.fechaPrestamo = fechaPrestamo;
        this.estado = estado;
        this.fechaDevolucion = fechaDevolucion;
        this.multa = multa;
    }

    private Short id;
    private Short usuarioId;
    private Short libroId;
    private LocalDateTime fechaPrestamo;
    private String estado;
    private LocalDateTime fechaDevolucion;
    private BigDecimal multa;
}
