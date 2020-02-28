import java.util.*;
 
public class M {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
            for (int i = 0; i < t; ++i) {
 
                int n = scanner.nextInt();
                int[] arr = new int[n];
 
                for (int k = 0; k < n; ++k) {
                    arr[k] = scanner.nextInt();
                }
 
                Map<Integer, Integer> days = new LinkedHashMap<>();
                int res = 0;
 
                for (int j = n - 1; j >= 0; --j) {
                    for (int q = 0; q < j; ++q) {
                        if (days.containsKey(2 * arr[j] - arr[q])) {
                            res += days.get(2 * arr[j] - arr[q]);
                        }
                    }
 
                    if (!days.containsKey(arr[j])) {
                        days.put(arr[j], 1);
                    } else {
                        int temp = days.get(arr[j]);
                        days.replace(arr[j], temp + 1);
                    }
                }
                System.out.println(res);
            }
    }
}