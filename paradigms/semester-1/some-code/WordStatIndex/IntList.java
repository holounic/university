public class IntList {
    private Int last;
    private Int first;
    private int size;

    public IntList() {
        last = null;
        first = null;
        size = 0;
    }
    public IntList(Int integer) {
        first = integer;
        last = integer;
        size = 1;
    }

    public void addFirst(int integer) {
        Int newInt = new Int(integer);
        if (first == null) {
            first = newInt;
            last = newInt;
            size = 1;
            return; 
        }
        Int oldInt = first;
        oldInt.previous = newInt;
        newInt.next = oldInt;
        first = newInt;
        size++;
    }

    public void addLast(int integer) {
        Int newInt = new Int(integer);
        if (first == null) {
            first = newInt;
            last = newInt;
            size = 1;
            return; 
        }
        Int oldInt = last;
        oldInt.next = newInt;
        newInt.previous = oldInt;
        last = newInt;
        size++;
    }

    public int removeFirst() {
        Int oldFirst = first;
        first = first.next;
        first.previous = null;
        return oldFirst.getValue();
    }

    public int removeLast() {
        Int oldLast = last;
        last = last.previous;
        last.next = null;
        return oldLast.getValue();
    }

    public void setFirst(int value) {
        first.setValue(value);
    }

    public int getFirst() {
        return first.getValue();
    }

    @Override 
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Int current = this.first;
        while (current != null) {
            builder.append(current.getValue() + " ");
            current = current.next;
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}