import java.util.regex.Pattern;

public class Lexer {
    private final TextWrapper text;
    private Token curToken;
    private char curChar;
    private String curValue;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z_0-9]*");

    private static final char END = '\0';

    public Lexer(TextWrapper text) {
        this.text = text;
    }

    public Lexer(String text) {
        this(new TextWrapper(text));
    }

    public void nextToken() {
        dropCurValue();

        while (hasNext() && isBlank()) {
            nextChar();
        }
        curChar = nextChar();
        curToken = switch (curChar) {
            case '(' -> Token.L_BRACKET;
            case ')' -> Token.R_BRACKET;
            case ';' -> Token.SEMICOLON;
            case ',' -> Token.COMMA;
            case '*' -> Token.ASTERISK;
            case '&' -> Token.AMPERSAND;
            case END -> Token.END;
            default -> parseIdentifier();
        };
    }

    private Token parseIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (hasNext()) {
            builder.append(curChar);
            curChar = nextChar();
            if (curChar == '(' || curChar == ')' || curChar == '*' || curChar == '&' || curChar == ',') {
                text.prev();
                break;
            }
            if (isBlank()) {
                builder.append(curChar);
                break;
            }
        }
        curValue = builder.toString();
        if (curValue.equals("const")) {
            return Token.CONST;
        }
        return VARIABLE_PATTERN.matcher(curValue).matches() ? Token.VAR : Token.UNDEFINED;
    }

    private boolean hasNext() {
        return text.hasNext();
    }

    private boolean isBlank() {
        return Character.isWhitespace(text.peek());
    }

    private char nextChar() {
        return text.next();
    }

    public Token getCurToken() {
        return curToken;
    }

    public int getPosition() {
        return text.getPosition();
    }

    private void dropCurValue() {
        curValue = null;
    }
    
}
