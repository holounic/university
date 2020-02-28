package expression.parser;

public class OperationParsingException extends ParsingException {
    public OperationParsingException(int index, Object... args) {
        super("Operation parsing exception", index, args);
    }
}
