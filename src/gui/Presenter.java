package gui;

import builder.BuildResult;
import builder.Builder;
import builder.ImportManager;
import builder.LockManager;
import tokenizer.Token;
import tokenizer.Tokenizer;
import transformers.DocumentToPrettyStringTransformer;
import transformers.MultilineStringToArrayListTransformer;
import transformers.ResultTransformer;
import transformers.StringArrayListToSingleLineTransformer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Presenter {
    private Model model;
    private View view;

    public Presenter(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void init() {
        initModel();
        initView();
    }
    
    public void initModel() {
        this.model.registerListener(this.view);
        this.model.setInput("");
        this.model.setResult("");
        this.model.setUnitId("");
        this.model.setMultilineToSingleLineMode(false);
    }

    public void initView() {
        view.getInputArea().getDocument().addDocumentListener(inputDocumentListener);
        view.getResultArea().getDocument().addDocumentListener(resultDocumentListener);
        view.getIdField().addActionListener((ActionEvent e) -> {
            model.setUnitId(view.getIdField().getText());
        });
        view.getIsMultilineToSingleLine().addActionListener((ActionEvent e) -> {
            model.setMultilineToSingleLineMode(view.getIsMultilineToSingleLine().isSelected());
            view.getIdField().setEnabled(!model.isMultilineToSingleLineMode());
        });
        view.getBuildButton().addActionListener((ActionEvent e) -> { build(); });
        view.getCopyButton().addActionListener((ActionEvent e) -> { copyResultToClipboard(); });
        view.show();
    }

    private void setInput() {
       model.setInput(view.getInputArea().getText());
    }

    private void setResult() {
        model.setResult(view.getResultArea().getText());
    }

    private void build() {
        if (model.isMultilineToSingleLineMode()) {
            ArrayList<String> lines = new MultilineStringToArrayListTransformer().transform(model.getInput());

            String result = new StringArrayListToSingleLineTransformer().transform(lines);

            model.setResult(result);
            view.getResultArea().setText(result);
        }
        else {
            Tokenizer tokenizer = new Tokenizer();

            ArrayList<String> lines = new MultilineStringToArrayListTransformer().transform(model.getInput());

            lines.forEach(tokenizer::addString);

            ArrayList<Token> tokens = tokenizer.tokenize();
            LockManager lockManager = new LockManager();
            ImportManager importManager = new ImportManager();
            importManager.addExpectedImports();
            String lessonToken = model.getUnitId();

            Builder builder = new Builder(lessonToken, tokens, lockManager, importManager);

            BuildResult result = builder.build(new DocumentToPrettyStringTransformer());

            ResultTransformer resultTransformer = new ResultTransformer();

            String transformedResult = resultTransformer.transform(result.getResult());
            model.setResult(transformedResult);
            view.getResultArea().setText(transformedResult);
        }
    }

    public void copyResultToClipboard() {
        StringSelection stringSelection = new StringSelection(model.getResult());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    DocumentListener inputDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setInput();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setInput();
        }
    };

    DocumentListener resultDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setResult();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setResult();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setResult();
        }
    };


}
