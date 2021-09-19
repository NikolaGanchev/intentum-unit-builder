package gui.build.json.gui;

import builder.CreateEvent;
import builder.Identifiers;
import builder.json.TokenIterator;
import gui.build.json.Prediction;
import gui.build.json.TranslationTextPredictionGenerator;
import tokenizer.Token;
import transformers.MultilineStringToArrayListTransformer;
import transformers.StringArrayListToSingleLineTransformer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.TextAction;
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

        jsonView.getDocument().setText(jsonModel.getCurrentDocument());
        setDocument();

        if (jsonView.getDocument().getSelectedText() != null) {
            text = jsonView.getDocument().getSelectedText().trim();
            jsonView.getDocument().replaceSelection("");
        }
        else if (jsonModel.getPrediction().getStartingIndex() != -1) {
            text = jsonModel.getPrediction().getPrediction().trim();

            jsonView.getDocument().replaceRange(null,
                    jsonModel.getPrediction().getStartingIndex(),
                    jsonModel.getPrediction().getEndingIndex());
        }
        else {
            // There is neither a selection, nor a prediction
            return;
        }

        updatePrediction();

        jsonModel.addTranslationPair(keyInput, text, true);

        // Don't increment if identifier is a question
        if (Identifiers.isQuestion(token.getIdentifier())) {
            jsonView.getStopIncrementingCheckbox().setSelected(true);
            jsonModel.setStopIncrementing(true);
            nextIdentifier = Identifiers.ANSWER;
        }

        if (jsonModel.isStopIncrementing()) {
            String nextKey = tokenIterator.getTokenString() + nextIdentifier;
            jsonModel.setCurrentKey(nextKey);
            jsonView.getKeyInput().setText(nextKey);
            return;
        }

        String nextKey = tokenIterator.next() + nextIdentifier;
        jsonModel.setCurrentKey(nextKey);
        jsonView.getKeyInput().setText(nextKey);
    }

    private void setDocument() {
        jsonModel.setCurrentDocument(jsonView.getDocument().getText().trim());
    }

    private void updatePrediction() {
        Prediction prediction = TranslationTextPredictionGenerator.generatePrediction(jsonModel.getCurrentDocument());
        jsonModel.setPrediction(prediction);
        jsonView.getPrediction().setText(prediction.getPrediction());
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
