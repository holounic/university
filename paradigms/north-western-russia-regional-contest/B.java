import java.util.Scanner;
 
public class B {
    public static void main(String [] args) {
        int n = new Scanner(System.in).nextInt();
 
        for (int i = 0; i < n; ++i) {
            System.out.println((i - 25000) * 710);
        }
    }
}