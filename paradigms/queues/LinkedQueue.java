package queue;


import java.util.Iterator;

public class LinkedQueue extends AbstractQueue {

    private class Node {
        private final Object value;
        private Node next, prev;

        public Node(Object x) {
            assert x != null;
            value = x;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return  prev;
        }

        public Object getValue() { return value; }
    }

    private Node head, tail;

    public Node head() {
        return head;
    }

    public class LinkedQueueIterator implements Iterator<Object> {
        Node current;
        public LinkedQueueIterator(LinkedQueue q) {
            current = q.head();
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Object next() {
            Object x = current.getValue();
            current = current.getNext();
            return x;
        }
    }

    @Override
    public Iterator iterator() {
        return new LinkedQueueIterator(this);
    }

    public void enqueueImplementation(Object x) {
        Node newElement = new Node(x);
        Node oldPrev = tail;
        newElement.setPrev(oldPrev);
        tail = newElement;
        if (size == 0) {
            head = newElement;
        } else {
            oldPrev.setNext(newElement);
        }
    }

    public Object dequeueImplementation() {
        Object x = head.getValue();
        head = head.getNext();
        return x;
    }

    public Object elementImplementation() {
        assert size > 0;
        return head.getValue();
    }

    public void pushImplementation(Object x) {
        Node oldHead = head;
        Node newElement = new Node(x);
        newElement.setNext(oldHead);
        head = newElement;
        if (size == 0) {
            tail = newElement;
        } else {
            oldHead.setNext(newElement);
        }
    }

    public Object peekImplementation() {
        return tail.getValue();
    }

    public Object removeImplementation() {
        Object x = tail.getValue();
        tail = tail.getPrev();
        tail.setNext(null);
        return x;
    }

    public void clearImplementation() {
        head = (tail = null);
    }

    @Override
    protected Queue createQueue() {
        return new LinkedQueue();
    }

}
