public class Sum{

    private static int sum;

    private static boolean isNumber(char a){
        if (a - '0' > -1 && a - '0' < 10 ) {
            return true;
        }
        return false;
    }

    private static int isMinus(char a) {
        if (a == '-') {
            return -1;
        }
        return 1;
    }

    private static void parse(String arg) {
        if (arg.length() == 0) return;

        String toParse = ' ' + arg;
        int digit = 1;
        int idx = toParse.length() - 1;
        int number = 0;

        while (idx >= 0) {
            if (isNumber(toParse.charAt(idx))) {
                number += digit * (toParse.charAt(idx) - '0');
                digit *= 10;
            } else {
                number *= isMinus(toParse.charAt(idx));
                sum += number;
                digit = 1;
                number = 0;
            }

            idx--;
        }

    }
    public static void main(String [] args) {
        sum = 0;

        for (int i = 0; i < args.length; i++){
            parse(args[i]);
        }
        System.out.println(sum);
    }
}