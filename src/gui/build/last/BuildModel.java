package gui.build.last;

public class BuildModel {
    private String input;
    private String result;
    private boolean isMultilineToSingleLineMode;
    private String unitId;
    private BuildView buildView;

    public void registerListener(BuildView buildView) {
        this.buildView = buildView;
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
