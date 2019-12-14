package expression;

public interface Operand extends TripleExpression, Expression {
    String toString();
    String toMiniString();
    Priority getPriority();
}
