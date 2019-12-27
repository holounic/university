import java.util.*;
import java.io.*;

public class TwentyTwo {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File("part2num.in"));
        String s = in.nextLine();
        in.close();
        String[] splited = s.split("\\+");
        int[] partition = new int[splited.length];
        int t = 0;

        for (int i = 0; i < splited.length; i++) {
            partition[i] = Integer.parseInt(splited[i]);
            t += partition[i];
        }

        long[][] dp = new long[t + 2][t + 2];
        for (int i = 1; i < dp.length; i++) {
            for (int j = dp[0].length - 1; j > 0 ; j--) {
                if (i < j) {
                    dp[i][j] = 0;
                    continue;
                }
                if (i == j) {
                    dp[i][j] = 1;
                    continue;
                }
                dp[i][j] = dp[i][j + 1] + dp[i - j][j];
            }
        }

        long num = 0;
        int last = 0;
        int sum = 0;
        for (int el : partition) {
            for (int j = last; j < el; j++) {
                num += dp[t - sum - j][j];
            }
            sum += el;
            last = el;
        }
        PrintWriter out = new PrintWriter(new File("part2num.out"));
        out.write(Long.toString(num));
        out.close();
    }
}