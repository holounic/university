package expression.parser;

public class NumberParsingException extends ParsingException {
    public NumberParsingException(int index, Object... args) {
        super("Number parsing exception", index, args);
    }
}
