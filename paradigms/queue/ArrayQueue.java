package queue;

import java.util.Arrays;

//inv: size >= 0 && begin >= 0 && end >= 0 for i: begin...end array[i] != null
public class ArrayQueue {
    private int begin, size;
    private Object[] array;

    //Pre: true
    public ArrayQueue() {
        array = new Object[100];
    }
    //Post: array.length = 100

    private int end() {
        return (begin + size) % array.length;
    }

    //Pre: size = array.length
    private void resize() {
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


    //Pre: true
    public void enqueue(Object x) {
        assert x != null;
        if (size == array.length) {
            //Pre: size = array.length
            resize();
            //Post: array.length = size * 2
        }

        //Pre: size < array.length && end < array.length
        array[end()] = x;
        size++;
        //Post: array[end'] = x && end = (end' + 1) % array.length && size = size' + 1
    }
    //Post: array[end'] = x

    //Pre: size > 0
    public Object dequeue() {
        if (size == 0) {
            return null;
        }
        //Pre: true
        Object x = array[begin];
        array[begin] = null;
        begin = ++begin % array.length;
        //Post: begin = (begin' + 1) % array.length && R = array[begin']

        //Pre: size = 1
        if (--size == 0) {
            begin = 0;
        }
        //Post: size = 0 && begin = 0 && end = 0
        return x;
    }
    //Post: begin = (begin' + 1) && R = array[begin'] && size = size' - 1


    //Pre: size > 0
    public Object element() {
        if (size == 0) {
            return null;
        }
        return array[begin];
    }
    //Post: R = array[begin]

    //Pre: true
    public int size() {
        return size;
    }
    //Post: R = size

    //Pre: true
    public boolean isEmpty() {
        return size == 0;
    }
    //Post: R = (size == 0 ? true : false)


    //Pre: true
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
    //Post: forall i = begin...end:

    //Pre: true
    public void push(Object x) {
        assert x != null;
        if (size == array.length) {
            resize();
        }
        begin = begin - 1 < 0 ? array.length - 1 : begin - 1;
        array[begin] = x;
        size++;
    }
    //Post: size = size' + 1 && array[begin] = x && begin = begin' - 1

    public Object peek() {
        assert size > 0;
        return array[(end() - 1 < 0 ? array.length - 1 : end() - 1)];
    }

    public Object remove() {
        assert size > 0;
        int index = end() - 1 < 0 ? array.length - 1 : end() - 1;
        Object x = array[index];
        array[index] = null;
        size--;
        return x;
    }

    public void clear() {
        array = new Object[100];
        begin = 0;
        size = 0;
    }
}
