package info.kgeorgiy.ja.samsikova.arrayset;

import java.util.*;
import java.util.function.IntFunction;


public class ArraySet<T> extends AbstractSet<T> implements NavigableSet<T> {
    private final List<T> elements;
    private final Comparator<? super T> comparator;

    public ArraySet(Collection<? extends T> elements, Comparator<? super T> comparator) {
        Set<T> tempSet = new TreeSet<>(comparator);
        tempSet.addAll(elements);
        this.elements = List.copyOf(tempSet);
        this.comparator = comparator;
    }

    public ArraySet() {
        this.elements = Collections.emptyList();
        this.comparator = null;
    }

    public ArraySet(Collection<? extends T> elements) {
        this(elements, null);
    }

    private int getIndex(T o) {
        return Collections.binarySearch(elements, o, comparator);
    }

    private T getElementByIndex(int index) {
        if (index < 0 || index >= size()) return null;
        return elements.get(index);
    }

    private int getConditionalIndex(T o, IntFunction<Integer> negative, IntFunction<Integer> positive) {
        int index = getIndex(o);
        if (index < 0) {
            return negative.apply(index);
        }
        return positive.apply(index);
    }

    private int getLowerIndex(T o, boolean inclusive) {
        if (!inclusive) {
            return getConditionalIndex(o, (x -> -(x + 1) - 1), x -> x - 1);
        }
        return getConditionalIndex(o, (x -> -(x + 1) - 1), x -> x);
    }

    private int getHigherIndex(T o, boolean inclusive) {
        if (!inclusive) {
            return getConditionalIndex(o, x -> -(x + 1), x -> x + 1);
        }
        return getConditionalIndex(o, x -> -(x + 1), x -> x);
    }

    @Override
    public T lower(T o) {
        int index = getLowerIndex(o, false);
        return getElementByIndex(index);
    }

    @Override
    public T floor(T o) {
        int index = getLowerIndex(o, true);
        return getElementByIndex(index);
    }

    @Override
    public T ceiling(T o) {
        int index = getHigherIndex(o, true);
        return getElementByIndex(index);
    }

    @Override
    public T higher(T o) {
        int index = getHigherIndex(o, false);
        return getElementByIndex(index);
    }

    @Override
    public T pollFirst() {
        throw unsupportedOperationException();
    }

    @Override
    public T pollLast() {
        throw unsupportedOperationException();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        T element = (T) o;
        return getIndex(element) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }


    private UnsupportedOperationException unsupportedOperationException() {
        return new UnsupportedOperationException("ArraySet cannot be modified");
    }

    @Override
    public NavigableSet<T> descendingSet() {
        return new ArraySet<>(new ReversedOrderList<>(elements), Collections.reverseOrder(comparator));
    }

    private static class ReversedOrderList<U> extends AbstractList<U> {
        private final boolean reversed;
        private final List<U> elements;

         ReversedOrderList(List<U> elements) {
            if (elements instanceof ReversedOrderList) {
                ReversedOrderList<U> reversedElements = (ReversedOrderList<U>) elements;
                this.reversed = !reversedElements.reversed;
                this.elements = reversedElements.elements;
                return;
            }
            this.reversed = true;
            this.elements = elements;
        }

        @Override
        public int size() {
            return elements.size();
        }

        @Override
        public U get(int index) {
             return elements.get(reversed ? size() - 1 - index : index);
        }
    }

    @Override
    public Iterator<T> descendingIterator() {
        return descendingSet().iterator();
    }

    @SuppressWarnings("unchecked")
    private int compare(T x, T y) {
        return comparator == null ? ((Comparable<? super T>) x).compareTo(y)
                : comparator.compare(x, y);
    }

    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException("first element is greater than last element");
        }
        return generateSubsetNoThrow(fromElement, toElement, fromInclusive, toInclusive);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        if (isEmpty()) {
            return this;
        }
        return generateSubsetNoThrow(first(), toElement, true, inclusive);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        if (isEmpty()) {
            return this;
        }
        return generateSubsetNoThrow(fromElement, last(), inclusive, true);
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }

    private NavigableSet<T> generateSubsetNoThrow(T fromElement, T toElement, boolean fromInclusive, boolean toInclusive) {
        int begin = getHigherIndex(fromElement, fromInclusive);
        int end =  getLowerIndex(toElement, toInclusive);
        return new ArraySet<>(begin > end ? List.of() : elements.subList(begin, end + 1), comparator);
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator == Comparator.naturalOrder() ? null : comparator;
    }

    private NoSuchElementException noSuchElementException() {
        return new NoSuchElementException("Set is empty");
    }

    @Override
    public T first() {
        if (isEmpty()) throw noSuchElementException();
        return elements.get(0);
    }

    @Override
    public T last() {
        if (isEmpty()) throw noSuchElementException();
        return elements.get(elements.size() - 1);
    }
}
