package expression;

public class Variable implements Operand {
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

    @Override
    public int evaluate(int x, int y, int z) {
        switch (variable) {
            case("x"):
                return x;
            case("y"):
                return y;
            default:
                return z;
        }
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int hashCode() {
        return  toString().hashCode();
    }
}
