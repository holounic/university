package queue;

import java.util.Arrays;
//define: e_start = the most recently added element from the undeleted ones
//inv: size >= 0 && begin >= 0 && end >= 0
// internal elements of Q are not null
public class ArrayQueueADT {
    private int begin = 0;
    private int size = 0;
    private Object[] array = new Object[3];

    //Pre: true
    //Post: R = index of last added element && Q = Q'(queue does not change)
    private static int end(ArrayQueueADT queue) {
        return (queue.begin + queue.size) % queue.array.length;
    }

    //Pre: true
    //Post: Q = Q'(queue does not change)
    private static void resize(ArrayQueueADT queue) {
        assert queue != null;
        Object[] temp = new Object[queue.size];
        System.arraycopy(queue.array, queue.begin, temp, 0, queue.array.length - queue.begin);
        if (queue.begin != 0) {
            System.arraycopy(queue.array, 0, temp, queue.array.length - queue.begin, end(queue));
        }
        queue.array = Arrays.copyOf(temp, 2 * queue.size);
        queue.begin = 0;
    }


    //Pre: queue != null && x != null
    //Post: |Q| = |Q'| + 1 && Q = {elements of Q', x}
    public static void enqueue(ArrayQueueADT queue, Object x) {
        assert queue != null && x != null;
        if (queue.size == queue.array.length) {
            resize(queue);
        }
        queue.array[end(queue)] = x;
        queue.size++;
    }

    //Pre: queue != null
    //Post: R = e_begin && |Q| = |Q'| - 1
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;

        Object x = queue.array[queue.begin];
        queue.array[queue.begin] = null;
        queue.begin = ++queue.begin % queue.array.length;
        if (--queue.size == 0) {
            queue.begin = 0;
        }
        return x;
    }

    //Pre: queue != null && |Q| > 0
    // Post: R = e_begin && Q = Q'(queue does not change)
    public static Object element(ArrayQueueADT queue) {
        if (queue.size == 0) {
            return null;
        }
        return queue.array[queue.begin];
    }


    //Pre: queue != null
    //Post: R = |Q| && Q = Q' (queue does not change)
    public static int size(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }


    //Pre: queue != null
    //Post: R = (|Q| == 0 ? true : false) && Q = Q' (queue does not change)
    public static boolean isEmpty(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size == 0;
    }


    //Pre: queue != null && x != null
    //Post: |Q| = |Q'| + 1 && Q = {elements of Q', x}
    public static void push(ArrayQueueADT queue, Object x) {
        assert queue != null && x != null;
        if (queue.size == queue.array.length) {
            resize(queue);
        }
        queue.begin = queue.begin - 1 < 0 ? queue.array.length - 1 : queue.begin - 1;
        queue.array[queue.begin] = x;
        queue.size++;
    }


    //Pre queue != null && |Q| > 0
    //Post: R = last added elemnts of Q && Q = Q' (queue does not change)
    public static Object peek(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        return queue.array[(end(queue) - 1 < 0 ? queue.array.length - 1 : end(queue) - 1)];
    }


    //Pre: queue != null && |Q| > 0
    //Post: R = last added elemnt of Q && |Q| = |Q'| - 1 && Q = {e_1 ... e_(n-1)}
    public static Object remove(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        int index = end(queue) - 1 < 0 ? queue.array.length - 1 : end(queue) - 1;
        Object x = queue.array[index];
        queue.array[index] = null;
        queue.size--;
        return x;
    }

    //Pre: queue != null
    //Post: |Q| = 0
    public static void clear(ArrayQueueADT queue) {
        assert queue != null;
        queue.array = new Object[3];
        queue.begin = 0;
        queue.size = 0;
    }

}
