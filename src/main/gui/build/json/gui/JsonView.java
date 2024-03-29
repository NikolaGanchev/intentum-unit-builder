package main.gui.build.json.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JsonView {

    private JFrame frame;
    private JTextField keyInput;
    private JTextArea document;
    private JButton nextButton;
    private JButton finishButton;
    private JButton transformToSingleLineButton;
    private JCheckBox stopIncrementingCheckbox;
    private JTextArea prediction;
    private JLabel lastSwitch;
    private JLabel lastKey;
    private JButton showDocs;

    public JsonView() {
        this.frame = new JFrame("Intentum JSON създател");

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
        document.setLineWrap(true);
        JScrollPane documentScrollPane = new JScrollPane(document,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inputs.add(documentScrollPane);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel keybindingNext = new JLabel("Следващ - Ctrl + Enter");
        buttons.add(keybindingNext);

        JLabel keybindingMultilineToSingleLine = new JLabel("Един ред - Ctrl + -");
        buttons.add(keybindingMultilineToSingleLine);

        JLabel keybindingKeepIterator = new JLabel("Не добавяй към ключа - Ctrl + =");
        buttons.add(keybindingKeepIterator);

        stopIncrementingCheckbox = new JCheckBox("Не добавяй към ключа");
        buttons.add(stopIncrementingCheckbox);

        showDocs = new JButton("Документация");
        buttons.add(showDocs);

        transformToSingleLineButton = new JButton("Един ред");
        buttons.add(transformToSingleLineButton);

        nextButton = new JButton("Следващ");
        buttons.add(nextButton);

        finishButton = new JButton("Завърши");
        buttons.add(finishButton);

        lastKey = new JLabel("");
        buttons.add(lastKey);

        lastSwitch = new JLabel("");
        buttons.add(lastSwitch);

        prediction = new JTextArea();
        prediction.setMaximumSize(new Dimension(buttons.getMinimumSize().width,
                prediction.getMaximumSize().height));
        prediction.setEditable(false);
        prediction.setLineWrap(true);
        prediction.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttons.add(prediction);

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

    public JTextArea getPrediction() {
        return prediction;
    }

    public JLabel getLastSwitch() {
        return lastSwitch;
    }

    public JLabel getLastKey() {
        return lastKey;
    }

    public JButton getShowDocs() {
        return showDocs;
    }
}
