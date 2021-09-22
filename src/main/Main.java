package main;

import main.gui.build.last.BuildModel;
import main.gui.build.last.BuildPresenter;
import main.gui.build.last.BuildView;

public class Main {

    public static void main(String[] args) {
        BuildPresenter presenter = new BuildPresenter(new BuildModel(), new BuildView());
        presenter.init();
    }
}
