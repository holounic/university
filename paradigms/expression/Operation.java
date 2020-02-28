package expression;

public abstract class Operation implements Operand {
    protected final Operand left;
    protected final Operand right;

    protected Operation(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    protected abstract String getOperationSign();

    @Override
    public String toString() {
        StringBuilder expression = new StringBuilder();
        expression.append("(").append(left.toString()).append(" ")
                .append(getOperationSign()).append(" ")
                .append(right.toString()).append(")");
        return expression.toString();
    }


    @Override
    public String toMiniString() {
        Priority priorityLeft = left.getPriority();
        Priority priorityRight = right.getPriority();
        Priority priorityCurrent = this.getPriority();

        StringBuilder expression = new StringBuilder();

        if (priorityCurrent.value() <= priorityLeft.value()) {
            expression.append(left.toMiniString());
        } else {
            expression.append("(").append(left.toMiniString()).append(")");
        }

        expression.append(" ").append(getOperationSign()).append(" ");

        if (priorityCurrent.value() < priorityRight.value()
                || priorityCurrent == priorityRight && priorityCurrent.subvalue() != 0) {
            expression.append(right.toMiniString());
        } else {
            expression.append("(").append(right.toMiniString()).append(")");
        }
        return expression.toString();
    }



    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        Operation toCompare = (Operation)object;
        return this.right.equals(toCompare.right) && this.left.equals(toCompare.left);
    }

    @Override
    public int hashCode() {
       return toString().hashCode();
    }
}
