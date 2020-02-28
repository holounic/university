package expression.parser;

public class OverflowException extends ArithmeticException {
    public OverflowException() {
        super("overflow");
    }
}
