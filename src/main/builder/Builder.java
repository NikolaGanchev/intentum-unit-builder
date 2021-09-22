package main.builder;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import main.tokenizer.Token;
import main.transformers.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Builder {
    private static final String BEGIN_STATEMENT = "export default function %s(props: any) {\n";
    public static final String USE_TRANSLATION = "const [tl] = useTranslation(\"lessons\");\n";
    private static final String RETURN_STATEMENT = "\nreturn ";
    private static final String END_STATEMENT = "; \n}";
    private static final String IS_SHOWING_STATE = "const [isShowing, setIsShowing] = useState(false);\n";
    private static final String IS_EXITING_STATE = "const [hasClickedCheck, setHasClickedCheck] = useState(false);\n";
    private static final String USE_TRANSLATION_REGULAR = "const [t] = useTranslation(\"common\");\n";
    private final String lessonToken;
    private final ArrayList<Token> tokens;
    private Document document;
    private final LockManager lockManager;
    private final ImportManager importManager;
    private Element currentElement;
    private boolean hasControlledQuestion = false;
    private Element lastRoot;


    public Builder(String lessonToken, ArrayList<Token> tokens, LockManager lockManager, ImportManager importManager) {
        this.lessonToken = lessonToken;
        this.tokens = tokens;
        this.lockManager = lockManager;
        this.importManager = importManager;
    }

    public BuildResult build(Transformer<String, Document> transformer) {
        this.document = DocumentHelper.createDocument();
        this.currentElement = document.addElement("div");

        for (Token token : tokens) {
            evaluateToken(token);
        }
        if (hasControlledQuestion) {

            this.currentElement.addElement(ComponentStrings.END_BUTTON)
                    .addAttribute("text", "{hasClickedCheck? t(\"app.end\"): t(\"app.check\")}")
                    .addAttribute("onClick", "{() => { if (hasClickedCheck) " +
                            "{ props.endUnit() } " +
                            "else { setIsShowing(true); " +
                            "setHasClickedCheck(true); }}}");
        }
        else {
            this.currentElement.addElement(ComponentStrings.END_BUTTON)
                    .addAttribute("onClick", "{() => { props.endUnit() }}");
        }


        String documentString = transformer.transform(document);

        BuildResult result = new BuildResult(
                        importManager.getImportState(),
                        "\n",
                        BEGIN_STATEMENT.formatted(lessonToken.toUpperCase()),
                        USE_TRANSLATION,
                        (hasControlledQuestion? USE_TRANSLATION_REGULAR + IS_SHOWING_STATE + IS_EXITING_STATE: ""),
                        lockManager.getLockState(),
                        RETURN_STATEMENT,
                        documentString,
                        END_STATEMENT);

        return result;
    }

    private void evaluateToken(Token token) {
        switch (token.getIdentifier()) {
            case Identifiers.PARAGRAPH -> {
                this.currentElement.addElement(ComponentStrings.TEXT_BLOCK)
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.TEXT_BLOCK);
            }
            case Identifiers.HEADING -> {
                this.currentElement.addElement(ComponentStrings.HEADING)
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.HEADING);
            }
            case Identifiers.LITERAL -> {
                this.currentElement.addElement(ComponentStrings.PRE)
                        .addElement(ComponentStrings.TEXT_BLOCK)
                        .addText(getTranslationString(token));
            }
            case Identifiers.TEST_QUESTION -> {
                int lockNumber = lockManager.addLock();

                String answers = "{}";

                if (token.hasRelated()) {
                    answers = constructAnswersFromRelated(token);
                }

                this.currentElement.addElement(ComponentStrings.TEST_QUESTION)
                        .addAttribute("answers", answers)
                        .addAttribute("rightAnswer", "{}")
                        .addAttribute("tries", "{3}")
                        .addAttribute("onAnswer",
                                ComponentStrings.ON_ANSWER
                                        .formatted(lockNumber))
                        .addText(getTranslationString(token));

                this.currentElement = currentElement.addElement(ComponentStrings.LOCK)
                        .addAttribute("isLocked", ComponentStrings.IS_LOCKED.formatted(lockNumber));

                this.importManager.addComponentImport(ComponentStrings.TEST_QUESTION);
                this.importManager.addComponentImport(ComponentStrings.LOCK);
            }
            case Identifiers.FULL_ANSWER_QUESTION -> {
                int lockNumber = lockManager.addLock();
                int answersSize = token.hasRelated() ? token.getRelated().length : 0;

                String answerAttributeString;
                String answers;

                if (answersSize <= 1) {
                    answerAttributeString = ComponentStrings.RIGHT_ANSWER;
                    if (token.hasRelated()) {
                        answers = "{%s}".formatted(getInlineTranslationString(token.getRelated()[0]));
                    }
                    else {
                        answers = "{}";
                    }
                }
                else {
                    answerAttributeString = ComponentStrings.RIGHT_ANSWERS;

                    answers = constructAnswersFromRelated(token);
                }

                this.currentElement.addElement(ComponentStrings.FULL_ANSWER_QUESTION)
                        .addAttribute(answerAttributeString, answers)
                        .addAttribute("onAnswer",
                                ComponentStrings.ON_ANSWER
                                        .formatted(lockNumber))
                        .addText(getTranslationString(token));

                this.currentElement = currentElement.addElement(ComponentStrings.LOCK)
                        .addAttribute("isLocked", ComponentStrings.IS_LOCKED.formatted(lockNumber));

                this.importManager.addComponentImport(ComponentStrings.FULL_ANSWER_QUESTION);
                this.importManager.addComponentImport(ComponentStrings.LOCK);
            }
            case Identifiers.CONTROLLED_TEST_QUESTION -> {
                hasControlledQuestion = true;

                String answers = "{}";

                if (token.hasRelated()) {
                    answers = constructAnswersFromRelated(token);
                }

                this.currentElement.addElement(ComponentStrings.TEST_QUESTION)
                        .addAttribute("answers", answers)
                        .addAttribute("rightAnswer", "{}")
                        .addAttribute("isShowing", "{isShowing}")
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.TEST_QUESTION);
                this.importManager.addStateImport();
            }
            case Identifiers.CONTROLLED_FULL_ANSWER_QUESTION -> {
                hasControlledQuestion = true;
                int answersSize = token.hasRelated() ? token.getRelated().length : 0;

                String answerAttributeString;
                String answers;

                if (answersSize == 1) {
                    answerAttributeString = ComponentStrings.RIGHT_ANSWER;
                    if (token.hasRelated()) {
                        answers = "{%s}".formatted(getInlineTranslationString(token.getRelated()[0]));
                    }
                    else {
                        answers = "{}";
                    }
                }
                else if (answersSize > 1) {
                    answerAttributeString = ComponentStrings.RIGHT_ANSWERS;

                    answers = constructAnswersFromRelated(token);
                }
                else {
                    answerAttributeString = null;
                    answers = "";
                }

                this.currentElement.addElement(ComponentStrings.FULL_ANSWER_QUESTION)
                        .addAttribute(answerAttributeString, answers)
                        .addAttribute("isShowing", "{isShowing}")
                        .addAttribute("noButton", "{true}")
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.FULL_ANSWER_QUESTION);
                this.importManager.addStateImport();
            }
            case Identifiers.RESEARCH_QUESTION -> {
                this.currentElement.addElement(ComponentStrings.RESEARCH_QUESTION)
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.RESEARCH_QUESTION);
            }
            case Identifiers.CODE_JAVA -> {
                addCodeWithLanguage(token, Languages.JAVA);
            }
            case Identifiers.CODE_CSHARP -> {
                addCodeWithLanguage(token, Languages.CSHARP);
            }
            case Identifiers.PSEUDOCODE -> {
                // The pseudocode used in intentum looks most like python code
                // So I use that as a substitute
                addCodeWithLanguage(token, Languages.PYTHON);
            }
            case Identifiers.CONSOLE -> {
                this.currentElement.addElement(ComponentStrings.CONSOLE)
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.CONSOLE);
            }
            case Identifiers.ERROR -> {
                this.currentElement.addElement(ComponentStrings.ERROR)
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.ERROR);
            }
            case Identifiers.FILL_QUESTION -> {
                int lockNumber = lockManager.addLock();

                this.currentElement.addElement(ComponentStrings.FILL_QUESTION)
                        .addAttribute("text", getTranslationString(token))
                        .addAttribute("onAnswer",
                                ComponentStrings.ON_ANSWER
                                        .formatted(lockNumber));

                this.currentElement = currentElement.addElement(ComponentStrings.LOCK)
                        .addAttribute("isLocked", ComponentStrings.IS_LOCKED.formatted(lockNumber));

                this.importManager.addComponentImport(ComponentStrings.FILL_QUESTION);
                this.importManager.addComponentImport(ComponentStrings.LOCK);
            }
            case Identifiers.QUOTE -> {
                this.currentElement.addElement(ComponentStrings.QUOTE)
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.QUOTE);
            }
            case Identifiers.TIP -> {
                this.currentElement.addElement(ComponentStrings.TIP)
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.TIP);
            }
            case Identifiers.WARNING -> {
                this.currentElement.addElement(ComponentStrings.WARNING)
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.WARNING);
            }
            case Identifiers.DATA_SWITCH -> {
                // If the token has related, it means it is the first in the Switch chain
                if (token.hasRelated()) {
                    // Create new switch
                    this.currentElement = currentElement.addElement(ComponentStrings.SWITCH);

                    // Save switch element to return to
                    this.lastRoot = currentElement;

                    // Add first data-switch
                    this.currentElement = currentElement.addElement("div")
                            .addAttribute("data-switch", getTranslationString(token));

                    // Loop through related to evaluate related
                    for (Token relatedToken: token.getRelated()) {
                        evaluateToken(relatedToken);
                    }

                    // Get back to Switch element
                    this.currentElement = this.lastRoot;

                    // Now, the parent of the Switch should be directly above it
                    this.currentElement = this.currentElement.getParent();

                }
                else {
                    // Get back to Switch element
                    this.currentElement = this.lastRoot;

                    // Add data-switch
                    this.currentElement = currentElement.addElement("div")
                            .addAttribute("data-switch", getTranslationString(token));
                }

                this.importManager.addComponentImport(ComponentStrings.SWITCH);
            }
        }
    }

    private void addCodeWithLanguage(Token token, String language) {
        this.currentElement.addElement(ComponentStrings.CODE)
                .addAttribute("language", "{%s}".formatted(language))
                .addAttribute("showNumbers", "true")
                .addText(getTranslationString(token));

        this.importManager.addComponentImport(ComponentStrings.CODE);
    }

    private String getTranslationString(Token token) {
        return ComponentStrings
                .TRANSLATION_STRING
                .formatted(this.lessonToken, token.getToken());
    }

    private String getInlineTranslationString(Token token) {
        return ComponentStrings
                .INLINE_TRANSLATION_STRING
                .formatted(this.lessonToken, token.getToken());
    }

    private String constructAnswersFromRelated(Token token) {
        StringBuilder sb = new StringBuilder();
        sb.append("{[");
        Iterator<Token> iterator = Arrays.stream(token.getRelated()).iterator();

        while (iterator.hasNext()) {
            sb.append(getInlineTranslationString(iterator.next()));

            if (iterator.hasNext()) {
                sb.append(", ");
            }

        }

        sb.append("]}");
        return sb.toString();
    }
}
