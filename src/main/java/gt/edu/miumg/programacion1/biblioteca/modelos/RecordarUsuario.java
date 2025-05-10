/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.modelos;

/**
 *
 * @author wgarciac
 */
public class RecordarUsuario {

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
     * @return the recordar
     */
    public boolean isRecordar() {
        return recordar;
    }

    /**
     * @param recordar the recordar to set
     */
    public void setRecordar(boolean recordar) {
        this.recordar = recordar;
    }

    public RecordarUsuario(String email, boolean recordar) {
        this.email = email;
        this.recordar = recordar;
    }

    private String email;
    private boolean recordar;

}
