import java.util.*;
 
public class A {
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        int a, b, n;
        a = scanner.nextInt();
        b = scanner.nextInt();
        n = scanner.nextInt();
        scanner.close();
 
        System.out.println((int)Math.ceil((float)(n - b) / (b - a) ) * 2 + 1);
    }
}