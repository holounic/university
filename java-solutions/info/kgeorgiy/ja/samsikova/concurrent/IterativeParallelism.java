package info.kgeorgiy.ja.samsikova.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IterativeParallelism implements ListIP {

    private final ParallelMapper mapper;

    public IterativeParallelism() {
        this.mapper = null;
    }

    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String join(int threads, List<?> values) throws InterruptedException {
        return processParallel(threads, values,
                list -> list.stream().map(Object::toString).collect(Collectors.joining()),
                list -> String.join("", list));
    }

    @Override
    public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return processParallel(threads, values,
                list -> list.stream().filter(predicate).collect(Collectors.toList()),
                this::collectToList);
    }

    @Override
    public <T, U> List<U> map(int threads, List<? extends T> values, Function<? super T, ? extends U> f) throws InterruptedException {
        return processParallel(threads, values,
                list -> list.stream().map(f).collect(Collectors.toList()),
                this::collectToList);
    }

    private <T> List<T> collectToList(List<? extends List<? extends T>> values) {
        return values.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return processParallel(threads, values,
                list -> Collections.max(list, comparator),
                list -> Collections.max(list, comparator));
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, Collections.reverseOrder(comparator));
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return processParallel(threads, values,
                list -> list.stream().allMatch(predicate),
                list -> list.stream().allMatch(b -> b));
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, predicate.negate());
    }

    private <T, M, R> R processParallel(int threads, List<T> values, Function<List<T>, M> reducePartFunction, Function<List<M>, R> reduceFinalFunction) throws InterruptedException {
        List<List<T>> parts = split(threads, values);
        List<M> partsRes = mapper == null
                ? processPartsParallel(reducePartFunction, parts)
                : mapper.map(reducePartFunction, parts);
        return reduceFinalFunction.apply(partsRes);
    }

    private <T, R> List<R> processPartsParallel(Function<List<T>, R> reduceFunction, List<List<T>> parts) throws InterruptedException {
        List<R> res = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            res.add(null);
        }
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            int finalI = i;
            Thread thread = new Thread(() -> res.set(finalI, reduceFunction.apply(parts.get(finalI))));
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return res;
    }

    private <T> List<List<T>> split(int parts, List<T> values) {
        final int partSize = values.size() / parts;
        final int rest = values.size() % parts;

        final List<List<T>> res = new ArrayList<>();
        int start = 0;
        int end;
        for (int i = 0; i < Math.min(parts, values.size()); i++, start = end) {
            end = start + partSize;
            if (i < rest) {
                end++;
            }
            res.add(values.subList(start, end));
        }
        return res;
    }
}
