package expression;

public enum Priority {
    LOW_S(1, 0),
    LOW_A(1, 1),
    HIGH_D(4, 0),
    HIGH_M(4, 1),
    VAR(10, 1);

    private int value;
    private int subvalue;

    Priority(int value, int subvalue) {
        this.value = value;
        this.subvalue = subvalue;
    }

    public int value() {
        return value;
    }
    public int subvalue() {
        return subvalue;
    }
}
