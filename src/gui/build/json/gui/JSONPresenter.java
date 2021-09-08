package gui.build.json.gui;

import builder.CreateEvent;
import builder.json.TokenIterator;
import transformers.MultilineStringToArrayListTransformer;
import transformers.StringArrayListToSingleLineTransformer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
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
    }

    public void initModel() {
        jsonModel.setCurrentDocument("");
        jsonModel.setCurrentKey(tokenIterator.getTokenString());
    }

    public void initView() {
        jsonView.getKeyInput().setText(jsonModel.getCurrentKey());
        jsonView.getDocument().setText(jsonModel.getCurrentDocument());
        jsonView.getDocument().getDocument().addDocumentListener(changeDocumentListener);
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
        jsonModel.addTranslationPair(jsonView.getKeyInput().getText(),
                jsonView.getDocument().getSelectedText(), true);
        jsonView.getDocument().replaceSelection("");
        String nextKey = tokenIterator.next();
        jsonModel.setCurrentKey(nextKey);
        jsonView.getKeyInput().setText(nextKey);
    }

    private void setDocument() {
        jsonModel.setCurrentDocument(jsonView.getDocument().getText());
    }

    DocumentListener changeDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setDocument();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setDocument();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setDocument();
        }
    };

    public void setCreateEvent(CreateEvent createEvent) {
        this.createEvent = createEvent;
    }
}
