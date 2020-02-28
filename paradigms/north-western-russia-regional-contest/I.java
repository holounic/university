import java.util.*;
 
public class I {
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
 
        long minX = Long.MAX_VALUE;
        long maxX = Long.MIN_VALUE;
        long minY = Long.MAX_VALUE;
        long maxY = Long.MIN_VALUE;
 
        for (int i = 0; i < n; ++i) {
            long x, y, h;
            x = scanner.nextLong();
            y = scanner.nextLong();
            h = scanner.nextLong();
            minX = Math.min(minX, x - h);
            maxX = Math.max(maxX, x + h);
            minY = Math.min(minY, y - h);
            maxY = Math.max(maxY, y + h);
        }
        
        System.out.println((maxX + minX) / 2 + " " + (maxY + minY) / 2 + " " + (Math.max(maxY - minY, maxX - minX) + 1) / 2);
 
    }
}