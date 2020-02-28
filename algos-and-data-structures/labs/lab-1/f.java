import java.util.Scanner;
 
public class Binary {
 
    public static int search(int [] arr, int l, int r, int key) {
        if (r - l <=  1) {
            if (key - arr[l] <= arr[r] - key) {
                return l;
            }
            return r;
        }
        int m = (l + r) / 2;
        if (arr[m] > key) {
            r = m;
        } else {
            l = m;
        }
        return search(arr, l, r, key);
    }
 
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        int n, k;
        n = scanner.nextInt();
        k = scanner.nextInt();
        int [] arr = new int [n];
        int [] req = new int [k];
 
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        for (int i = 0; i < k; i++) {
            req[i] = scanner.nextInt();
            
        }
        for (int i = 0; i < k; i++) {
            int res = search(arr, 0, n - 1, req[i]);
            System.out.println(arr[res]);
        }
    }
}