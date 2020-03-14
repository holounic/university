package queue;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.Function;

public abstract class AbstractQueue implements Queue, Iterable {
    protected int size = 0;

    private void increaseSize() {
        size++;
    }

    private void decreaseSize() {
        size--;
    }

    protected abstract void enqueueImplementation(Object x);

    public void enqueue(Object x) {
        assert x != null;
        enqueueImplementation(x);
        increaseSize();
    }

    protected abstract Object dequeueImplementation();

    public Object dequeue() {
        assert size > 0;
        Object x = dequeueImplementation();
        decreaseSize();
        return x;
    }

    protected abstract Object elementImplementation();

    public Object element() {
        assert size > 0;
        return elementImplementation();
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    protected abstract void pushImplementation(Object x);

    public void push(Object x) {
        assert x != null;
        pushImplementation(x);
        increaseSize();
    }

    protected abstract Object peekImplementation();

    public Object peek() {
        assert size > 0;
        return peekImplementation();
    }

    protected abstract Object removeImplementation();
    public Object remove() {
        assert size > 0;
        Object x = removeImplementation();
        decreaseSize();
        return x;
    }

    protected abstract void clearImplementation();

    public void clear() {
        size = 0;
        clearImplementation();
    }

    protected abstract Queue createQueue();

    private Queue applyFunctions(final Expression f) {
        Queue filtered = createQueue();
        Iterator iter = iterator();

        while (iter.hasNext()) {
            Object x = f.applyMethod(iter.next());
            if (x != null) {
                filtered.enqueue(x);
            }
        }
        return filtered;
    }

    public Queue filter(final Predicate<Object> p) {
        assert p != null;
        Expression predicate = (x) -> p.test(x) ? x : null;
        return applyFunctions(predicate);
    }

    public Queue map(final Function<Object, Object> f) {
        assert f != null;
        Expression function = (x) -> f.apply(x);
        return applyFunctions(function);
    }
}
