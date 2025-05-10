/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.programacion1.biblioteca.util;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author wgarciac
 */
public final class SwingControls {

    private SwingControls() {
    }

    public static JLabel createLabel(String text, Font font) {

        JLabel label = new JLabel(text);
        label.setFont(font);

        return label;
    }

    public static JTextField createField(int columns, Font font) {

        JTextField field = new JTextField("", columns);
        field.setFont(font);
        field.setCaretPosition(0);

        return field;
    }

    public static JButton createButton(String texto, Font font) {
        JButton button = new JButton(texto);
        button.setFont(font);

        return button;
    }

    public static JTextArea createTextArea(Font font) {
        JTextArea textArea = new JTextArea(20, 20);
        textArea.setFont(font);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        return textArea;
    }

    public static void setTextAndResetCaret(JTextComponent field, String text) {
        field.setText(text);
        field.setCaretPosition(0);
    }
}
