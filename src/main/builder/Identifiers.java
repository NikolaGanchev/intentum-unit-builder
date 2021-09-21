package main.builder;

import main.tokenizer.Token;

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

    public static final String CODE_JAVA = "cj";

    public static final String CODE_CSHARP = "cc";

    public static final String PSEUDOCODE = "pc";

    public static final String CONSOLE = "con";

    public static final String ERROR = "e";

    public static final String FILL_QUESTION = "fiq";

    public static final String QUOTE = "q";

    public static final String TIP = "t";

    public static final String WARNING = "w";

    // No identifiers should be made that start with a except that one, since it can have other parts
    // That forces me to use .startsWith() for now
    public static final String ANSWER = "a";

    // The same applies for DATA_SWITCH, but it should be harder since it is a two character combination
    public static final String DATA_SWITCH = "ds";

    public static final String SWITCH_BEGINNING = "s";

    public static boolean isQuestion(Token token) {
        String identifierToCheck = token.getIdentifier();

        return identifierToCheck.equals(CONTROLLED_FULL_ANSWER_QUESTION) ||
                identifierToCheck.equals(CONTROLLED_TEST_QUESTION) ||
                identifierToCheck.equals(CONTROLLED_FILL_QUESTION) ||
                identifierToCheck.equals(FULL_ANSWER_QUESTION) ||
                identifierToCheck.equals(TEST_QUESTION) ||
                identifierToCheck.equals(FILL_QUESTION);
    }

    public static boolean isRelatedToSwitchToken(Token tokenToCheck, Token possibleRelation) {
        if (possibleRelation == null) {
            return false;
        }

        boolean tokenIsDataSwitchOfPossibleRelation = tokenToCheck.getIdentifier().equals(DATA_SWITCH) &&
                tokenToCheck.getNumber() == possibleRelation.getNumber()
                && tokenToCheck.getToken().startsWith(SWITCH_BEGINNING);

        return (tokenToCheck.getToken().startsWith(SWITCH_BEGINNING) || tokenIsDataSwitchOfPossibleRelation) &&
                 possibleRelation.getIdentifier().equals(DATA_SWITCH);
    }

    public static boolean isPartOfSwitch(Token token) {
        return token.getToken().startsWith(SWITCH_BEGINNING) ||
                token.getIdentifier().startsWith(DATA_SWITCH);
    }
}
