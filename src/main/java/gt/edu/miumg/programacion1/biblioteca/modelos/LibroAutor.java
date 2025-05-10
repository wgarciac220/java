/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.modelos;

/**
 *
 * @author wgarciac
 */
public class LibroAutor {

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
     * @return the autorId
     */
    public Short getAutorId() {
        return autorId;
    }

    /**
     * @param autorId the autorId to set
     */
    public void setAutorId(Short autorId) {
        this.autorId = autorId;
    }

    public LibroAutor(Short id, Short libroId, Short autorId) {
        this.id = id;
        this.libroId = libroId;
        this.autorId = autorId;
    }

    private Short id;
    private Short libroId;
    private Short autorId;
}
