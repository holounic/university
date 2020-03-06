package queue;

import java.util.Arrays;
//define: e_start = the most recently added element from the undeleted ones
//inv: size >= 0
// internal elements of queue are not null
public class ArrayQueue {
    private int begin, size;
    private Object[] array;

    public ArrayQueue() {
        array = new Object[100];
    }

    //Pre: true
    //Post: R = index of last added element && Q = Q'(queue does not change)
    private int end() {
        return (begin + size) % array.length;
    }

    //Pre: true
    //Post: Q = Q'(queue does not change)
    private void resize() {
        Object[] temp = new Object[size];

        System.arraycopy(array, begin, temp, 0, array.length - begin);
        if (begin != 0) {
            System.arraycopy(array, 0, temp, array.length - begin, end());
        }

        array = Arrays.copyOf(temp, 2 * size);
        begin = 0;

    }


    //Pre: true
    //Post: Q = {(elements of Q'), x} && |Q| = |Q'| + 1
    public void enqueue(Object x) {
        assert x != null;
        if (size == array.length) {
            resize();
        }

        array[end()] = x;
        size++;
    }

    //Pre: |Q| > 0
    //Post: R = e_1 && |Q| = |Q'| - 1 && Q' = {e_2, e_3 ... e_n}
    public Object dequeue() {
        assert size > 0;
        Object x = array[begin];
        array[begin] = null;
        begin = ++begin % array.length;

        if (--size == 0) {
            begin = 0;
        }
        return x;
    }


    //Pre: |Q| > 0
    //Post: R = e_start && Q = Q'(queue does not change)
    public Object element() {
        if (size == 0) {
            return null;
        }
        return array[begin];
    }


    //Pre: true
    //Post: R = |Q| && Q = Q'(queue does not change)
    public int size() {
        return size;
    }


    //Pre: true
    //Post: R = (|Q| == 0) && Q = Q'(queue does not change)
    public boolean isEmpty() {
        return size == 0;
    }

    //Pre: x != null
    //Post: |Q| = |Q'| + 1 && Q = {x, (elements of Q')}
    public void push(Object x) {
        assert x != null;
        if (size == array.length) {
            resize();
        }
        begin = begin - 1 < 0 ? array.length - 1 : begin - 1;
        array[begin] = x;
        size++;
    }

    //Pre: |Q| > 0
    //Post: R = last added element of Q && Q = Q'(queue does not change)
    public Object peek() {
        assert size > 0;
        return array[(end() - 1 < 0 ? array.length - 1 : end() - 1)];
    }

    //Pre: |Q| > 0
    //Post: R = last added element of Q && Q = {e_1 ... e_(end - 1)} && |Q| = |Q'| - 1
    public Object remove() {
        assert size > 0;
        int index = end() - 1 < 0 ? array.length - 1 : end() - 1;
        Object x = array[index];
        array[index] = null;
        size--;
        return x;
    }

    //Pre: true
    //Post: |Q| = 0
    public void clear() {
        array = new Object[100];
        begin = 0;
        size = 0;
    }

}
