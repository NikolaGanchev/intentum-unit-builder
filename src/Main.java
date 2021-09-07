
import gui.build.last.BuildModel;
import gui.build.last.BuildPresenter;
import gui.build.last.BuildView;

public class Main {

    public static void main(String[] args) {
        BuildPresenter presenter = new BuildPresenter(new BuildModel(), new BuildView());
        presenter.init();
    }
}
