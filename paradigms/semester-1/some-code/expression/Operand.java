package expression;

public interface Operand {
    @Override
    String toString();
    boolean equals(Operand toCompare);
    String toMiniString();
    Priority getPriority();
    int evaluate(int x);
}
