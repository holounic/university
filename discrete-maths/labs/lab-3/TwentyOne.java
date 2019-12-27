import java.util.*;
import java.io.*;

public class TwentyOne {

    static int nextPartition(int[] sum, int l) {
        sum[l - 1]--;
        sum[l - 2]++;

        if (sum[l - 2] > sum[l - 1]) {
            sum[l - 2] += sum[l - 1];
            sum[l - 1] = 0;
            return l - 1;
        }
        while (sum[l - 2] * 2 <= sum[l - 1]) {
            sum[l++] = sum[l - 2] - sum[l - 3];
            sum[l - 2] = sum[l - 3];
        }
        return l;
    }
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("num2part.in"));
        int n = in.nextInt();
        int k = in.nextInt();
        in.close();

        int[] partition = new int[n];
        Arrays.fill(partition, 1);

        int cnt = 0;
        int l = n;

        while (cnt != k) {
            l = nextPartition(partition, l);
            cnt++;
        }

        PrintWriter out = new PrintWriter(new File("num2part.out"));
        for (int i = 0; i < l; i++) {
            out.write(partition[i] + (i < l - 1 ? "+" : ""));
        }
        out.close();
    }

}