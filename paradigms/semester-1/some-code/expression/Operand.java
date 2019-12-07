package expression;

public interface Operand extends Expression {
    String toString();
    String toMiniString();
    Priority getPriority();
}
