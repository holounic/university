package expression;

public class Const implements Operand {
    public final int constant;
    private static final Priority priority = Priority.VAR;

    public Const(int constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return Integer.toString(this.constant);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public String toMiniString() {
        return Integer.toString(this.constant);
    }

    @Override
    public int evaluate(int x) {
        return this.constant;
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

        Const toCompare = (Const)object;
        return constant == toCompare.constant;
    }


}
