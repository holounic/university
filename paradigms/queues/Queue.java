package queue;

import java.util.function.Function;
import java.util.function.Predicate;

//define: Q := queue, {} := multiset
//inv: size >= 0
// Queue = {e_1, e_2...e_n} not null
public interface Queue {

    //Pre: true
    //Post: Q = {(elements of Q'), x} && |Q| = |Q'| + 1
    void enqueue(Object x);

    //Pre: |Q| > 0
    //Post: R = e_start && |Q| = |Q'| - 1 && Q' = {e_2, e_3 ... e_n}
    Object dequeue();

    //Pre: |Q| > 0
    //Post: R = e_start && Q = Q'(queue does not change)
    Object element();

    //Pre: true
    //Post: R = |Q| && Q = Q'(queue does not change)
    int size();

    //Pre: true
    //Post: R = (|Q| == 0 ? true : false) && Q = Q'(queue does not change)
    boolean isEmpty();

    //Pre: x != null
    //Post: |Q| = |Q'| + 1 && Q = {x, (elements of Q')}
    void push(Object x);

    //Pre: |Q| > 0
    //Post: R = last added element of Q && Q = Q'(queue does not change)
    Object peek();

    //Pre: |Q| > 0
    //Post: R = last added element of Q && Q = Q' \ last added element of Q' && |Q| = |Q'| - 1
    Object remove();

    //Pre: true
    //Post: |Q| = 0
    void clear();

    //Pre: |Q| > 0 && f != null
    //Post: R = {f(e_1), f(e_2) ... f(e_n)} && Q = Q'(queue does not change)
    Queue map(Function<Object, Object> f);

    //Pre: |Q| > 0 && p != null
    //Post: R = (|Q| > 0 ? {el_k...el_j | k < j && forall i: el_i in R : p(el_i) = true} : {} ) && Q = Q'(queue does not change))
    Queue filter(Predicate<Object> p);

}
