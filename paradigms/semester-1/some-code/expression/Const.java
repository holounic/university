package expression;

public class Const implements Operand {
    private final int constant;
    private static final Priority priority = Priority.NULL;

    public Const(int constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return Integer.toString(this.constant);
    }

    @Override
    public boolean equals(Operand toCompare) {
        return  toCompare instanceof Const && Integer.toString(this.constant).equals(toCompare.toString());
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

}
