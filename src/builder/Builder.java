package builder;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import tokenizer.Token;

import java.util.ArrayList;

public class Builder {
    private static final String BEGIN_STATEMENT = "public function %s(props: any) {\n";
    public static final String USE_TRANSLATION = "const [tl] = useTranslation(\"lessons\");\n";
    private static final String RETURN_STATEMENT = "\nreturn ";
    private static final String END_STATEMENT = "; \n}";
    private final String lessonToken;
    private final ArrayList<Token> tokens;
    private Document document;
    private final LockManager lockManager;
    private Element currentElement;

    public Builder(String lessonToken, ArrayList<Token> tokens, LockManager lockManager) {
        this.lessonToken = lessonToken;
        this.tokens = tokens;
        this.lockManager = lockManager;
    }

    public PrintableResult build() {
        this.document = DocumentHelper.createDocument();
        this.currentElement = document.addElement("div");

        for (Token token : tokens) {
            evaluateToken(token);
        }

        PrintableResult result = new PrintableResult(
                new String[] {
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
                        .addText(
                                ComponentStrings
                                        .TRANSLATION_STRING
                                        .formatted(this.lessonToken, token.getToken())
                        );
            }
            case Identifiers.HEADING -> {
                this.currentElement.addElement(ComponentStrings.HEADING)
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));
            }
            case Identifiers.LITERAL -> {
                this.currentElement.addElement(ComponentStrings.PRE)
                        .addElement(ComponentStrings.TEXT_BLOCK)
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));
            }
            case Identifiers.TEST_QUESTION -> {
                int lockNumber = lockManager.addLock();

                this.currentElement.addElement(ComponentStrings.TEST_QUESTION)
                        .addAttribute("answers", "{}")
                        .addAttribute("rightAnswer", "{}")
                        .addAttribute("tries", "3")
                        .addAttribute("onAnswer",
                                ComponentStrings.ON_ANSWER
                                        .formatted(lockNumber))
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));

                this.currentElement = currentElement.addElement(ComponentStrings.LOCK)
                        .addAttribute("isLocked", ComponentStrings.isLocked.formatted(lockNumber));
            }
            case Identifiers.FULL_ANSWER_QUESTION -> {
                int lockNumber = lockManager.addLock();

                this.currentElement.addElement(ComponentStrings.FULL_ANSWER_QUESTION)
                        .addAttribute("rightAnswer", "{}")
                        .addAttribute("onAnswer",
                                ComponentStrings.ON_ANSWER
                                        .formatted(lockNumber))
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));

                this.currentElement = currentElement.addElement(ComponentStrings.LOCK)
                        .addAttribute("isLocked", ComponentStrings.isLocked.formatted(lockNumber));
            }
            case Identifiers.RESEARCH_QUESTION -> {
                this.currentElement.addElement(ComponentStrings.RESEARCH_QUESTION)
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));
            }
            case Identifiers.CODE -> {
                this.currentElement.addElement(ComponentStrings.CODE)
                        .addAttribute("language", "")
                        .addAttribute("showNumbers", "true")
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));
            }
            case Identifiers.CONSOLE -> {
                this.currentElement.addElement(ComponentStrings.CONSOLE)
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));
            }
            case Identifiers.ERROR -> {
                this.currentElement.addElement(ComponentStrings.ERROR)
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));
            }
            case Identifiers.FILL_QUESTION -> {
                int lockNumber = lockManager.addLock();

                this.currentElement.addElement(ComponentStrings.FILL_QUESTION)
                        .addAttribute("text", ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()))
                        .addAttribute("onAnswer",
                                ComponentStrings.ON_ANSWER
                                        .formatted(lockNumber));

                this.currentElement = currentElement.addElement(ComponentStrings.LOCK)
                        .addAttribute("isLocked", ComponentStrings.isLocked.formatted(lockNumber));
            }
            case Identifiers.QUOTE -> {
                this.currentElement.addElement(ComponentStrings.QUOTE)
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));
            }
            case Identifiers.TIP -> {
                this.currentElement.addElement(ComponentStrings.TIP)
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));
            }
            case Identifiers.WARNING -> {
                this.currentElement.addElement(ComponentStrings.WARNING)
                        .addText(ComponentStrings
                                .TRANSLATION_STRING
                                .formatted(this.lessonToken, token.getToken()));
            }
        }
    }
}
