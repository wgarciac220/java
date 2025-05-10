/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.util;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author wgarciac
 */
public class StarRatingRenderer extends JLabel implements TableCellRenderer {

    public StarRatingRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setFont(new Font("SansSerif", Font.PLAIN, 18));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        float rating = 0f;
        if (value instanceof Number) {
            rating = ((Number) value).floatValue();
        }

        int estrellas = Math.round(rating);
        StringBuilder estrellasStr = new StringBuilder("★★★★★".substring(0, estrellas));
        while (estrellasStr.length() < 5) {
            estrellasStr.append("☆"); // estrellas vacías
        }

        setText(estrellasStr.toString());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        return this;
    }
}
