/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.modelos;

/**
 *
 * @author wgarciac
 */
public class Resena {

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
     * @return the resena
     */
    public String getResena() {
        return resena;
    }

    /**
     * @param resena the resena to set
     */
    public void setResena(String resena) {
        this.resena = resena;
    }

    /**
     * @return the rating
     */
    public byte getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(byte rating) {
        this.rating = rating;
    }

    public Resena(Short id, Short usuarioId, Short libroId, String resena, byte rating) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.resena = resena;
        this.rating = rating;
    }

    private Short id;
    private Short usuarioId;
    private Short libroId;
    private String resena;
    private byte rating;
}
