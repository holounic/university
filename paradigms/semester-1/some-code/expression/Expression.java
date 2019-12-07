package expression;

@FunctionalInterface
public interface Expression extends ToMiniString {
    int evaluate(int x);
}