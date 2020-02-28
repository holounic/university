import java.util.*;
 
public class J {
 
    static int toInt(int c) {
        return (char)c - '0';
    }
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] v = new int[n][n];
        int[][] arr = new int[n][n];
        String next;
 
        for (int i = 0; i < n - 1; i++) {
            next = scanner.next();
            arr[i] = next.chars().map(character -> toInt(character)).toArray();
 
            System.arraycopy(arr[i], i + 1, arr[i], 0, n - i - 1);
        }
 
         
        int res = 0;
        for (int j = 1; j < n; ++j) {
 
            for (int i = 0; i < n - j; ++i) {
 
                for (int q = 1; q < j; ++q) {
                    res += arr[i][q - 1] * v[i + q][j - q - 1];
                }
                 
                if (arr[i][j - 1] != (res %= 10)) {
                    v[i][j - 1] = 1;
                }
                res = 0;
            }
        }
 
        for (int i = 0; i < n; ++i) {
             
 
            for (int j = 0; j - 1 < i; ++j) {
                System.out.print(0);
            }
 
            for (int d = 0; d  < n - i - 1 ; ++d) {
                System.out.print(v[i][d]);
            }
            System.out.println();
        }
    }
}