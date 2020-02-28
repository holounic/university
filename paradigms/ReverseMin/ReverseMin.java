import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class ReverseMin {
    public static void main(String [] args) {
        List <Integer> colums = new ArrayList<>();
        List <Integer> raws = new ArrayList<>();
        List <Integer> lengths = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String line = "";

        while(scanner.hasNext()) {
            line = scanner.nextLine();
            String [] parsed = line.split(" ");

            int minInRaw = 1000000000;

            if (line.isEmpty()) {
                raws.add(0);
                lengths.add(0);
                continue;
            }

            int lineSum = 0;
            for (int i = 0; i < parsed.length; i++) {
                int currentInt = Integer.parseInt(parsed[i]);
                minInRaw = Math.min(minInRaw, currentInt);
                if (colums.size() <= i) {
                    colums.add(1000000000);
                }
                colums.set(i, Math.min(colums.get(i), currentInt));
            }
            raws.add(minInRaw);
            lengths.add(parsed.length);
        }
        scanner.close();

        for (int i = 0; i < raws.size(); i++) {
            if (lengths.get(i) == 0) {
                System.out.println();
                continue;
            }
            for (int j = 0; j < lengths.get(i); j++) {
                System.out.print(Math.min(raws.get(i), colums.get(j)) + " ");
            }
            System.out.println();
        }
    }
} 