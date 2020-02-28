package queue;

import java.util.Arrays;

public class ArrayQueueADT {
    private int begin = 0;
    private int size = 0;
    private Object[] array = new Object[3];

    private static int end(ArrayQueueADT queue) {
        return (queue.begin + queue.size) % queue.array.length;
    }

    //Pre: queue != null
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

    /* Post: array.length = array'.length * 2 && begin = 0 && end = size
     && forall i = 0... size: array[i] = array'[(i + begin) % array'.length]
    */

    //Pre: queue != null
    public static void enqueue(ArrayQueueADT queue, Object x) {
        assert queue != null && x != null;
        if (queue.size == queue.array.length) {
            resize(queue);
        }
        queue.array[end(queue)] = x;
        queue.size++;
    }
    //Post: queue.size = size' + 1 && queue.array[queue.end'] = x && queue.end = (1 + queue.end) % queue.array.length

    //Pre: queue != null
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
    //Post: R = queue.array[begin'] && queue.begin = (1 + queue.begin) % queue.array.length && size = size' - 1

    //Pre: queue != null && queue.size > 0
    public static Object element(ArrayQueueADT queue) {
        if (queue.size == 0) {
            return null;
        }
        return queue.array[queue.begin];
    }
    // Post: R = queue.array[begin]

    //Pre: queue != null
    public static int size(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }
    //Post: R = queue.size

    //Pre: queue != null
    public static boolean isEmpty(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size == 0;
    }
    //Post: R = (queue.size == 0 ? true : false)


    //Pre: queue != null
    public static void push(ArrayQueueADT queue, Object x) {
        assert queue != null && x != null;
        if (queue.size == queue.array.length) {
            resize(queue);
        }
        queue.begin = queue.begin - 1 < 0 ? queue.array.length - 1 : queue.begin - 1;
        queue.array[queue.begin] = x;
        queue.size++;
    }
    //Post: queue.begin = (queue.begin' - 1 < 0 ?  array.length - 1 : queue.begin' - 1) && queue.array[queue.begin] = x

    //Pre queue != null && queue.size > 0
    public static Object peek(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        return queue.array[(end(queue) - 1 < 0 ? queue.array.length - 1 : end(queue) - 1)];
    }
    //Post: R =  queue.array[(queue.end' - 1 < 0 ? queue.array.length - 1 : queue.end' - 1)]

    //Pre: queue != null && size > 0
    public static Object remove(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        int index = end(queue) - 1 < 0 ? queue.array.length - 1 : end(queue) - 1;
        Object x = queue.array[index];
        queue.array[index] = null;
        queue.size--;
        return x;
    }
    //Post: end = queue.end' - 1 < 0 ? queue.end' = queue.array.length - 1 : --queue.end && R = queue[end]

    public static String toString(ArrayQueueADT queue) {
        StringBuilder builder = new StringBuilder();
        if (queue.size == 0) {
            return "null";
        }
        int i = queue.begin;
        do {
            builder.append((queue.array[i] == null ? "null" : queue.array[i].toString()) + " ");
            i = ++i % queue.array.length;
        } while (i != end(queue));
        return builder.toString();
    }

    //Pre: queue != null
    public static void clear(ArrayQueueADT queue) {
        assert queue != null;
        queue.array = new Object[3];
        queue.begin = 0;
        queue.size = 0;
    }
    //Post: size = 0
}
