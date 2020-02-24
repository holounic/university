package expression.parser;

public class ParenthesisParsingException extends ParsingException {
    public ParenthesisParsingException(int pointer, Object... args) {
        super("Mismatched parenthesis", pointer, args);
    }
}
