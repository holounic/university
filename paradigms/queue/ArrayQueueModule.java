package queue;

import java.util.Arrays;

public class ArrayQueueModule {
    private static int begin = 0;
    private static int size = 0;
    private static Object[] array = new Object[3];

    private static int end() {
        return (begin + size) % array.length;
    }

    public static void enqueue(Object x) {
        assert x != null;
        if (size == array.length) {
            resize();
        }
        array[end()] = x;
        size++;
    }

    //Prev: true
    private static void resize() {
        Object[] temp = new Object[size];
        System.arraycopy(array, begin, temp, 0, array.length - begin);
        if (begin != 0) {
            System.arraycopy(array, 0, temp, array.length - begin, end());
        }
        array = Arrays.copyOf(temp, 2 * size);
        begin = 0;
    }
    /* Post: array.length = array'.length * 2 && begin = 0 && end = size
     && forall i = 0... size: array[i] = array'[(i + begin) % array'.length]
    */

    public static Object dequeue() {
        if (size == 0) {
            return null;
        }
        Object x = array[begin];
        array[begin] = null;
        begin = (begin + 1) % array.length;

        if (--size == 0) {
            begin = 0;
        }
        return x;
    }

    public static Object element() {
        assert size > 0;
        return array[begin];
    }

    public static int size() {
        return size;
    }

    public static boolean isEmpty() {
        return size == 0;
    }

    public static void clear() {
        array = new Object[10];
        begin = 0;
        size = 0;
    }

    public static void push(Object x) {
        assert x != null;
        if (size == array.length) {
            resize();
        }
        begin = begin - 1 < 0 ? array.length - 1 : begin - 1;
        array[begin] = x;
        size++;
    }

    public static Object peek() {
        assert size > 0;
        return array[(end() - 1 < 0 ? array.length - 1 : end() - 1)];
    }

    public static Object remove() {
        assert size > 0;
        int index = end() - 1 < 0 ? array.length - 1 : end() - 1;
        Object x = array[index];
        array[index] = null;
        size--;
        return x;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (size == 0) {
            return "null";
        }
        int i = begin;
        do {
            builder.append((array[i] == null ? "null" : array[i].toString()) + " ");
            i = ++i % array.length;
        } while (i != end());
        return builder.toString();
    }
}
