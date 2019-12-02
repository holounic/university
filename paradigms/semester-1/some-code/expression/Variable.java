package expression;

public class Variable implements Operand, Expression {
    private final String variable;
    private static final Priority priority = Priority.VAR;

    public Variable(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return this.variable;
    }

    @Override
    public boolean equals(Operand toCompare) {
        return this.variable.equals(toCompare.toString()) && toCompare instanceof Variable;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Expression toCompare) {
        System.out.println("comparing");
        return equals((Operand) toCompare);
    }

    @Override
    public String toMiniString() {
        return this.variable;
    }

    public int evaluate(int x) {
        return x;
    }

}
