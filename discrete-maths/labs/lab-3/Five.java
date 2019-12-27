import java.util.*;
import java.io.*;

public class Five {

        public static void main(String[] args) throws FileNotFoundException {
            Scanner in = new Scanner(new File("telemetry.in"));
            int n = in.nextInt();
            int k = in.nextInt();
            in.close();

            List<String> code = new ArrayList<>();
            code.add("");

            for (int i = 0; i < n; i++) {
                int curLength = code.size();

                for (int j = 1; j < k; j++) {
                    for (int s = 0; s < curLength; s++) {
                        String postfix = Integer.toString(j);
                        if ((j & 1) == 1) {
                            code.add(code.get(curLength - s - 1) + postfix);
                        } else {
                            code.add(code.get(s) + postfix);
                        }
                    }
                }

                for (int j = 0; j < curLength; j++) {
                    String temp = code.get(j) + "0";
                    code.set(j, temp);
                }

            }
            PrintWriter out = new PrintWriter(new File("telemetry.out"));
            for (String codes : code) {
                out.write(codes);
                System.out.println(codes);
                out.write('\n');
            }
            out.close();
        }
        
    }