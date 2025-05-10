/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.modelos;

import java.time.Year;

/**
 *
 * @author wgarciac
 */
public class Libro {

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
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the genero
     */
    public String getGenero() {
        return genero;
    }

    /**
     * @param genero the genero to set
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * @return the cantidad
     */
    public Short getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(Short cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the portada
     */
    public String getPortada() {
        return portada;
    }

    /**
     * @param portada the portada to set
     */
    public void setPortada(String portada) {
        this.portada = portada;
    }

    /**
     * @return the anoPublicacion
     */
    public Year getAnoPublicacion() {
        return anoPublicacion;
    }

    /**
     * @param anoPublicacion the anoPublicacion to set
     */
    public void setAnoPublicacion(Year anoPublicacion) {
        this.anoPublicacion = anoPublicacion;
    }

    /**
     * @return the editorial
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * @param editorial the editorial to set
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     * @return the idioma
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * @param idioma the idioma to set
     */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    /**
     * @return the rating
     */
    public Float getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(Float rating) {
        this.rating = rating;
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

    public Libro(Short id, String titulo, String isbn, String genero, Short cantidad, String portada, Year anoPublicacion, String editorial, String idioma, Float rating, Short autorId, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.genero = genero;
        this.cantidad = cantidad;
        this.portada = portada;
        this.anoPublicacion = anoPublicacion;
        this.editorial = editorial;
        this.idioma = idioma;
        this.rating = rating;
        this.autorId = autorId;
        this.estado = estado;
    }

    private Short id;
    private String titulo;
    private String isbn;
    private String genero;
    private Short cantidad;
    private String portada;
    private Year anoPublicacion;
    private String editorial;
    private String idioma;
    private Float rating;
    private Short autorId;
    private String estado;
}
