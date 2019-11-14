import java.util.*;
import java.io.*;

public class C {

    public static class Node {
        char value;
        Node next;
        Node prev;

        Node(char value, Node prev, Node next) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }

        Node(char value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }

    public static class BestListEver {
        Node begin;
        Node end;

        BestListEver(Node node) {
            this.begin = node;
            this.end = node;
        }

        int findAndMove(char c) {
            Node current = this.begin;
            int iter = 0;

            while (current != null) {
                if (current.value == c) {
                    Node prev = current.prev;
                    Node next = current.next;
                    if (prev == null) {
                        return 1;
                    } 
                    prev.next = next;
                    if (next != null) {
                        next.prev = prev;
                    }
                    pushFront(current);
                    return iter + 1;
                }
                current = current.next;
                ++iter;
            }
            return -1;
        }

        void pushFront(Node node) {
            node.prev = null;
            node.next = this.begin;
            this.begin.prev = node;
            this.begin = node;
        }

        void pushBack(Node node) {
            node.prev = this.end;
            this.end.next = node;
            this.end = node;
        }

    }
    public static void main(String [] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("mtf.in"));
        String s = scanner.next();
        scanner.close();

        BestListEver list = new BestListEver(new Node('a'));
        for (int i = 1; i < 26; ++i) {
            list.pushBack(new Node((char)('a' + i)));
        }

        try {
            OutputStream writer = new FileOutputStream("mtf.out");
            for (int i = 0; i < s.length(); ++i) {
                int res = list.findAndMove(s.charAt(i));
                String sRes = Integer.toString(res);
                for (int j = 0; j < sRes.length(); ++j) {
                    writer.write(sRes.charAt(j));
                }
                writer.write(' ');
            }
            writer.close();
        } catch (IOException e) {
            //trolling occured
        }

    }
}