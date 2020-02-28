package expression;

public interface Operand extends Expression, TripleExpression {
    String toString();
    String toMiniString();
    Priority getPriority();
}
