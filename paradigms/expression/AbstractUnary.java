package expression;

public abstract class AbstractUnary implements Operand {
    TripleExpression first;
    public AbstractUnary(TripleExpression x) {
        this.first = x;
    }

    protected abstract int operator(int x);

    @Override
    public int evaluate(int x, int y, int z) {
        return operator(first.evaluate(x, y, z));
    }

}
