package expression.parser;

public class ParsingException extends Exception {
    public ParsingException(String format, int index, Object... args) {
        super(String.format(format, args) + " at index " + (index + 1));
    }
}
