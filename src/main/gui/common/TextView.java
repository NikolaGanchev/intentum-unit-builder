package main.gui.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TextView {
    private JFrame output;

    public TextView(String result, String title, boolean isHtml) {
        output = new JFrame(title);
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        output.setIconImage(icon);

        output.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        output.add(panel);

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        if (isHtml) {
            textPane.setContentType("text/html");
        }
        textPane.setText(result);
        textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        textPane.setCaretPosition(0);
        textPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textPane,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane);

    }

    public void show() {
        output.setVisible(true);
    }
}
