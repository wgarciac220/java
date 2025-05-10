/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author wgarciac
 */
public class FileInitializer {

    public static void copyFromResourceIfNotExists(String resourcePath) throws IOException {
        Path absoluteResourcePath = Paths.get(resourcePath).toAbsolutePath();

        if (!Files.exists(absoluteResourcePath)) {
            Files.createDirectories(absoluteResourcePath.getParent());

            try (InputStream in = FileInitializer.class.getClassLoader().getResourceAsStream(resourcePath)) {
                if (in == null) {
                    throw new IOException("No se encontró el archivo de plantilla en resources/" + resourcePath);
                }

                Files.copy(in, absoluteResourcePath);
                System.out.println("Archivo copiado desde resources a: " + absoluteResourcePath);
            }
        }
    }
}
