import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class ReverseSum {
    public static void main(String [] args) {
        List <List<Integer>> input = new ArrayList<>();
        List <Integer> colums = new ArrayList<>();
        List <Integer> raws = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String line = "";

        while(scanner.hasNextLine()) {
            line = scanner.nextLine();
            String [] parsed = line.split(" ");
            List <Integer> currentLine = new ArrayList<>(0);

            if (line.isEmpty()) {
                input.add(currentLine);
                raws.add(0);
                continue;
            }
            
            int lineSum = 0;
            for (int i = 0; i < parsed.length; i++) {
                int currentInt = Integer.parseInt(parsed[i]);
                lineSum += currentInt;
                if (colums.size() <= i) {
                    colums.add(0);
                }
                int temp = colums.get(i) + currentInt;
                colums.set(i, temp);
                currentLine.add(currentInt);
            }
            raws.add(lineSum);
            input.add(currentLine);
        }
        scanner.close();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).size() == 0) {
                System.out.println();
                continue;
            }
            for (int j = 0; j < input.get(i).size(); j++) {
                System.out.print(raws.get(i) + colums.get(j) - input.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}