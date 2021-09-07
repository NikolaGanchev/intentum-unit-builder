package gui.build.json.gui;

import javax.swing.*;

public class JSONPresenter {
    private JSONModel jsonModel;
    private JSONView jsonView;

    public JSONPresenter(JSONModel jsonModel, JSONView jsonView) {
        this.jsonModel = jsonModel;
        this.jsonView = jsonView;
    }

    public void init() {
        initModel();
        initView();
    }

    public void initModel() {
    }

    public void initView() {
        jsonView.show();
    }

}
