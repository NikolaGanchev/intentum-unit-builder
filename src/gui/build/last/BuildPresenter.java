package gui.build.last;

import builder.*;
import builder.json.TokenIterator;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import gui.build.json.gui.JSONModel;
import gui.build.json.gui.JSONPresenter;
import gui.build.json.gui.JSONView;
import gui.build.json.gui.ResultView;
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

            String json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
            ArrayList<Token> tokens = tokenizer.tokenize();
            for (Token token: tokens) {
                buildView.getInputArea().append(token.getToken() + "\n");
            }

            showJson(json);

            build(tokens, false);
        });
        presenter.init();
    }

    private void showJson(String json) {
        ResultView resultView = new ResultView(json);
        resultView.show();
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
