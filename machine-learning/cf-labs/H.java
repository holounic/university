import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class H {
    private static final double DEFAULT_INIT = 0.0;

    private static final String VAR = "var";
    private static final String TNH = "tnh";
    private static final String RLU = "rlu";
    private static final String MUL = "mul";
    private static final String SUM = "sum";
    private static final String HAD = "had";
    private static BufferedReader in;

    public static void main(String[] args) throws IOException {
        in = new BufferedReader(new InputStreamReader(System.in));
        String[] line = in.readLine().trim().split(" +");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);
        int k = Integer.parseInt(line[2]);
        List<Module> nodes = new ArrayList<>();
        outer: for (int i = 0; i < n; i++) {
            line = in.readLine().trim().split(" +");
            String t = line[0];
            switch (t) {
                case VAR:
                    nodes.add(new Module(Integer.parseInt(line[1]), Integer.parseInt(line[2]), DEFAULT_INIT));
                    continue outer;
                case TNH:
                    nodes.add(new TanHFun(nodes.get(Integer.parseInt(line[1]) - 1)));
                    continue outer;
                case RLU:
                    nodes.add(new ReLUFun(nodes.get(Integer.parseInt(line[2]) - 1), Integer.parseInt(line[1])));
                    continue outer;
                case MUL:
                    nodes.add(new Mul(new ModulePair(nodes.get(Integer.parseInt(line[1]) - 1), nodes.get(Integer.parseInt(line[2]) - 1))));
                    continue outer;
                case SUM:
                    var curNodes = new ArrayList<Module>();
                    for (int j = 0; j < Integer.parseInt(line[1]); j++) {
                        curNodes.add(nodes.get(Integer.parseInt(line[2 + j]) - 1));
                    }
                    nodes.add(new Sum(curNodes));
                    continue outer;
                case HAD:
                    curNodes = new ArrayList<Module>();
                    for (int j = 0; j < Integer.parseInt(line[1]); j++) {
                        curNodes.add(nodes.get(Integer.parseInt(line[2 + j]) - 1));
                    }

                    nodes.add(new MultyMul(curNodes));
            }
        }
        for (int v = 0; v < m; v++) nodes.get(v).vals.fill();
        for (int i = 0; i < k; i++) nodes.get(n - k + i).df.fill();

        nodes.forEach(Module::vals);

        for (int i = n - 1; i >= 0; i--) nodes.get(i).diff();

        for (int i = 0; i < k; i++) System.out.println(nodes.get(n + i - k).vals);
        for (int i = 0; i < m; i++) System.out.println(nodes.get(i).df);
    }

    private static class Matrix {
        final List<List<Double>> inner;
        final int w, h;

        public Matrix(int w, int h, double init) {
            this.w = w;
            this.h = h;
            inner = new ArrayList<>();
            for (int i = 0; i < w; i++) {
                var row = new ArrayList<Double>();
                for (int j = 0; j < h; j++) {
                    row.add(init);
                }
                inner.add(row);
            }
        }

        public List<Double> get(int i) {
            return inner.get(i);
        }

        public double get(int i, int j) {
            return get(i).get(j);
        }

        public void set(int i, int j, double val) {
            get(i).set(j, val);
        }

        public void mul(int i, int j, double factor) {
            set(i, j, get(i).get(j) * factor);
        }

        public void add(int i, int j, double delta) {
            set(i, j, get(i).get(j) + delta);
        }

        private void fill() throws IOException {
            for (int i = 0; i < w; i++) {
                String[] line = in.readLine().trim().split(" +");
                for (int j = 0; j < h; j++) {
                    this.set(i, j, Integer.parseInt(line[j]));
                }
            }
        }

        @Override
        public String toString() {
            return inner.stream()
                    .map(cell -> cell.stream()
                            .map(d -> String.format(Locale.US, "%.5f", d))
                            .collect(Collectors.joining(" ")))
                    .collect(Collectors.joining("\n"));
        }
    }

    private static class Module {
        final Matrix vals, df;

        public Module(int w, int h, double init) {
            vals = new Matrix(w, h, init);
            df = new Matrix(w, h, DEFAULT_INIT);
        }
        void vals() {}
        void diff() {}
    }

    private static class ContainerModule<T> extends Module {
        final T inner;

        public ContainerModule(int w, int h, double init, T inner) {
            super(w, h, init);
            this.inner = inner;
        }

        public ContainerModule(int w, int h, T inner) {
            super(w, h, DEFAULT_INIT);
            this.inner = inner;
        }
    }

    private static class TanHFun extends ContainerModule<Module> {
        public TanHFun(Module arg) {
            super(arg.vals.w, arg.vals.h, arg);
        }

        @Override
        public void vals() {
            for (int i = 0; i < vals.w; i++) {
                for (int j = 0; j < vals.h; j++) {
                    vals.set(i, j, Math.tanh(inner.vals.get(i, j)));
                }
            }
        }

        @Override
        public void diff() {
            for (int i = 0; i < inner.df.w; i++) {
                for (int j = 0; j < inner.df.h; j++) {
                    inner.df.add(i, j, (1 - vals.get(i, j) * vals.get(i, j)) * df.get(i, j));
                }
            }
        }
    }

    private static class ReLUFun extends ContainerModule<Module> {
        final double alpha;

        public ReLUFun(Module arg, double alpha) {
            super(arg.vals.w, arg.vals.h, arg);
            this.alpha = alpha;
        }

        @Override
        public void vals() {
            for (int i = 0; i < vals.w; i++) {
                for (int j = 0; j < vals.h; j++) {
                    double v = inner.vals.get(i, j);
                    vals.set(i, j, v < 0 ? v / alpha : v);
                }
            }
        }

        @Override
        public void diff() {
            for (int i = 0; i < inner.df.w; i++) {
                for (int j = 0; j < inner.df.h; j++) {
                    double v = inner.vals.get(i, j);
                    inner.df.add(i, j, v < 0 ? df.get(i, j) / alpha : df.get(i, j));
                }
            }
        }
    }

    private static class ModulePair {
        final Module a, b;
        public ModulePair(Module a, Module b) {
            this.a = a;
            this.b = b;
        }
    }

    private static class Mul extends ContainerModule<ModulePair> {
        public Mul(ModulePair pair) {
            super(pair.a.vals.w, pair.b.vals.h, pair);
        }

        @Override
        public void vals() {
            for (int i = 0; i < inner.a.vals.w; i++) {
                for (int j = 0; j < inner.a.vals.h; j++) {
                    for (int k = 0; k < inner.b.vals.h; k++) {
                        vals.add(i, k, inner.a.vals.get(i, j) * inner.b.vals.get(j, k));
                    }
                }
            }
        }

        public void iterateDiff(Module m1, Module m2, int maxI, int maxJ, int maxK, boolean rev) {
            for (int i = 0; i < maxI; i++) {
                for (int j = 0; j < maxJ; j++) {
                    for (int k = 0; k < maxK; k++) {
                        m1.df.add(i, j,
                                rev ? df.get(k, j) * m2.vals.get(k, i)
                                        : df.get(i, k) * m2.vals.get(j, k));
                    }
                }
            }
        }

        @Override
        public void diff() {
            iterateDiff(inner.a, inner.b, inner.a.df.w, inner.a.df.h, inner.b.vals.h, false);
            iterateDiff(inner.b, inner.a, inner.a.df.h, inner.b.vals.h, inner.a.df.w, true);
        }
    }

    private static class Sum extends ContainerModule<List<Module>> {
        public Sum(List<Module> arg) {
            super(arg.get(0).vals.w, arg.get(0).vals.h, arg);
        }

        private static void update(Matrix to, Matrix from) {
            for (int i = 0; i < to.w; i++) {
                for (int j = 0; j < to.h; j++) {
                    to.add(i, j, from.get(i, j));
                }
            }
        }

        @Override
        public void vals() {
            inner.forEach(a -> {
                update(vals, a.vals);
            });
        }

        @Override
        public void diff() {
            inner.forEach(a -> {
                update(a.df, df);
            });
        }
    }

    private static class MultyMul extends ContainerModule<List<Module>> {
        public MultyMul(List<Module> args) {
            super(args.get(0).vals.w, args.get(0).vals.h, 1, args);
        }

        @Override
        public void vals() {
            inner.forEach(a -> {
                for (int i = 0; i < a.vals.w; i++) {
                    for (int j = 0; j < a.vals.h; j++) vals.mul(i, j, a.vals.get(i, j));
                }
            });
        }

        @Override
        public void diff() {
            double acc;
            for (int m1 = 0; m1 < inner.size(); m1++) {
                for (int i = 0; i < inner.get(m1).df.w; i++) {
                    for (int j = 0; j < inner.get(m1).df.h; j++) {
                        acc = 1.;
                        for (int m2 = 0; m2 < inner.size(); m2++) acc *= m1 == m2 ? 1 : inner.get(m2).vals.get(i, j);
                        inner.get(m1).df.add(i, j, acc * df.get(i, j));
                    }
                }
            }
        }
    }
}