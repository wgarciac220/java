/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gt.edu.miumg.programacion1.biblioteca.modelos.RecordarUsuario;
import gt.edu.miumg.programacion1.biblioteca.util.FileInitializer;
import gt.edu.miumg.programacion1.biblioteca.util.LocalDateAdapter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 *
 * @author wgarciac
 */
public class RecordarUsuarioData {

    private final Gson gson;
    private final String sourceLocation;
    private final Path sourcePath;

    public RecordarUsuarioData(String sourceLocation) throws IOException {
        this.sourceLocation = sourceLocation;
        this.sourcePath = Paths.get(this.sourceLocation);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // 🔥 importante
                .setPrettyPrinting()
                .create();

        FileInitializer.copyFromResourceIfNotExists(this.sourceLocation);
        System.out.println(this.sourcePath);
    }

    public RecordarUsuario cargar() {
        try (Reader reader = Files.newBufferedReader(sourcePath)) {
            return gson.fromJson(reader, RecordarUsuario.class);
        } catch (IOException e) {
            return new RecordarUsuario("", false);
        }
    }

    public void guardar(RecordarUsuario recordatorio) {
        try (Writer writer = Files.newBufferedWriter(sourcePath)) {
            gson.toJson(recordatorio, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void borrar() {
        try (Writer writer = Files.newBufferedWriter(sourcePath)) {
            writer.write("{}"); // JSON vacío válido
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
