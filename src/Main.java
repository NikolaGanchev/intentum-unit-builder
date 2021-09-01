import gui.Presenter;
import gui.Model;
import gui.View;

public class Main {
    static boolean multilineToSingleLine = false;

    public static void main(String[] args) {
        Presenter presenter = new Presenter(new Model(), new View());
        presenter.init();
    }
}
