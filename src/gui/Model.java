package gui;

public class Model {
    private String input;
    private String result;
    private boolean isMultilineToSingleLineMode;
    private String unitId;
    private View view;

    public void registerListener(View view) {
        this.view = view;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public boolean isMultilineToSingleLineMode() {
        return isMultilineToSingleLineMode;
    }

    public void setMultilineToSingleLineMode(boolean multilineToSingleLineMode) {
        isMultilineToSingleLineMode = multilineToSingleLineMode;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
