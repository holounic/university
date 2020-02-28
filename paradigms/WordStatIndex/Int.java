public class Int {
    private int value;
    public Int previous;
    public Int next;

    public Int(int value) {
        this.value = value;
        previous = null;
        next = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}