import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class G {
    private static int[] fun;
    private static int n;
	public static void main(String[] args) throws IOException {
        int o = 0;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int m = Integer.parseInt(reader.readLine());
        n = (1 << m);
		fun = new int[n];
		
		for (int i = 0; i < n; i++) {
			fun[i] = Integer.parseInt(reader.readLine());
            o += fun[i];
		}
		if (o > 512) {
			mkCnf(m);
            return;
		}
		mkDnf(m);
    }


	static void mkDnf(int m) {
		List<List<Double>> rw = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			if (fun[i] == 1) rw.add(mkAnd(m, i));
		}
		if (rw.size() != 0) {
			System.out.println(2 + "\n" + rw.size() + " " + 1);
			for (var doubles : rw) {
				for (var d : doubles) System.out.print(d + " ");
				System.out.println();
			}
			for (int i = 0; i < rw.size(); i++) {
				System.out.print(1.0 + " ");
			}
		} else {
			System.out.println(1 + "\n" + 1);
			for (int i = 0; i < m; i++) {
				System.out.print(1.0 + " ");
			}
		}
        System.out.println(-0.5);
	}
	
	static void mkCnf(int m) {
		List<List<Double>> rw = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			if (fun[i] == 0) rw.add(mkOr(m, i));
		}
		System.out.println(2 + "\n" + rw.size() + " " + 1);
		for (var l : rw) {
			for (var d : l) {
				System.out.print(d + " ");
			}
			System.out.println();
		}
		for (int i = 0; i < rw.size(); i++) {
			System.out.print(1.0 + " ");
		}
		System.out.println(-rw.size() + 0.5);
	}

    static List<Double> mkOr(int m, int i) {
		List<Double> r = new ArrayList<>();
		for (int j = 0; j < m; j++) {
            r.add(((i >> j) & 1) == 1 ? -1.0 : 1.0);
		}
		r.add(Integer.bitCount(i) - 0.5);
		return r;
	}
	
	static List<Double> mkAnd(int m, int i) {
		List<Double> r = new ArrayList<>();
		for (int j = 0; j < m; j++) {
            r.add(((i >> j) & 1) == 1 ? 1.0 : -1.0);
		}
		r.add(0.5 - Integer.bitCount(i));
		return r;
	}
}