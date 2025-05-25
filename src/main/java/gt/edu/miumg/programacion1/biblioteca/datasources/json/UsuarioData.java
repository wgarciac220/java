/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gt.edu.miumg.programacion1.biblioteca.modelos.Usuario;
import gt.edu.miumg.programacion1.biblioteca.util.FileInitializer;
import gt.edu.miumg.programacion1.biblioteca.util.LocalDateAdapter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author wgarciac
 */
public class UsuarioData {

    private final Gson gson;
    private final String sourceLocation;
    private final Path sourcePath;

    public UsuarioData(String sourceLocation) throws IOException {
        this.sourceLocation = sourceLocation;
        this.sourcePath = Paths.get(this.sourceLocation);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // 🔥 importante
                .setPrettyPrinting()
                .create();

        FileInitializer.copyFromResourceIfNotExists(this.sourceLocation);
        System.out.println(this.sourceLocation);
    }

    public List<Usuario> cargarUsuarios() {
        try (Reader reader = Files.newBufferedReader(this.sourcePath)) {
            Type listType = new TypeToken<List<Usuario>>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (Exception e) {
            return List.of();
        }
    }

    public void guardarUsuarios(List<Usuario> usuarios) {
        try (Writer writer = Files.newBufferedWriter(this.sourcePath)) {
            this.gson.toJson(usuarios, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
