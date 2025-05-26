/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author wgarciac
 */
public class Password {

    // Generar un salt aleatorio de 16 bytes y codificarlo en Base64
    public static String generarSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String saltBase64) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(saltBase64);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(saltBytes);
            byte[] hashBytes = md.digest(password.getBytes("UTF-8"));

            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }

}
