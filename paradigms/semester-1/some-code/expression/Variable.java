package expression;

public class Variable implements Operand, Expression {
    public final String variable;
    private static final Priority priority = Priority.VAR;

    public Variable(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return this.variable;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }
        Variable toCompare = (Variable)object;
        return this.variable.equals(toCompare.variable);
    }

    @Override
    public String toMiniString() {
        return this.variable;
    }

    public int evaluate(int x) {
        return x;
    }

}
