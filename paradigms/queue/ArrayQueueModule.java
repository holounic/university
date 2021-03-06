package queue;

import java.util.Arrays;
//define: e_start = the most recently added element from the undeleted ones
//inv: size >= 0
// internal elements of queue are not null
public class ArrayQueueModule {
    private static int begin = 0;
    private static int size = 0;
    private static Object[] array = new Object[3];

    //Pre: true
    //Post: R = index of last added element && Q = Q'(queue does not change)
    private static int end() {
        return (begin + size) % array.length;
    }


    //Pre: true
    //Post: Q = Q'(queue does not change)
    private static void resize() {
        Object[] temp = new Object[size];
        System.arraycopy(array, begin, temp, 0, array.length - begin);
        if (begin != 0) {
            System.arraycopy(array, 0, temp, array.length - begin, end());
        }
        array = Arrays.copyOf(temp, 2 * size);
        begin = 0;
    }

    //Pre: x != null
    //Post: |Q| = |Q'| + 1 && Q = {elements of Q', x}
    public static void enqueue(Object x) {
        assert x != null;
        if (size == array.length) {
            resize();
        }
        array[end()] = x;
        size++;
    }

    //Pre: true
    //Post: R = e_1 && |Q| = |Q'| - 1 && Q = {e'_2, ...e'n}
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

    //Pre: |Q| > 0
    // Post: R = e_1 && Q = Q'(queue does not change)
    public static Object element() {
        assert size > 0;
        return array[begin];
    }

    //Pre: true
    //Post: R = |Q| && Q = Q'(queue does not change)
    public static int size() {
        return size;
    }

    //Pre: true
    //Post: R = (|Q| == 0 ? true : false) && Q = Q'(queue does not change)
    public static boolean isEmpty() {
        return size == 0;
    }

    //Pre: true
    //Post: |Q| = 0
    public static void clear() {
        array = new Object[10];
        begin = 0;
        size = 0;
    }

    //Pre: x != null
    //Post: |Q| = |Q'| + 1 && Q = {elements of Q', x}
    public static void push(Object x) {
        assert x != null;
        if (size == array.length) {
            resize();
        }
        begin = begin - 1 < 0 ? array.length - 1 : begin - 1;
        array[begin] = x;
        size++;
    }

    //Pre |Q| > 0
    //Post: R = last added elemnts of Q && Q = Q'(queue does not change)
    public static Object peek() {
        assert size > 0;
        return array[(end() - 1 < 0 ? array.length - 1 : end() - 1)];
    }

    //Pre: |Q| > 0
    //Post: R = last added elemnt of Q && |Q| = |Q'| - 1 && Q = {e_1 ... e_(n-1)}
    public static Object remove() {
        assert size > 0;
        int index = end() - 1 < 0 ? array.length - 1 : end() - 1;
        Object x = array[index];
        array[index] = null;
        size--;
        return x;
    }

}
