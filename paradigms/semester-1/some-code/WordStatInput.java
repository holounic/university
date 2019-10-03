import java.util.*;
import java.io.*;

public class WordStatInput {
    private static Scanner openInput(File file, String encoding) {
        try {
            return new Scanner(file, encoding);
        }
        catch (FileNotFoundException e) {
            return new Scanner("");
        }
    }

    private static boolean isPartOfWord(char c) {
        return (Character.isAlphabetic(c) || Character.DASH_PUNCTUATION == Character.getType(c) || c == '\'');
    }

    public static void main(String [] args) {
        String inputFileName = args[0];
        String encoding = "UTF-8";
        File inputFile = new File(inputFileName);
        Scanner scanner = openInput(inputFile, encoding);

        List <String> words = new ArrayList<>();
        Map <String, Integer> statistic = new HashMap<>();
        String line;

        while (scanner.hasNext()) {
            line = scanner.nextLine().toLowerCase();
            int start = -1;
            for (int i = 0; i < line.length(); i++) {
                if (isPartOfWord(line.charAt(i)) && start == -1) {
                    start = i;
                }

                if ((!isPartOfWord(line.charAt(i)) || i + 1 == line.length()) && start != -1 ) {
                    String word;
                    if (!isPartOfWord(line.charAt(i))) {
                        word = line.substring(start, i);
                    } else {
                        word = line.substring(start, i + 1);
                    }

                    if (!word.isEmpty()) {
                        if (!statistic.containsKey(word)) {
                            words.add(word);
                            statistic.put(word, 1);
                        } else {
                            int wordStatistic = statistic.get(word) + 1;
                            statistic.replace(word, wordStatistic);
                        }
                    }
                    start = -1;
                }
            }
        }
        scanner.close();

        String outputFileName = args[1];
        File outputFile = new File(outputFileName);
        try (FileWriter writer = new FileWriter(outputFile, false)) {
            for (String word : words) {
                writer.write(word + " " + statistic.get(word) + "\n");
            }
            writer.close();
        } catch (IOException e) {
        }
    }
}