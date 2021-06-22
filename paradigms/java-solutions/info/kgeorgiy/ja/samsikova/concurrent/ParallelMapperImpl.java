package info.kgeorgiy.ja.samsikova.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParallelMapperImpl implements ParallelMapper {

    private final List<Thread> workers;
    private final Queue<Runnable> tasks = new ArrayDeque<>();
    private static final boolean PRINT_STACKTRACE = false;

    public ParallelMapperImpl(int threads) {
       Runnable runner = () -> {
           try {
               while (!Thread.interrupted()) {
                   nextTask().run();
               }
           } catch (final InterruptedException e) {
               if (PRINT_STACKTRACE) {
                   e.printStackTrace();
               }
           } finally {
               Thread.currentThread().interrupt();
           }
       };
       workers = Stream.generate(() -> new Thread(runner))
               .limit(threads)
               .collect(Collectors.toList());
       workers.forEach(Thread::start);
    }

    private Runnable nextTask() throws InterruptedException {
        Runnable task;
        synchronized (tasks) {
            while (tasks.isEmpty()) {
                tasks.wait();
            }
            task = tasks.poll();
        }
        return task;
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        MappingList<T, R> ml = new MappingList<>(f, args.size());
        IntStream.range(0, args.size())
                .forEach(i -> addTask(() -> ml.set(i, args.get(i))));
        return ml.await();
    }

    private void addTask(Runnable task) {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
    }

    @Override
    public void close() {
        workers.forEach(Thread::interrupt);
        for (Thread thread : workers) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                if (PRINT_STACKTRACE) {
                   e.printStackTrace();
               }
            }
        }
    }

    private static class MappingList<T, R> {
        private final List<R> result;
        private final Function<? super T, ? extends R> f;
        private int remaining;

        public MappingList(Function<? super T, ? extends R> f, int size) {
            this.result = new ArrayList<>(Collections.nCopies(size, null));
            this.f = f;
            this.remaining = result.size();
        }

        public void set(int index, T value) {
            R res = f.apply(value);
            setValue(index, res);
        }

        private synchronized void setValue(int index, R value) {
            result.set(index, value);
            remaining--;
            if (remaining == 0) {
                notify();
            }
        }

        public synchronized List<R> await() throws InterruptedException {
            while (remaining > 0) {
                wait();
            }
            return result;
        }
    }
}
