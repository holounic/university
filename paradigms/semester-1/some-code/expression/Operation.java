package expression;

import java.io.PrintStream;

public abstract class Operation implements Operand {
    protected final Operand left;
    protected final Operand right;

    public Operation(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    protected abstract char getOperationSign();

    @Override
    public String toString() {
        StringBuilder expression = new StringBuilder();
        expression.append("(").append(left.toString()).append(" ")
                .append(getOperationSign()).append(" ")
                .append(right.toString()).append(")");
        return expression.toString();
    }


    @Override
    public String toMiniString() {
        Priority priorityLeft = left.getPriority();
        Priority priorityRight = right.getPriority();
        Priority priorityCurrent = this.getPriority();

        StringBuilder expression = new StringBuilder();

        if (priorityCurrent.value() <= priorityLeft.value()) {
            expression.append(left.toMiniString());
        } else {
            expression.append("(").append(left.toMiniString()).append(")");
        }

        expression.append(" ").append(getOperationSign()).append(" ");

        if (priorityCurrent.value() < priorityRight.value()
                || priorityCurrent == priorityRight && priorityCurrent.subvalue() != 0) {
            expression.append(right.toMiniString());
        } else {
            expression.append("(").append(right.toMiniString()).append(")");
        }
        return expression.toString();
    }

    private boolean equals(Operation toCompare) {
        return this.getOperationSign() == toCompare.getOperationSign() && this.left.equals(toCompare.left)
                && this.right.equals(toCompare.right);
    }

    @Override
    public boolean equals(Operand toCompare) {
        return toCompare.equals(this);
    }

    @Override
    public boolean equals(Expression toCompare) {
        return equals((Operation) toCompare);
    }

    public int evaluate(int x) {
        int leftValue = left.evaluate(x);
        int rightValue = right.evaluate(x);
        switch(getOperationSign()) {
            case('+'):
                return leftValue + rightValue;
            case('*'):
                return  leftValue * rightValue;
            case('-'):
                return leftValue - rightValue;
            case('/'):
                return leftValue / rightValue;
        }
        return 0;
    }

}
