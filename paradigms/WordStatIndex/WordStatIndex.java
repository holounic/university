import java.util.*;
import java.io.*;

public class WordStatIndex {
    public static Scanner openInput(File file, String encoding) {
        try {
            return new Scanner(file, encoding);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return new Scanner(System.in);
        }
    } 

    private static boolean isWordPart(char c) {
        return (Character.isAlphabetic(c) || Character.DASH_PUNCTUATION == Character.getType(c) || c == '\'');
    }

    public static void main(String [] args) {
        String inputFileName = args[0];
        String encoding = "UTF-8";
        Scanner scanner = openInput(new File(inputFileName), encoding);
        Map<String,IntList> statistics= new LinkedHashMap<>();
        int wordCount = 1;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase();
            int start = -1;
            for (int i = 0; i < line.length(); i++) {
                if (isWordPart(line.charAt(i)) && start == -1) {
                    start = i;
                }

                if (start != -1 && (i + 1 == line.length() || !isWordPart(line.charAt(i)))) {
                    String word;
                    if (!isWordPart(line.charAt(i))) {
                        word = line.substring(start, i);
                    } else {
                        word = line.substring(start, i + 1);
                    }

                    if (statistics.containsKey(word)) {
                        IntList temp = statistics.get(word);
                        temp.addLast(wordCount++);
                        temp.setFirst(temp.getFirst() + 1);
                        statistics.replace(word, temp);
                    } else {
                        IntList temp = new IntList();
                        temp.addFirst(1);
                        temp.addLast(wordCount++);
                        statistics.put(word, temp);
                    }
                    start = -1;
                }
            }
        }
        scanner.close();

        // for (String word : statistics.keySet()) {
        //     System.out.println(word + " " + statistics.get(word).toString());
        // }
        String outputFileName = args[1];
        try (FileWriter writer = new FileWriter(new File(outputFileName), false)) {
            for (String word : statistics.keySet()) {
                writer.write(word + " " + statistics.get(word).toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("IOException");
        } 
    }
}