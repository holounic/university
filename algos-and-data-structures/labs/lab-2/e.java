import java.util.*;

public class e {

    static int operation(int first, int second, String sign) {
        if (sign.equals("+")) {
            return first + second;
        }
        if (sign.equals("-")) {
            return first - second;
        }
        return first * second;
    }
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        Stack<Integer> numbers = new Stack<>();
        while (scanner.hasNext()) {
            String next = scanner.next();
            try {
                numbers.add(Integer.parseInt(next));
            } catch (NumberFormatException e) {
                int secondNum = numbers.pop();
                int firstNum = numbers.pop();
                numbers.push(operation(firstNum, secondNum, next));
            }
        }
        System.out.println(numbers.pop());
    }
}