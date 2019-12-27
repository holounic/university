import java.util.*;
import java.io.*;

public class Nineteen {

    static int littleConst;
    static List<Boolean> prevIndicators = new ArrayList<Boolean>();
    static StringBuilder ans = new StringBuilder();
    static int d = 0;
    static long k;

    public static void computeDp(long[][] dp) {
        dp[0][0] = 1;
        for (int i = 1; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                if (j > 0) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
                if (j < dp[0].length - 1) {
                    dp[i][j] += dp[i - 1][j + 1];
                }
            }
        }
    }

    public static long power2(long power) {
        return (1 << power);
    } 

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File("num2brackets2.in"));
        int n = in.nextInt();
        k = in.nextLong();
        in.close();

        long[][] dp = new long[2 * n + 1][2 * n + 1];
        computeDp(dp);

        littleConst = 2 * n - 1;
        k += 1;

        for (int i = 0; i < 2 * n && k != 0; ++i) {
            long powerOpen = (littleConst - i - d - 1) / 2;
            long poweredOpen = power2(powerOpen);
            long powerClose = (littleConst - i + 1 - d) / 2;
            long poweredClose = power2(powerClose);

            if (d < n && k <= dp[littleConst - i][d + 1] * poweredOpen) {
                ans.append("(");
                d++;
                prevIndicators.add(false);
                continue;
            }

            if (d < n) {
                k -=  poweredOpen * dp[littleConst - i][d + 1];
            }

            if (d > 0 && prevIndicators.size() > 0 && !prevIndicators.get(prevIndicators.size() - 1) && (k <= dp[littleConst - i][d - 1] * poweredClose)) {
                ans.append(")");
                d--;
                prevIndicators.remove(prevIndicators.size() - 1);
                continue;
            }

            if (d > 0 && prevIndicators.size() > 0 && !prevIndicators.get(prevIndicators.size() - 1)) {
                k -=  poweredClose * dp[littleConst - i][d - 1];
            }

            if (d < n && dp[littleConst - i][d + 1] * poweredOpen >= k) {
                ans.append("[");
                d++;
                prevIndicators.add(true);
                continue;
            }

            if (d < n) {
                k -=  poweredOpen * dp[littleConst - i][d + 1];
            }

            d--;
            ans.append("]");
            prevIndicators.remove(prevIndicators.size() - 1);
        }
        PrintWriter out = new PrintWriter(new File("num2brackets2.out"));
        out.write(ans.toString());
        out.close();
    }
}