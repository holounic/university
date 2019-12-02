package expression;

@FunctionalInterface
public interface Expression extends ToMiniString {
    int evaluate(int x);
    default boolean equals(Expression expression) {
        return equals((Operand)expression);
    }
}