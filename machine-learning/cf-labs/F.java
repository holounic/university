import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class F {

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		var lineCommon = in.readLine().trim().split(" +");
		var lineDs = in.readLine().trim().split(" +");

		int n = Integer.parseInt(lineDs[0]);
		int m = Integer.parseInt(lineCommon[0]);
		var entries = new ArrayList<Sample>();

		for (int i = 0; i < n; i++) {
			lineDs = in.readLine().trim().split(" +");
			var features = new ArrayList<Integer>();
			for (int k = 0; k < m + 1; k++) {
				int val = Integer.parseInt(lineDs[k]);
				if (k == m) {
					entries.add(new Sample(features, val - 1));
					continue;
				}
				features.add(val);
			}
		}
		in.close();

		var tree = new Tree(
				entries,
				Integer.parseInt(lineCommon[2]),
				n,
				Integer.parseInt(lineCommon[1]),
				m);
		System.out.println(tree.id);
		System.out.println(tree);
	}

	static int[] occurrence;
	public static int mostProbable(List<Sample> ds) {
		int solution = 0;
		Arrays.fill(occurrence, 0);
		for (Sample d : ds) {
			occurrence[d.y]++;
			if (occurrence[d.y] > occurrence[solution]) {
				solution = d.y;
			}
		}
		return solution;
	}

	private static class Tree {
		private int h, n, k, m, id = 0;
		Node root;

		public Tree(List<Sample> s, int h, int n, int k, int m) {
			this.h = h;
			this.n = n;
			this.k = k;
			this.m = m;
			occurrence = new int[k];
			root = train(new Leaf(id++, s, true), 0);
		}

		private Node train(Node node, int d) {
			Leaf curLeaf = (Leaf) node;
			if (d == h) {
				curLeaf.solution = mostProbable(curLeaf.samples);
				return curLeaf;
			}
			int minClass = Integer.MAX_VALUE, maxClass = 0;
			for (Sample entry : curLeaf.samples) {
				maxClass = Math.max(maxClass, entry.y);
				minClass = Math.min(minClass, entry.y);
			}
			if (minClass == maxClass) {
				curLeaf.solution = minClass;
				return curLeaf;
			}
			return split(curLeaf, d);
		}

		private Node fromLeaf(Leaf leaf, Node l, Node r, int splitDim, double splitValue) {
			return new TreeNode(leaf.id, l, r, splitDim, splitValue);
		}

		private Node split(Leaf cur, int d) {
			SplitResult split = n >= 100 ? byGini(cur) : byEntropy(cur) ;
			if (split == null) {
				return cur.setMostProbable();
			} else {
				cur.samples.sort(Comparator.comparing(a -> a.x.get(split.dim)));
				int splitByIndex = 0;
				for (int i = 0; i < cur.samples.size(); i++) {
					if (cur.samples.get(i).x.get(split.dim) > split.value) {
						splitByIndex = i;
						break;
					}
				}
				Node l = train(
						new Leaf(id++, cur.samples.subList(0, splitByIndex)),
						d + 1);
				Node r = train(
						new Leaf(id++, cur.samples.subList(splitByIndex, cur.samples.size())),
						d + 1);
				return fromLeaf(cur, l, r, split.dim, split.value);
			}
		}

		private SplitResult byGini(Leaf l) {
			double bestScore = 0;
			int splittingDim = -1;
			double splittingVal = -1;
			for (int splitDim = 0; splitDim < m; splitDim++) {
				if (notApplicable(l, splitDim)) continue;
				double left = 0, right;
				double[] c1 = new double[k], c2 = new double[k];
				right = l.samples.stream().mapToDouble(s -> 2 * (c2[s.y]++) + 1).sum();
				for (int split = 0; split < l.samples.size(); split++) {
					if (split != 0) {
						if (!l.samples.get(split).x.get(splitDim).equals(l.samples.get(split - 1).x.get(splitDim))) {
							double score = right / (l.samples.size() - split) + left / split;
							if (score > bestScore) {
								bestScore = score;
								splittingDim = splitDim;
								splittingVal = (l.samples.get(split - 1).x.get(splitDim) + (double) l.samples.get(split).x.get(splitDim)) / 2;
							}
						}
					}
					left += 2 * (c1[l.samples.get(split).y]++);
					left++;
					right += 2 * -(c2[l.samples.get(split).y]--);
					right++;
				}
			}
			return splittingDim == -1 ? null : new SplitResult(splittingDim, splittingVal);
		}

		private SplitResult byEntropy(Leaf l) {
			double bestScore = Integer.MAX_VALUE, splittingVal = -1;
			int splittingDim = -1;
			for (int splitDim = 0; splitDim < m; splitDim++) {
				if (notApplicable(l, splitDim)) {
					continue;
				}
				double[] c2 = new double[k], c1 = new double[k];
				l.samples.forEach(s -> ++c1[s.y]);
				for (int split = 0; split < l.samples.size(); split++) {
					int curSolution = l.samples.get(split).y;
					if (split != 0) {
						if (!l.samples.get(split - 1).x.get(splitDim).equals(l.samples.get(split).x.get(splitDim))) {
							double score = entropy(c2, split) * split
									+ entropy(c1, l.samples.size() - split) * (l.samples.size() - split);
							if (score < bestScore) {
								splittingDim = splitDim;
								bestScore = score;
								splittingVal = (
										l.samples.get(split - 1).x.get(splitDim)
												+ (double) l.samples.get(split).x.get(splitDim)) / 2;
							}
						}
					}
					--c1[curSolution];
					++c2[curSolution];
				}
			}
			return splittingDim == -1 ? null : new SplitResult(splittingDim, splittingVal);
		}

		private boolean notApplicable(Leaf l, int splitDim) {
			l.samples.sort(Comparator.comparingInt(a -> a.x.get(splitDim)));
			return l.samples.get(0).x.get(splitDim).equals(l.samples.get(l.samples.size() - 1).x.get(splitDim));
		}

		private double entropy(double[] a, int size) {
			double sum = 0;
			for (int i = 0; i < a.length; i++) {
				sum -= a[i] == 0 ? 0 : a[i] / size * Math.log(a[i] / size);
			}
			return sum;
		}

		private void extractInner(Map<Integer, String> reprs, Node node) {
			reprs.put(node.getId(), node.toString());
			if (node instanceof TreeNode) {
				extractInner(reprs, ((TreeNode) node).l);
				extractInner(reprs, ((TreeNode) node).r);
			}
		}

		@Override
		public String toString() {
			var sortedById = new TreeMap<Integer, String>();
			extractInner(sortedById, root);
			return String.join("\n", sortedById.values());
		}
	}

	private static class Sample {
		final List<Integer> x;
		final int y;

		public Sample(List<Integer> x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private static class SplitResult {
		int dim;
		double value;
		public SplitResult(int dim, double value) {
			this.dim = dim;
			this.value = value;
		}
	}

	private static class Node {
		public int id;
		public Node(int id) { this.id = id;}
		public int getId() { return id + 1; }
	}

	private static class TreeNode extends Node {
		Node l, r;
		int splitDim;
		double splitVal;

		public TreeNode(int id, Node l, Node r, int splitDim, double splitVal) {
			super(id);
			this.l = l;
			this.r = r;
			this.splitDim = splitDim;
			this.splitVal = splitVal;
		}

		@Override
		public String toString() {
			return String.format(
					Locale.US,
					"Q %d %.9f %d %d",
					1 + splitDim, splitVal, l.getId(), r.getId());
		}
	}

	private static class Leaf extends Node {
		List<Sample> samples;
		int solution = -1;

		public Leaf(int id, List<Sample> samples) {
			this(id, samples, false);
		}

		public Leaf(int id, List<Sample> samples, boolean withMostProbable) {
			super(id);
			this.samples = samples;
			if (withMostProbable) {
				solution = mostProbable(samples);
			}
		}

		public Node setMostProbable() {
			solution = mostProbable(samples);
			return this;
		}

		@Override
		public String toString() {
			return "C " + (solution + 1);
		}
	}
}