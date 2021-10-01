package main.gui.build.json.gui;

import main.builder.CreateEvent;
import main.builder.Identifiers;
import main.builder.json.TokenIterator;
import main.gui.build.last.exceptions.KeyAlreadyExistsException;
import main.json.JsonCreatorManager;
import main.json.Prediction;
import main.json.TranslationTextPredictionGenerator;
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

public class JsonPresenter {
    private JsonModel jsonModel;
    private JsonView jsonView;
    private CreateEvent createEvent;
    private boolean manuallyActivatedStopIncrement;
    private JsonCreatorManager jsonCreatorManager;

    public JsonPresenter(JsonModel jsonModel, JsonView jsonView, JsonCreatorManager jsonCreatorManager) {
        this.jsonModel = jsonModel;
        this.jsonView = jsonView;
        this.jsonCreatorManager = jsonCreatorManager;
    }

    public void init() {
        initModel();
        initView();
        setKeyBindings();
        this.jsonCreatorManager.setIsIncrementingEvent((boolean oldValue, boolean isIncrementing) -> {
            if (isIncrementing) {
                reactivateIncrement();
            }
            else {
                stopIncrement();
            }
        });
        jsonCreatorManager.setIncrementing(!jsonModel.isStopIncrementing());
    }

    public void initModel() {
        jsonModel.setCurrentDocument("");
        jsonModel.setCurrentKey(jsonCreatorManager.getCurrentKey());
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
            manuallyActivatedStopIncrement = jsonView.getStopIncrementingCheckbox().isSelected();
            jsonCreatorManager.setIncrementing(!jsonModel.isStopIncrementing());
        });
        jsonView.getShowDocs().addActionListener((ActionEvent e) -> {
            TextView textView = new TextView(Documentation.getDocumentation(), "Документация", true);
            textView.show();
        });
        jsonView.show();
    }

    private void handleFinish() {
        if (createEvent != null) {
            createEvent.jsonCreated(jsonCreatorManager.getJsonObject(), jsonCreatorManager.getTokenizer());
        }
        // Close window
        jsonView.getFrame().dispatchEvent(new WindowEvent(jsonView.getFrame(), WindowEvent.WINDOW_CLOSING));
    }

    private void handleNext() {
        String keyInput = jsonView.getKeyInput().getText();
        Token token = new Token(keyInput);
        String key;
        String text;

        // Get the next key
        try {
            key = jsonCreatorManager.getNextKey(token, manuallyActivatedStopIncrement);
        } catch (KeyAlreadyExistsException e) {
            JOptionPane.showMessageDialog(jsonView.getFrame(), "Не може да има два еднакви ключа.", "Грешка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (jsonView.getDocument().getSelectedText() != null) {
            text = jsonView.getDocument().getSelectedText();
            jsonView.getDocument().replaceSelection("");
            trimDocument();
        }
        else if (jsonModel.getPrediction().getStartingIndex() != -1) {
            text = jsonModel.getPrediction().getPrediction();

            jsonView.getDocument().replaceRange(null,
                    jsonModel.getPrediction().getStartingIndex(),
                    jsonModel.getPrediction().getEndingIndex());
            trimDocument();
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

        if (!Identifiers.isPartOfSwitch(token)){
            jsonView.getLastSwitch().setText("");
        }

        updatePrediction();

        jsonCreatorManager.addTranslationPair(keyInput, text, true);

        jsonModel.setCurrentKey(key);
        jsonView.getKeyInput().setText(key);
    }

    private void setDocument() {
        jsonModel.setCurrentDocument(jsonView.getDocument().getText());
    }

    private void reactivateIncrement() {
        jsonView.getStopIncrementingCheckbox().setSelected(false);
        jsonModel.setStopIncrementing(false);
    }

    private void trimDocument() {
        String trimmed = jsonView.getDocument().getText().trim();
        jsonView.getDocument().setText(trimmed);
        jsonView.getDocument().setCaretPosition(0);
    }

    private void updatePrediction() {
        Prediction prediction = jsonCreatorManager.getPrediction(jsonModel.getCurrentDocument());
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
