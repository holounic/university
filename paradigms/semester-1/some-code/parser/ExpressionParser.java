package parser;

import expression.*;

import java.util.Map;
import java.util.Set;

public class ExpressionParser implements Parser {
    public ExpressionParser(String expression) {
        this.expression = expression + END;
    }
    private static final Set<String> variables = Set.of(
            "x", "y", "z"
    );

    private static final Map <String, Operation> operations = Map.of(
            "-" , Operation.SUB,
            "+", Operation.ADD,
            "*", Operation.MUL,
            "/", Operation.DIV
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
            throw new ParsingException("Expected operation, found something", pointer);
        }
        return Operation.NONE;
    }

    private String identifier() {
        StringBuilder identifier = new StringBuilder();
        if (!test(END)) {
            identifier.append(getChar());
            if (Character.isLetter(getChar())) {
                while (Character.isLetter(nextChar())) {
                    identifier.append(getChar());
                }
            } else {
                nextChar();
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
                throw new ParsingException("Wrong number format", pointer);
            }
        }
        if (getChar() == '-' && Character.isAlphabetic(getChar(pointer + 1))) {
            nextChar();
            String variable = identifier();
            if (variables.contains(variable)) {
                return new CheckedNegate(variable);
            }
            pointer -= variable.length();
            throw new ParsingException("Wrong number format", pointer);
        }
        if (test('(')) {
            int begin = pointer;
            pointer++;
            TripleExpression inner = addSub();
            skipWhiteSpace();
            if (testNext(')')) {
                nextChar();
                return inner;
            }
            pointer = begin;
            throw new ParsingException("Mismatched parenthesis ", pointer);
        }
        if (!test(END)) {
            String identifier = identifier();
            if (variables.contains(identifier)) {
                return new Variable(identifier);
            }
            pointer -= identifier.length();
            throw new ParsingException("Expected value, found " + identifier, pointer);
        }
        throw new ParsingException("Expected value, found something else", pointer);
    }

    private TripleExpression mulDiv() throws ParsingException {
        TripleExpression left = number();
        while (true) {
            currentOperation = operation();
            switch (currentOperation) {
                case MUL:
                    left =  new CheckedMultiply((Operand) left, (Operand) number());
                    break;
                case DIV:
                    left =  new CheckedDivide((Operand) left, (Operand) number());
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
    public TripleExpression parse() throws ParsingException {
        TripleExpression result = addSub();
        if (!test(END)) {
            throw new ParsingException("Expected end, found " + getChar(), pointer);
        }
        return result;
    }
}
