package expression.parser;

import expression.*;

import java.text.ParseException;
import java.util.Map;
import java.util.Set;

public class ExpressionParser implements Parser {
    public ExpressionParser() {}

    public ExpressionParser(String source) {
        this.expression = source + END;
    }
    private static final Set<String> variables = Set.of(
            "x", "y", "z"
    );

    private static final Map <String, Operation> operations = Map.of(
            "-" , Operation.SUB,
            "+", Operation.ADD,
            "*", Operation.MUL,
            "/", Operation.DIV,
            "//", Operation.LOG,
            "**", Operation.POW
    );

    private final char END = '\0';
    private String expression;
    private int pointer = 0;
    private Operation currentOperation = Operation.NONE;

    private char getChar() {
        return expression.charAt(pointer);
    }

    private char getChar(int pointer) {
        if (pointer >= expression.length()) {
            return END;
        }
        return expression.charAt(pointer);
    }

    private char nextChar() {
        if (pointer >= expression.length() - 1) {
            return END;
        }
        return expression.charAt(++pointer);
    }

    private boolean test(char toCompare) {
        return (getChar() == toCompare);
    }

    private void skipWhiteSpace() {
        while (Character.isWhitespace(expression.charAt(pointer))) {
            pointer++;
        }
    }

    private boolean testNext(char toCompare) {
        boolean equal = test(toCompare);
        if (equal) {
            pointer++;
        }
        return equal;
    }

    private String readNumber() {
        StringBuilder number = new StringBuilder();
        do {
            number.append(getChar());
        } while (Character.isDigit(nextChar()));
        return number.toString();
    }

    private Operation operation() throws ParsingException {
        skipWhiteSpace();
        String identifier = identifier();
        if (operations.containsKey(identifier)) {
            return operations.get(identifier);
        }
        pointer -= identifier.length();
        if (test(')')) {
            return Operation.NONE;
        }
        if (!test(END)) {
            throw new OperationParsingException(pointer);
        }
        return Operation.NONE;
    }

    private String identifier() {
        StringBuilder identifier = new StringBuilder();
        if (!test(END)) {
            char c = getChar();
            identifier.append(getChar());
            if (Character.isLetter(getChar())) {
                while (Character.isLetter(nextChar())) {
                    identifier.append(getChar());
                }
            } else {
                if (c == nextChar() && (c == '*' || c == '/')) {
                    identifier.append(getChar());
                    nextChar();
                }
            }
        }
        return identifier.toString();
    }

    private TripleExpression number() throws ParsingException {
        currentOperation = Operation.NONE;
        skipWhiteSpace();
        if (Character.isDigit(getChar()) || getChar() == '-' && Character.isDigit(getChar(pointer + 1))) {
            String number = readNumber();
            try {
                return new Const(Integer.parseInt(number));
            } catch (NumberFormatException e) {
                pointer -= number.length();
                throw new NumberParsingException(pointer);
            }
        }
        if (getChar() == '-') {
            nextChar();
            TripleExpression inner = number();
            if (inner instanceof Const) {
                return new Const(-1 * ((Const) inner).evaluate(0));
            }
            return new CheckedNegate((Operand)inner);
        }
        if (test('(')) {
            int begin = pointer;
            pointer++;
            TripleExpression inner = addSub();
            skipWhiteSpace();
            if (testNext(')')) {
                return inner;
            }
            pointer = begin;
            throw new ParenthesisParsingException(pointer);
        }
        if (!test(END)) {
            String identifier = identifier();
            if (variables.contains(identifier)) {
                return new Variable(identifier);
            }
            pointer -= identifier.length();
            throw new ParsingException("Expected value, found " + identifier, pointer);
        }
        throw new ParsingException("Expected value, found end of the expression", pointer);
    }

    private TripleExpression mulDiv() throws ParsingException {
        TripleExpression left = powLog();
        while (true) {
            switch (currentOperation) {
                case MUL:
                    left =  new CheckedMultiply((Operand) left, (Operand) powLog());
                    break;
                case DIV:
                    left =  new CheckedDivide((Operand) left, (Operand) powLog());
                    break;
                default:
                    return left;
            }
        }
    }

    private TripleExpression powLog() throws ParsingException {
        TripleExpression left = number();
        while (true) {
            currentOperation = operation();
            switch (currentOperation) {
                case LOG:
                    left = new CheckedLog((Operand)left, (Operand)number());
                    break;
                case POW:
                    left = new CheckedPow((Operand)left, (Operand)number());
                    break;
                    default:
                        return left;
            }
        }
    }
    private TripleExpression addSub() throws ParsingException {
        TripleExpression left = mulDiv();
        while (true) {
            switch (currentOperation) {
                case ADD:
                    left =  new CheckedAdd((Operand) left, (Operand) mulDiv());
                    break;
                case SUB:
                    left = new CheckedSubtract((Operand) left, (Operand) mulDiv());
                    break;
                default:
                    return left;
            }
        }
    }

    @Override
    public TripleExpression parse(String source) throws ParsingException {
        pointer = 0;
        this.expression = source + END;
        return parse();
    }

    public TripleExpression parse() throws ParsingException {
            TripleExpression result = addSub();
            if (!test(END)) {
                throw new ParsingException("Expected end, found " + getChar(), pointer);
            }
            return result;
    }
}
