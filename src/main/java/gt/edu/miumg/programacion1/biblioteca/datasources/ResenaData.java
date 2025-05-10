/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.datasources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gt.edu.miumg.programacion1.biblioteca.modelos.Resena;
import gt.edu.miumg.programacion1.biblioteca.util.FileInitializer;
import gt.edu.miumg.programacion1.biblioteca.util.LocalDateTimeAdapter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author wgarciac
 */
public class ResenaData {

    private final Gson gson;
    private final String sourceLocation;
    private final Path sourcePath;

    public ResenaData(String sourceLocation) throws IOException {

        this.sourceLocation = sourceLocation;
        this.sourcePath = Paths.get(this.sourceLocation);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()) // 🔥 importante
                .setPrettyPrinting()
                .create();

        FileInitializer.copyFromResourceIfNotExists(this.sourceLocation);
        System.out.println(this.sourcePath);
    }

    public List<Resena> cargaResenas() {
        try (Reader reader = Files.newBufferedReader(this.sourcePath)) {
            Type listType = new TypeToken<List<Resena>>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (Exception e) {
            System.out.println(e);
            return List.of();
        }
    }

    public void guardarResenas(List<Resena> resenas) {
        try (Writer writer = Files.newBufferedWriter(this.sourcePath)) {
            this.gson.toJson(resenas, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
