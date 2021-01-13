import java.util.*;
import java.io.*;

public class D {

    public void out(String message) {
        try {
            PrintWriter out = new PrintWriter("check.out");
            out.print(message);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Trolling occured");
        }
    }

    public void no() {
        out("NO");
        System.exit(0);
    }

    public void yes() {
        out("YES");
    }

    public void herederity(BitSet set, Set<BitSet> all, Set<BitSet> checked) {
        if (checked.contains(set)) {
            return;
        }
        if (!all.contains(set)) {
            no();
        }
        for (int i = 0; i < set.size(); ++i) {
            if (!set.get(i)) continue;
            set.set(i, false);
            herederity(set, all, checked);
            set.set(i);
        }
        checked.add(set);
    }

    public void herederity(Set<BitSet> all) {
        Set<BitSet> checked = new HashSet<>();
        for (BitSet set : all) {
            herederity(set, all, checked);
        }
    }

    public void augmentation(BitSet s1, BitSet s2, Set<BitSet> all) {
        BitSet cmpr = new BitSet(s2.size());
        for (int i = 0; i < s2.size(); ++i) {
            cmpr.set(i, s2.get(i));
        }
        cmpr.andNot(s1);
        for (int i = 0; i < s2.size(); ++i) {
            if (!cmpr.get(i) || s1.get(i)) continue;
            s1.set(i);
            if (all.contains(s1)) {
                s1.set(i, false);
                return;
            }
            s1.set(i, false);
        }
        no();
    }

    public void augmentation(Set<BitSet> all) {
        for (BitSet s1 : all) {
            for (BitSet s2 : all) {
                if (s1.cardinality() >= s2.cardinality()) continue;
                augmentation(s1, s2, all);
            }
        }
    }

    public void run() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("check.in"))));
        StringTokenizer tokenizer = new StringTokenizer(in.readLine());

        int n = Integer.parseInt(tokenizer.nextToken());
        int m = Integer.parseInt(tokenizer.nextToken());
        boolean has_empty = false;

        Set<BitSet> all = new HashSet<>();
        while (m > 0) {
            tokenizer = new StringTokenizer(in.readLine());
            int l = Integer.parseInt(tokenizer.nextToken());
            if (l == 0) has_empty = true;
            BitSet set = new BitSet(n);
            while (l > 0) {
                int e = Integer.parseInt(tokenizer.nextToken());
                set.set(e);
                --l;
            }
            all.add(set);
            --m;
        }
        if (!has_empty) no();
        herederity(all);
        augmentation(all);
        yes();
    }
    public static void main(String[] args) throws Exception {
        new D().run();
    }
}