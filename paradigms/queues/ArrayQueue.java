package queue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public class
ArrayQueue extends AbstractQueue {

    private int begin;
    private Object[] array;

    public ArrayQueue() {
        array = new Object[100];
    }

    public class ArrayQueueIterator implements Iterator<Object> {
        int size, begin, pointer;

        public ArrayQueueIterator(ArrayQueue q) {
            size = q.size();
            begin = q.begin;
            pointer = 0;
        }

        @Override
        public boolean hasNext() {
            return pointer < size;
        }

        @Override
        public Object next() {
            Object x = array[(begin + pointer++) % array.length];
            return x;
        }
    }

    @Override
    public Iterator iterator() {
        return new ArrayQueueIterator(this);
    }

    private int end() {
        return (begin + size) % array.length;
    }

    private void resize() {
        Object[] temp = new Object[size];
        System.arraycopy(array, begin, temp, 0, array.length - begin);
        if (begin != 0) {
            System.arraycopy(array, 0, temp, array.length - begin, end());
        }

        array = Arrays.copyOf(temp, 2 * size);
        begin = 0;
    }

    @Override
    public void enqueueImplementation(Object x) {
        if (size > array.length - 1) {
            resize();
        }
        array[end()] = x;
    }

    @Override
    public Object dequeueImplementation() {
        Object x = array[begin];
        array[begin] = null;
        begin = ++begin % array.length;

        if (size - 1 == 0) {
            begin = 0;
        }
        return x;
    }

    @Override
    public Object elementImplementation() {
        return array[begin];
    }

    @Override
    public void pushImplementation(Object x) {
        if (size == array.length) {
            resize();
        }
        begin = begin - 1 < 0 ? array.length - 1 : begin - 1;
        array[begin] = x;
    }

    @Override
    public Object peekImplementation() {
        return array[(end() - 1 < 0 ? array.length - 1 : end() - 1)];
    }

    @Override
    public Object removeImplementation() {
        int index = end() - 1 < 0 ? array.length - 1 : end() - 1;
        Object x = array[index];
        array[index] = null;
        return x;
    }

    @Override
    public void clearImplementation() {
        array = new Object[100];
        begin = 0;
    }

    @Override
    protected Queue createQueue() {
        return new ArrayQueue();
    }

}
