import java.util.*;
import java.io.*;

public class TwentyNine {

    static void nextPartition(int[] sum, int l) {
        sum[l - 1]--;
        sum[l - 2]++;

        if (sum[l - 2] > sum[l - 1]) {
            sum[l - 2] += sum[l - 1];
            sum[l - 1] = 0;
            return;
        }
        while (sum[l - 2] * 2 <= sum[l - 1]) {
            sum[l++] = sum[l - 2] - sum[l - 3];
            sum[l - 2] = sum[l - 3];
        }
    }
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File("nextpartition.in"));
        String s = in.nextLine();
        in.close();
        String[] splited = s.split("[+=]");

        int t = Integer.parseInt(splited[0]);
        int[] sum = new int[t];
        for (int i = 1; i < splited.length; i++) {
            sum[i - 1] = Integer.parseInt(splited[i]);
        }
        PrintWriter out = new PrintWriter(new File("nextpartition.out"));
        if (t == sum[0]) {
            out.write("No solution");
            out.close();
            System.exit(0);
        }
        nextPartition(sum, splited.length - 1);

        
        out.write(t + "=");
        int i = 0;
        while (sum[i] != 0) {
            out.write(sum[i++] + (sum[i] == 0 ? "" : "+"));
        }
        out.close();
    }
}