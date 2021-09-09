package gui.build.json.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
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
    private JButton transformToSingleLineButton;
    private JCheckBox stopIncrementingCheckbox;

    public JSONView() {
        this.frame = new JFrame("Intentum JSON creator");

        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        frame.setIconImage(icon);

        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(panel);

        JPanel inputs = new JPanel();
        inputs.setLayout(new BoxLayout(inputs, BoxLayout.Y_AXIS));
        inputs.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(inputs);

        keyInput = new JTextField();
        keyInput.setMaximumSize(
                new Dimension(keyInput.getMaximumSize().width, keyInput.getPreferredSize().height));
        keyInput.setBorder(
                new CompoundBorder(
                        new LineBorder(Color.BLACK),
                        new EmptyBorder(10, 10, 10, 10)));
        inputs.add(keyInput);

        document = new JTextArea();
        document.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane documentScrollPane = new JScrollPane(document,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inputs.add(documentScrollPane);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBorder(new EmptyBorder(10, 10, 10, 10));

        stopIncrementingCheckbox = new JCheckBox("Не добавяй към ключа");
        buttons.add(stopIncrementingCheckbox);

        transformToSingleLineButton = new JButton("Един ред");
        buttons.add(transformToSingleLineButton);

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

    public JTextField getKeyInput() {
        return keyInput;
    }

    public JTextArea getDocument() {
        return document;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public JButton getFinishButton() {
        return finishButton;
    }

    public JButton getTransformToSingleLineButton() {
        return transformToSingleLineButton;
    }

    public JCheckBox getStopIncrementingCheckbox() {
        return stopIncrementingCheckbox;
    }
}
