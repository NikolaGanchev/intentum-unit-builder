package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class View {
    private JFrame frame;
    private JTextArea inputArea;
    private JTextArea resultArea;
    private JTextField idField;
    private JCheckBox isMultilineToSingleLine;
    private JButton buildButton;
    private JButton copyButton;

    public View() {
        this.frame = new JFrame("Intentum unit builder");

        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        frame.setIconImage(icon);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(panel);

        JLabel inputLabel = new JLabel("Вход");
        panel.add(inputLabel);

        this.inputArea = new JTextArea();
        this.inputArea.setEditable(true);
        JScrollPane inputScrollPane = new JScrollPane(inputArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.inputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(inputScrollPane);

        this.isMultilineToSingleLine = new JCheckBox("Няколко реда към един ред");
        this.isMultilineToSingleLine.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(isMultilineToSingleLine);

        JLabel idLabel = new JLabel("Идентификатор");
        panel.add(idLabel);

        this.idField = new JTextField("");
        this.idField.setMaximumSize(new Dimension(panel.getMaximumSize().width, idField.getPreferredSize().height));
        panel.add(idField);

        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel);

        this.buildButton = new JButton("Създай");
        buttonPanel.add(buildButton);

        this.copyButton = new JButton("Копирай");
        buttonPanel.add(copyButton);
        buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());

        JLabel resultLabel = new JLabel("Резултат");
        panel.add(resultLabel);

        this.resultArea = new JTextArea();
        this.resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(resultScrollPane);
    }

    public void show() {
        this.frame.setVisible(true);
    }

    public JTextArea getInputArea() {
        return inputArea;
    }

    public JTextArea getResultArea() {
        return resultArea;
    }

    public JTextField getIdField() {
        return idField;
    }

    public JCheckBox getIsMultilineToSingleLine() {
        return isMultilineToSingleLine;
    }

    public JButton getBuildButton() {
        return buildButton;
    }

    public JButton getCopyButton() {
        return copyButton;
    }
}
