package md2html;

import java.io.*;

public class Reader {
    private BufferedReader reader;

    public Reader(String file) throws FileNotFoundException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    private boolean isBlank(String line) {
        if (line.isEmpty()) {
            return true;
        }
        for (int i = 0; i < line.length(); ++i) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public String read() {
        StringBuilder builder = new StringBuilder();

        try {
            String next = reader.readLine();

            while (next != null && isBlank(next)) {
                next = reader.readLine();
            }
            while (next != null && !isBlank(next) && !next.isEmpty()) {
                if (!isBlank(next)) {
                    builder.append(next + "\n");
                }
                next = reader.readLine();
            }

            if (builder.length() == 0) {
                return null;
            }

            builder.deleteCharAt(builder.length() - 1);
        } catch (IOException e) {
            System.out.println(e);
        }
        return builder.toString();
    }

    public boolean close() {
        try {
            reader.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
