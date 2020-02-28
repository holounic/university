import java.util.Scanner;
 
public class Inversion {
 
    static long count = 0;
 
    static int[] mergeSort(int[] a) {
        if (a.length == 1) return a;
        
        int m = a.length/2;
        int [] left = new int[m];
        int [] right = new int[a.length - m];
        System.arraycopy(a, 0, left, 0, m);
        System.arraycopy(a, m, right, 0, a.length - m);
 
        left = mergeSort(left);
        right = mergeSort(right);
        return merge(left, right);
    }
 
    static int[] merge(int [] a, int [] b) {
        int[] res = new int[a.length + b.length];
        int i = 0;
        int j = 0;
        for (int k = 0; k<res.length; k++) {
            if (j==b.length || i<a.length && a[i]<=b[j]) {
                res[k] = a[i];
                i++;
            } else {
                res[k] = b[j];
                count +=a.length - i;
                j++;
            }
        }
        return res;
    }
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int [] a = new int [n];
 
        for (int i = 0; i<n; i++) {
            a[i] = scanner.nextInt();
        }
 
        a = mergeSort(a);
        System.out.println(count);
    }
}