package main.gui.build.json.gui;

import main.builder.CreateEvent;
import main.builder.Identifiers;
import main.builder.json.TokenIterator;
import main.gui.build.json.Prediction;
import main.gui.build.json.TranslationTextPredictionGenerator;
import main.gui.common.TextView;
import main.tokenizer.Token;
import main.transformers.MultilineStringToArrayListTransformer;
import main.transformers.StringArrayListToSingleLineTransformer;
import main.utils.Documentation;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class JSONPresenter {
    private JSONModel jsonModel;
    private JSONView jsonView;
    private TokenIterator tokenIterator;
    private CreateEvent createEvent;

    public JSONPresenter(JSONModel jsonModel, JSONView jsonView, TokenIterator tokenIterator) {
        this.jsonModel = jsonModel;
        this.jsonView = jsonView;
        this.tokenIterator = tokenIterator;
    }

    public void init() {
        initModel();
        initView();
        setKeyBindings();
    }

    public void initModel() {
        jsonModel.setCurrentDocument("");
        jsonModel.setCurrentKey(tokenIterator.getTokenString());
        jsonModel.setStopIncrementing(false);
        jsonModel.setPrediction(new Prediction("Няма валидни подсказвания", -1, -1));
    }

    public void initView() {
        jsonView.getKeyInput().setText(jsonModel.getCurrentKey());
        jsonView.getDocument().setText(jsonModel.getCurrentDocument());
        jsonView.getDocument().getDocument().addDocumentListener(changeDocumentListener);
        jsonView.getPrediction().setText(jsonModel.getPrediction().getPrediction());
        jsonView.getTransformToSingleLineButton().addActionListener((ActionEvent e) -> {
            String selectedText = jsonView.getDocument().getSelectedText();
            MultilineStringToArrayListTransformer transformer1 = new MultilineStringToArrayListTransformer();
            StringArrayListToSingleLineTransformer transformer2 = new StringArrayListToSingleLineTransformer();
            String output = transformer2.transform(transformer1.transform(selectedText));
            jsonView.getDocument().replaceSelection(output);
        });
        jsonView.getNextButton().addActionListener((ActionEvent e) -> {
            handleNext();
        });
        jsonView.getFinishButton().addActionListener((ActionEvent e) -> {
            handleFinish();
        });
        jsonView.getStopIncrementingCheckbox().addActionListener((ActionEvent e) -> {
            jsonModel.setStopIncrementing(jsonView.getStopIncrementingCheckbox().isSelected());
        });
        jsonView.getShowDocs().addActionListener((ActionEvent e) -> {
            TextView textView = new TextView(Documentation.getDocumentation(), "Документация", true);
            textView.show();
        });
        jsonView.show();
    }

    private void handleFinish() {
        if (createEvent != null) {
            createEvent.jsonCreated(jsonModel.getJsonObject(), jsonModel.getTokens());
        }
        // Close window
        jsonView.getFrame().dispatchEvent(new WindowEvent(jsonView.getFrame(), WindowEvent.WINDOW_CLOSING));
    }

    private void handleNext() {
        String keyInput = jsonView.getKeyInput().getText();
        Token token = new Token(keyInput);
        String nextIdentifier = token.getIdentifier();
        String text;
        boolean isPartOfSwitch = Identifiers.isPartOfSwitch(token);

        if (jsonModel.getJsonObject().has(token.getToken())) {
            JOptionPane.showMessageDialog(jsonView.getFrame(), "Не може да има два еднакви ключа.", "Грешка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (jsonView.getDocument().getSelectedText() != null) {
            text = jsonView.getDocument().getSelectedText();
            jsonView.getDocument().replaceSelection("");
        }
        else if (jsonModel.getPrediction().getStartingIndex() != -1) {
            text = jsonModel.getPrediction().getPrediction();

            jsonView.getDocument().replaceRange(null,
                    jsonModel.getPrediction().getStartingIndex(),
                    jsonModel.getPrediction().getEndingIndex());
        }
        else {
            // There is neither a selection, nor a prediction
            JOptionPane.showMessageDialog(jsonView.getFrame(), "Трябва да бъде избран текст за продължаване",
                    "Грешка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        jsonView.getLastKey().setText("Последен ключ: " + token.getToken());
        if (token.getIdentifier().startsWith(Identifiers.DATA_SWITCH)) {
            jsonView.getLastSwitch().setText("Последен Switch: " + token.getToken());
        }

        if (!isPartOfSwitch){
            jsonView.getLastKey().setText("");
        }

        updatePrediction();

        jsonModel.addTranslationPair(keyInput, text, true);

        // Don't increment if identifier is a question
        if (Identifiers.isQuestion(token)) {
            stopIncrement();
            nextIdentifier = Identifiers.ANSWER;
        }

        if (jsonModel.isStopIncrementing()) {
            String nextKey = tokenIterator.getTokenString() + nextIdentifier;
            jsonModel.setCurrentKey(nextKey);
            jsonView.getKeyInput().setText(nextKey);
            return;
        }

        if (token.getIdentifier().startsWith(Identifiers.DATA_SWITCH)) {
            String nextKey = tokenIterator.getTokenStringAsSToken();
            jsonModel.setCurrentKey(nextKey);
            jsonView.getKeyInput().setText(nextKey);
            return;
        }

        String nextKey = tokenIterator
                .next(isPartOfSwitch) + nextIdentifier;
        jsonModel.setCurrentKey(nextKey);
        jsonView.getKeyInput().setText(nextKey);
    }

    private void setDocument() {
        jsonModel.setCurrentDocument(jsonView.getDocument().getText());
    }

    private void updatePrediction() {
        Prediction prediction = TranslationTextPredictionGenerator
                .generatePrediction(jsonModel.getCurrentDocument(), true);
        jsonModel.setPrediction(prediction);
        jsonView.getPrediction().setText(prediction.getPrediction());
    }

    private void stopIncrement() {
        jsonView.getStopIncrementingCheckbox().setSelected(true);
        jsonModel.setStopIncrementing(true);
    }

    DocumentListener changeDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setDocument();
            updatePrediction();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setDocument();
            updatePrediction();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setDocument();
            updatePrediction();
        }
    };

    public void setCreateEvent(CreateEvent createEvent) {
        this.createEvent = createEvent;
    }

    private void setKeyBindings() {
        KeyStroke next = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK);
        KeyStroke multilineToSingleLine = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK);
        KeyStroke toggleIterator = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK);
        KeyStroke switchFocusToKeyInput = KeyStroke.getKeyStroke("UP");
        KeyStroke switchFocusToDocument = KeyStroke.getKeyStroke("DOWN");;

        jsonView.getDocument().getInputMap().put(next, "next");
        jsonView.getDocument().getActionMap().put("next", nextAction);

        jsonView.getKeyInput().getInputMap().put(next, "next");
        jsonView.getKeyInput().getActionMap().put("next", nextAction);

        jsonView.getDocument().getInputMap().put(multilineToSingleLine, "multilineToSingleLine");
        jsonView.getDocument().getActionMap().put("multilineToSingleLine", multilineToSingleLineAction);

        jsonView.getDocument().getInputMap().put(toggleIterator, "toggleIterator");
        jsonView.getDocument().getActionMap().put("toggleIterator", toggleIteratorAction);

        jsonView.getKeyInput().getInputMap().put(toggleIterator, "toggleIterator");
        jsonView.getKeyInput().getActionMap().put("toggleIterator", toggleIteratorAction);

        jsonView.getKeyInput().getInputMap().put(switchFocusToDocument, "switchFocusToDocument");
        jsonView.getKeyInput().getActionMap().put("switchFocusToDocument", switchFocusToDocumentAction);

        jsonView.getDocument().getInputMap().put(switchFocusToKeyInput, "switchFocusToKeyInput");
        jsonView.getDocument().getActionMap().put("switchFocusToKeyInput", switchFocusToKeyInputAction);
    }

    private final AbstractAction nextAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jsonView.getNextButton().doClick();
        }
    };

    private final AbstractAction multilineToSingleLineAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jsonView.getTransformToSingleLineButton().doClick();
        }
    };

    private final AbstractAction toggleIteratorAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jsonView.getStopIncrementingCheckbox().doClick();
        }
    };

    private final AbstractAction switchFocusToDocumentAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jsonView.getDocument().requestFocusInWindow();
        }
    };

    private final AbstractAction switchFocusToKeyInputAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jsonView.getKeyInput().requestFocusInWindow();
        }
    };
}
