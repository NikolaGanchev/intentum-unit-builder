package main.gui.build.last;

import com.google.gson.JsonElement;
import main.builder.*;
import main.builder.json.TokenIterator;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import main.gui.build.json.gui.JSONModel;
import main.gui.build.json.gui.JSONPresenter;
import main.gui.build.json.gui.JSONView;
import main.gui.common.TextView;
import main.tokenizer.Token;
import main.tokenizer.Tokenizer;
import main.transformers.*;
import main.utils.Documentation;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

public class BuildPresenter {
    private BuildModel buildModel;
    private BuildView buildView;

    public BuildPresenter(BuildModel buildModel, BuildView buildView) {
        this.buildModel = buildModel;
        this.buildView = buildView;
    }

    public void init() {
        initModel();
        initView();
    }
    
    public void initModel() {
        this.buildModel.registerListener(this.buildView);
        this.buildModel.setInput("");
        this.buildModel.setResult("");
        this.buildModel.setUnitId("");
        this.buildModel.setMultilineToSingleLineMode(false);
    }

    public void initView() {
        buildView.getInputArea().getDocument().addDocumentListener(inputDocumentListener);
        buildView.getResultArea().getDocument().addDocumentListener(resultDocumentListener);
        buildView.getIdField().getDocument().addDocumentListener(idDocumentListener);
        buildView.getIsMultilineToSingleLine().addActionListener((ActionEvent e) -> {
            buildModel.setMultilineToSingleLineMode(buildView.getIsMultilineToSingleLine().isSelected());
            buildView.getIdField().setEnabled(!buildModel.isMultilineToSingleLineMode());
        });
        buildView.getBuildButton().addActionListener((ActionEvent e) -> {
            Tokenizer tokenizer = new Tokenizer();

            ArrayList<String> lines = new MultilineStringToArrayListTransformer().transform(buildModel.getInput());

            lines.forEach(tokenizer::addString);
            build(tokenizer, buildModel.isMultilineToSingleLineMode());
        });
        buildView.getCopyButton().addActionListener((ActionEvent e) -> { copyResultToClipboard(); });
        buildView.getCreateJSONButton().addActionListener((ActionEvent e) -> { initCreateJSON(); });
        buildView.getShowDocs().addActionListener((ActionEvent e) -> {
            TextView textView = new TextView(Documentation.getDocumentation(), "Документация", true);
            textView.show();
        });
        buildView.show();
    }

    private void setInput() {
       buildModel.setInput(buildView.getInputArea().getText());
    }

    private void setResult() {
        buildModel.setResult(buildView.getResultArea().getText());
    }

    private void setUnitId() {
        buildModel.setUnitId(buildView.getIdField().getText());
    }

    private void initCreateJSON() {
        JSONPresenter presenter = new JSONPresenter(
                new JSONModel(new JsonObject(), new Tokenizer()),
                new JSONView(),
                new TokenIterator());

        presenter.setCreateEvent((jsonObject, tokenizer) -> {

            ArrayList<String> tokenStrings = tokenizer.getSanitizedTokenStrings();
            for (String tokenString : tokenStrings) {
                buildView.getInputArea().append(tokenString + "\n");
            }

            String json = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(jsonObject);

            showJson(new EscapedToUnescapedStringTransformer().transform(json));

            build(tokenizer, false);
        });
        presenter.init();
    }

    private void showJson(String json) {
        TextView textView = new TextView(json, "JSON резултат", false);
        textView.show();
    }

    private void build(Tokenizer tokenizer, boolean isMultilineToSingleLineMode) {
        build(tokenizer.tokenize(), isMultilineToSingleLineMode);
    }

    private void build(ArrayList<Token> tokens, boolean isMultilineToSingleLineMode) {
        if (isMultilineToSingleLineMode) {
            ArrayList<String> lines = new MultilineStringToArrayListTransformer().transform(buildModel.getInput());

            String result = new StringArrayListToSingleLineTransformer().transform(lines);

            buildModel.setResult(result);
            buildView.getResultArea().setText(result);
        }
        else {
            LockManager lockManager = new LockManager();
            ImportManager importManager = new ImportManager();
            importManager.addExpectedImports();
            String lessonToken = buildModel.getUnitId();

            Builder builder = new Builder(lessonToken, tokens, lockManager, importManager);

            BuildResult result = builder.build(new DocumentToPrettyStringTransformer());

            ResultTransformer resultTransformer = new ResultTransformer();

            String transformedResult = resultTransformer.transform(result.getResult());
            buildModel.setResult(transformedResult);
            buildView.getResultArea().setText(transformedResult);
        }
    }

    public void copyResultToClipboard() {
        StringSelection stringSelection = new StringSelection(buildModel.getResult());
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

    DocumentListener idDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setUnitId();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setUnitId();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setUnitId();
        }
    };
}
