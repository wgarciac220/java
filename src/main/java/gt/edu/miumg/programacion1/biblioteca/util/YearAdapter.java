/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Year;

/**
 *
 * @author wgarciac
 */
public class YearAdapter extends TypeAdapter<Year> {

    @Override
    public void write(JsonWriter out, Year value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getValue()); // Escribe como entero: 2025
        }
    }

    @Override
    public Year read(JsonReader in) throws IOException {
        if (in.peek().name().equals("NULL")) {
            in.nextNull();
            return null;
        }
        int yearValue = in.nextInt();
        return Year.of(yearValue);
    }

}
