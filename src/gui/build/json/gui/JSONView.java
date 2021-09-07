package gui.build.json.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JSONView {

    private JFrame frame;
    private JTextField keyInput;
    private JTextArea document;
    private JButton nextButton;
    private JButton finishButton;

    public JSONView() {
        this.frame = new JFrame("Intentum JSON creator");

        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        frame.setIconImage(icon);

        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(panel);

        keyInput = new JTextField();
        panel.add(keyInput);
        keyInput.setBorder(new LineBorder(Color.BLACK));

        document = new JTextArea();
        document.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane documentScrollPane = new JScrollPane(document,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(documentScrollPane);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBorder(new EmptyBorder(10, 10, 10, 10));

        nextButton = new JButton("Следващ");
        buttons.add(nextButton);

        finishButton = new JButton("Завърши");
        buttons.add(finishButton);

        panel.add(buttons);
    }

    public void show() {
        this.frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
