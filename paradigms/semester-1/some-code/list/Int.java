public class Int {
    private int data;
    public Int previous;
    public Int next;

    public Int(int data) {
        this.data = data;
        previous = null;
        next = null;
    }

    public int toInt() {
        return data;
    }
}