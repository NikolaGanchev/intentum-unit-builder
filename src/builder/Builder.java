package builder;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import tokenizer.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Builder {
    private static final String BEGIN_STATEMENT = "export default function %s(props: any) {\n";
    public static final String USE_TRANSLATION = "const [tl] = useTranslation(\"lessons\");\n";
    private static final String RETURN_STATEMENT = "\nreturn ";
    private static final String END_STATEMENT = "; \n}";
    private final String lessonToken;
    private final ArrayList<Token> tokens;
    private Document document;
    private final LockManager lockManager;
    private final ImportManager importManager;
    private Element currentElement;

    public Builder(String lessonToken, ArrayList<Token> tokens, LockManager lockManager, ImportManager importManager) {
        this.lessonToken = lessonToken;
        this.tokens = tokens;
        this.lockManager = lockManager;
        this.importManager = importManager;
    }

    public PrintableResult build() {
        this.document = DocumentHelper.createDocument();
        this.currentElement = document.addElement("div");

        for (Token token : tokens) {
            evaluateToken(token);
        }

        this.currentElement.addElement(ComponentStrings.END_BUTTON)
                .addAttribute("onClick", "{() => { props.endUnit() }}");

        PrintableResult result = new PrintableResult(
                new String[] {
                        importManager.getImportState(),
                        BEGIN_STATEMENT.formatted(lessonToken.toUpperCase()),
                        USE_TRANSLATION,
                        lockManager.getLockState(),
                        RETURN_STATEMENT
                        },
                document,
                new String[] {
                        END_STATEMENT
                });

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
            case Identifiers.RESEARCH_QUESTION -> {
                this.currentElement.addElement(ComponentStrings.RESEARCH_QUESTION)
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.RESEARCH_QUESTION);
            }
            case Identifiers.CODE -> {
                this.currentElement.addElement(ComponentStrings.CODE)
                        .addAttribute("language", "")
                        .addAttribute("showNumbers", "true")
                        .addText(getTranslationString(token));

                this.importManager.addComponentImport(ComponentStrings.CODE);
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
        }
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
