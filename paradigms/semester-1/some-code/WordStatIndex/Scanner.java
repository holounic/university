import java.util.*;
import java.io.*;

public class Scanner {
    public BufferedReader reader;
    private char[] buffer;
    private int bufferIdx;

    public Scanner(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
        resetBuffer();
    }

    public Scanner(File file) throws FileNotFoundException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        resetBuffer();
    }

    private void resetBuffer() {
        buffer = new char[7];
        bufferIdx = 0;
    }

    private void resizeBuffer() {
        char [] resized = new char[buffer.length * 2];
        System.arraycopy(buffer, 0, resized, 0, buffer.length);
        buffer = resized;
    }

    private String bufferToString() {
        return new StringBuilder().append(buffer, 0, bufferIdx).toString();
    }

    public boolean hasNext() {
        try {
            int current = reader.read();
            if (current == -1) {
                return false;
            } else {
                if (buffer.length <= bufferIdx) {
                    resizeBuffer();
                }
                buffer[bufferIdx++] = (char)current;
                return true;
            }
        } catch (IOException e) {
            System.out.println("IOException caught: " + e);
        }
        return false;
    }
    
    public String nextLine() {
        try {
            int current = reader.read();
            while ((char)current != '\n' && current != -1) {
                if (bufferIdx >= buffer.length) {
                    resizeBuffer();
                }
                buffer[bufferIdx++] = (char)current;
                current = reader.read();
            }
        }  catch (IOException e) { 
            System.out.println("IOException caught: " + e);   
        }
        String line = bufferToString();
        resetBuffer();
        return line;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Unable to close this Instance of Scanner");
        }
        
        resetBuffer();
    }

}
