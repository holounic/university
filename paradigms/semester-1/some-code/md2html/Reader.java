package md2html;

import java.io.*;

public class Reader {
    private BufferedReader reader;

    public Reader(String file) throws FileNotFoundException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    public String read() {
        StringBuilder builder = new StringBuilder();

        try {
            String next = reader.readLine();

            while (next != null && next.isEmpty()) {
                next = reader.readLine();
            }
            while (next != null && !next.isEmpty() && !next.isEmpty()) {
                if (!next.isEmpty()) {
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
