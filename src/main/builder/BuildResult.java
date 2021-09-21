package main.builder;

public class BuildResult {
    private final String[] strings;

    public BuildResult(String ...strings) {
        this.strings = strings;
    }

    public String getResult() {
        StringBuilder result = new StringBuilder();

        for (String str: strings) {
            result.append(str);
        }

        return result.toString();
    }

    public void print() {
        for (String str: strings) {
            System.out.print(str);
        }
    }

    @Override
    public String toString() {
        return getResult();
    }
}
