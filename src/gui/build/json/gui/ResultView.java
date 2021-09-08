package gui.build.json.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ResultView {
    private JFrame output;

    public ResultView(String result) {
        output = new JFrame("JSON изход");
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        output.setIconImage(icon);

        output.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        output.add(panel);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText(result);
        JScrollPane scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane);

    }

    public void show() {
        output.setVisible(true);
    }
}
