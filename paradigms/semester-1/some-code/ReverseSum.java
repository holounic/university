import java.util.Scanner;
import java.util.Arrays;


public class ReverseSum {

    static void fillWithZero(int [] arr, int idx) {
        for (int i = idx; i < arr.length; i++) {
            arr[i] = 0;
        }
    }

    public static void main(String [] args) {
        int [][] input = new int[2][];
        int inputIdx = 0;
        int [] colums = new int[2];
        fillWithZero(colums, 0);
        int columIdx = 0;
        int [] raws = new int[2];
        fillWithZero(raws, 0);
        int rawsIdx = 0;
        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String [] parsed = line.split(" ");

            if (parsed.length >= colums.length) {
                colums = Arrays.copyOf(colums, colums.length * 2);
                fillWithZero(colums, colums.length / 2);
            }

            if (rawsIdx > raws.length - 1) {
                raws = Arrays.copyOf(raws, raws.length * 2);

                fillWithZero(raws, raws.length / 2);
                int [][] temp = new int[input.length][];
                for (int k = 0; k < input.length; k++) {
                    temp[k] = new int [input[k].length];
                    System.arraycopy(input[k], 0, temp[k], 0, input[k].length);
                }
                input = new int[input.length * 2][];
                for (int k = 0; k < temp.length; k++){
                    input[k] = temp[k];
                }
            }

            if (line.isEmpty()) {
                input[inputIdx++] = new int[0];
                raws[rawsIdx++] = 0;
                continue;
            }
            int [] currentLine = new int[parsed.length];
            
            int lineSum = 0;
            for (int i = 0; i < parsed.length; i++) {
                int currentInt = Integer.parseInt(parsed[i]);
                lineSum += currentInt;
                if (colums.length <= i) {
                    colums = Arrays.copyOf(colums, colums.length * 2);
                    fillWithZero(colums, colums.length / 2);
                }
                colums[i] += currentInt;
                currentLine[i] = currentInt;
            }
            raws[rawsIdx++] = lineSum;
            input[inputIdx++] = currentLine;
        }
        scanner.close();

        for (int i = 0; i < inputIdx; i++) {
            if (input[i].length == 0) {
                System.out.println();
                continue;
            } else {
                for (int j = 0; j < input[i].length; j++) {
                    System.out.print(raws[i] + colums[j] - input[i][j] + " ");
                }
                System.out.println();
            }
            
        }
    }
}
