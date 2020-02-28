package md2html;

import java.io.*;

public class Writer {
    private BufferedWriter writer;

    public Writer(String file) throws IOException {
        writer = new BufferedWriter(new FileWriter(new File(file)));
    }

    public boolean write(String text) {
        try {
            writer.write(text + '\n');
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean close(){
        try {
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
