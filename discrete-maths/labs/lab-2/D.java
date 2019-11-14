import java.util.*;
import java.io.*;

public class D {
    public static void main(String [] args) throws FileNotFoundException {
        Scanner scanner = new Scanner( new File("lzw.in"));
        String s = scanner.next() + "!";
        scanner.close();

        Map<String,Integer> dict = new HashMap<>();
        for (int i = 0; i < 26; ++i) {
            char filler[] = {(char)('a' + i)};
            dict.put(new String(filler), i);
        }

        try {
            OutputStream writer = new FileOutputStream("lzw.out");
            int length = s.length();
            int max = 25;
            for (int i = 0; i < length; ++i) {
                if (s.charAt(i) == '!') break;
                int begin = i;
                while (dict.containsKey(s.substring(begin, i + 1)) && i < length) {
                    ++i;
                    if (i == length) {
                        i--;
                        break;
                    }
                }
                String key = s.substring(begin, i);
                String res = Integer.toString(dict.get(key));
                if (i < length - 1) {
                    dict.put(s.substring(begin, i + 1), ++max);
                }
                for (int j = 0; j < res.length(); ++j) {
                    writer.write(res.charAt(j));
                }
                writer.write(' ');
                --i;
            }
            writer.close();
        } catch (IOException e) {
            //trolling occured
        }
    }
}