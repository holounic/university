package expression;

import expression.parser.OverflowException;

public class CheckedPow extends AbstractBinary {
    private static String operationSign = "**";
    private static Priority priority = Priority.HIGH_P;

    public CheckedPow(Operand left, Operand right) {
        super(left, right);
    }

    @Override
    protected String getOperationSign() {
        return operationSign;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    private void overflowCheck(int left, int right) {
        if (right == 0 || left == 0) {
            return;
        }
        if (right > 0 && left > 0) {
            int delta = Integer.MAX_VALUE / left;
            if (right > delta) {
                throw new OverflowException();
            }
            return;
        }
        if (right < 0 && left < 0) {
            int delta = Integer.MAX_VALUE / left;
            if (right < delta) {
                throw new OverflowException();
            }
            return;
        }
        if (right > 0 && left < 0) {
            int delta = Integer.MIN_VALUE / right;
            if (left < delta) {
                throw new OverflowException();
            }
            return;
        }
        int delta = Integer.MIN_VALUE / left;
        if (right < delta) {
            throw new OverflowException();
        }
    }

    private int compute(int left, int right) throws ArithmeticException {
        if (left == 0 && right == 0) {
            throw new ArithmeticException("Two zeros prohibited");
        }
        if (right < 0) {
            throw new ArithmeticException("Negative powers prohibited");
        }
        if (left == 0) {
            return 0;
        }
        if (right == 0) {
            return 1;
        }
        int i = 1;
        int result = left;
        while (i < right) {
            if (right / 2 >= i) {
                overflowCheck(result, result);
                result *= result;
                i *= 2;
            } else {
                overflowCheck(result, left);
                result *= left;
                i++;
            }
        }
        return result;
    }

    @Override
    protected int operator(int x, int y) {
        return compute(x, y);
    }

}
