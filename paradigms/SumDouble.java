
public class SumDouble {
    private static double sum;

    static void parse(String arg) {
        if (arg.length() == 0) {
            return;
        }
        int idx = 0;
        String toParse = arg;
        int beginIdx = -1;
        int endIdx = -1;

        while (idx < toParse.length()) {
            char current = toParse.charAt(idx);
            if (!Character.isWhitespace(current)) {
                if (beginIdx == -1) {
                    beginIdx = idx;
                }       
            } else {
                if (beginIdx != -1) {
                    endIdx = idx;
                }
            }
            if ((idx == toParse.length() - 1 || endIdx != -1) && beginIdx != -1) {
                if (endIdx == -1) {
                    endIdx = idx + 1;
                } 
                String temp = toParse.substring(beginIdx, endIdx);
                sum += Double.parseDouble(temp);
                beginIdx = -1;
                endIdx = -1;
            }
            idx++;
            
        }
    }

    public static void main(String [] args) {
        sum = 0;
        for (int i = 0; i < args.length; i++) {
            parse(args[i]);
        }
        System.out.println(sum);
    }
}