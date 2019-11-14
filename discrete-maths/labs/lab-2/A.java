import java.util.*;
import java.io.*;

public class A {
    static class Node implements Comparable<Node> {
        final long count;
        public Node(long count) {
            this.count = count;
        }
        @Override
        public int compareTo(Node node) {
            return Long.compare(count, node.count);
        }
    }
    static class InternalNode extends Node {
        Node left;
        Node right;
        public InternalNode(Node left, Node right) {
            super(left.count + right.count);
            this.left = left;
            this.right = right;
        }
    }
    static class Leaf extends Node {
        public Leaf(long count) {
            super(count);
        }
    }
    public static void main(String [] args) throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(new File("huffman.in"));
        int n = scanner.nextInt();
        int[] freq = new int [n];
        for (int i = 0; i < n; ++i) {
            freq[i] = scanner.nextInt();
        }
        scanner.close();
        PriorityQueue<Node> queue = new PriorityQueue<>();

        for (int i = 0; i < n; ++i) {
            queue.add(new Leaf(freq[i]));
        }
        long sum = 0;
        while (queue.size() > 1) {
            Node first = queue.poll();
            Node second = queue.poll();
            InternalNode node =  new InternalNode(first, second);
            sum += node.count;
            queue.add(node);
        }
        String res = Long.toString(sum);
        try {
            OutputStream writer = new FileOutputStream("huffman.out");
            for (int i = 0; i < res.length(); ++i) {
                writer.write(res.charAt(i));
            }
        } catch (IOException e) {
            //ooopsy!(((((((( trolling occured
        }
    }
}