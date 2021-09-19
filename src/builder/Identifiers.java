package builder;

public class Identifiers {
    public static final String PARAGRAPH = "p";

    public static final String HEADING = "h";

    public static final String LITERAL = "pre";

    public static final String TEST_QUESTION = "tq";

    public static final String FULL_ANSWER_QUESTION = "fq";

    public static final String CONTROLLED_TEST_QUESTION = "tqc";

    public static final String CONTROLLED_FULL_ANSWER_QUESTION = "fqc";

    public static final String CONTROLLED_FILL_QUESTION = "fic";

    public static final String RESEARCH_QUESTION = "rq";

    public static final String CODE = "c";

    public static final String CONSOLE = "con";

    public static final String ERROR = "e";

    public static final String FILL_QUESTION = "fiq";

    public static final String QUOTE = "q";

    public static final String TIP = "t";

    public static final String WARNING = "w";
    
    public static final String ANSWER = "a";

    public static boolean isQuestion(String identifierToCheck) {
        return identifierToCheck.equals(CONTROLLED_FULL_ANSWER_QUESTION) ||
                identifierToCheck.equals(CONTROLLED_TEST_QUESTION) ||
                identifierToCheck.equals(CONTROLLED_FILL_QUESTION) ||
                identifierToCheck.equals(FULL_ANSWER_QUESTION) ||
                identifierToCheck.equals(TEST_QUESTION) ||
                identifierToCheck.equals(FILL_QUESTION);
    }
}
