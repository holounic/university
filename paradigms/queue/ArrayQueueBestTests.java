package queue;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class ArrayQueueBestTests {
    private static final int testNumber = 100;
    private static Mode mode;
    private static Object queue;

    private static enum Mode {
        MODULE, ADT, CLASS
    }

    public static void main(String[] args) {
        log("тестируем");
        generalTest();
        log("мы выжили");
    }

    public static final Object[] objects = {
            "random", "not random", "oooopsy", 0.1, 5, "BAGLE RAT"
    };

    private static int getQueueSize() {
        switch (mode) {
            case MODULE:
                return ArrayQueueModule.size();
            case ADT:
                return ArrayQueueADT.size((ArrayQueueADT)queue);
            case CLASS:
                return ((ArrayQueue)queue).size();
            default:
                assert false;
                return 0;
        }
    }

    private static void enqueueQueue(Object argument) {
        switch (mode) {
            case MODULE:
                ArrayQueueModule.enqueue(argument);
                return;
            case ADT:
                ArrayQueueADT.enqueue((ArrayQueueADT)queue, argument);
                return;
            case CLASS:
                ((ArrayQueue)queue).enqueue(argument);
            default:
                assert false;
        }
    }

    private static Object checkDequeue() {
        switch (mode) {
            case MODULE:
                return ArrayQueueModule.dequeue();
            case ADT:
                return ArrayQueueADT.dequeue((ArrayQueueADT)queue);
            case CLASS:
                ((ArrayQueue)queue).dequeue();
            default:
                assert false;
                return null;
        }
    }

    private static void generalTest() {
        testModule();
        testADT();
        testClass();
    }

    private static void testModule() {
        mode = Mode.MODULE;
        test();
    }

    private static void testADT() {
        mode = Mode.ADT;
        queue = new ArrayQueueADT();
        test();
    }

    private static void testClass() {
        mode = Mode.MODULE;
        queue = new ArrayQueue();
        test();
    }

    private static void test() {
        log("Testing " + mode.name());

        Queue<Object> control = new ArrayDeque<>();
        Random randGenerator = new Random();
        int[] statistics = new int[3];

        for (int i = 0; i < testNumber; i++) {
            switch (randGenerator.nextInt(3)) {
                case 0:
                    assert getQueueSize() == control.size();
                    ArrayQueueModule.clear();
                    control.clear();
                    statistics[0]++;
                    break;
                case 1:
                        assert getQueueSize() == control.size();
                        enqueueQueue(objects[i % objects.length]);
                        ArrayQueueModule.enqueue(objects[i % objects.length]);
                        control.add(objects[i % objects.length]);
                        statistics[1]++;
                        break;
                default:
                    if (control.size() == 0) {
                        assert getQueueSize() == 0;
                        break;
                    }
                    statistics[2]++;
                    assert checkDequeue().equals(control.remove());
            }
        }
        log("Successful\n" + info("size&clear", statistics[0])
                + info("enqueue", statistics[1]) + info("dequeue", statistics[2]));
    }

    private static String info(String type, int n) {
        return new StringBuilder(type + " tested " + n + " times\n").toString();
    }

    private static void log(Object m) {
        System.out.println(m.toString());
    }
}
