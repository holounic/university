import java.io.*;

public class Scanner {
    private Reader reader;
    private int bufferedChar;
    private boolean hasBuffered;

    public Scanner(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
        resetBuffer();
    }

    public Scanner(File file) throws FileNotFoundException {
        this((new FileInputStream(file)));
    }

    private void resetBuffer() {
        bufferedChar = -2;
        hasBuffered = false;
    }

    private void skipWhiteSpace() {
        try {
            int nextChar = bufferedChar;
            if (!hasBuffered) {
                nextChar = reader.read();
            }
            while (Character.isWhitespace((char)nextChar)) {
                hasBuffered = false;
                nextChar = reader.read();
            }
            bufferedChar = nextChar;
            hasBuffered = true;
        } catch (IOException e) {
            System.out.println("IOException caught: " + e);
        }
    }

    private void skipBlankTillLineBreak() {
        try {
            int nextChar = bufferedChar;
            if (!hasBuffered) {
                nextChar = reader.read();
            }
            while (Character.isWhitespace((char)nextChar) && nextChar != (int)'\n' && nextChar != -1) {
                hasBuffered = false;
                nextChar = reader.read();
            }
            bufferedChar = nextChar;
            hasBuffered = true;
        } catch (IOException e) {
            System.out.println("IOException caught: " + e);
        }
    }

    public boolean hasNext() {
        try {
            if (!hasBuffered) {
                bufferedChar = reader.read();
                hasBuffered = true;
            }
            if (bufferedChar >= 0) {
                return true;
            }
        } catch (IOException e) {
            System.out.println("IOException caught: " + e);
        }
        return false;
    }

    public String next() {
        StringBuilder builder = new StringBuilder();
        skipWhiteSpace();
        try {
            if (hasBuffered && bufferedChar != -1) {
                builder.append((char)bufferedChar);
                hasBuffered = false;
            }
            int currentChar = reader.read();
            while (currentChar != -1 && !Character.isWhitespace((char)currentChar)) {
                builder.append((char)currentChar);
                currentChar = reader.read();
            }
        } catch (IOException e) {
            System.out.println("IOException caught: " + e);
        }
        resetBuffer();
        return builder.toString();
    }
    
    public String nextLine() {
        skipBlankTillLineBreak();
        StringBuilder builder = new StringBuilder();
        try {
            int currentChar = -1;
            if (hasBuffered && bufferedChar != -1) {
                currentChar = bufferedChar;
            } else {
                currentChar = reader.read();
            }
            while (currentChar != -1 && currentChar != (int)'\n') {
                builder.append((char)currentChar);
                currentChar = reader.read();
            }
        } catch (IOException e) {
            System.out.println("IOException caught: " + e);
        }
        resetBuffer();
        return builder.toString();
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