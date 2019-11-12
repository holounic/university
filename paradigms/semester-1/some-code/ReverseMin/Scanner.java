import java.io.*;

public class Scanner {
    private Reader reader;
    private int bufferedChar;
    private boolean hasBuffered;
    private boolean isLineBreak;
    private enum CheckerType {
        LINE,
        SEQUENCE
    }

    public Scanner(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
        resetBuffer();
        isLineBreak = false;
    }

    public Scanner(File file) throws FileNotFoundException {
        this((new FileInputStream(file)));
    }

    private void resetBuffer() {
        bufferedChar = -1;
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
                isLineBreak = (boolean) (nextChar == (int)'\n');
            }
            bufferedChar = nextChar;
            hasBuffered = true;
        } catch (IOException e) {
            System.out.println("IOException caught: " + e);
        }
    }

    public boolean isLineEnd() {
        return isLineBreak;
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

    private boolean lineChecker(int c) {
        return c != -1 && c != (int)'\n';
    }

    private boolean sequenceChecker(int c) {
        return c != -1 && !Character.isWhitespace(c);
    }

    private boolean checker(int c, CheckerType type) {
        switch(type) {
            case LINE:
                return lineChecker(c);
            case SEQUENCE:
                return sequenceChecker(c);
            default:
                return false;
        }
    }

    private String readNext(CheckerType type) {
        StringBuilder builder = new StringBuilder();
        try {
            int currentChar;
            if (hasBuffered && bufferedChar != -1) {
                currentChar = bufferedChar;
            } else {
                currentChar = reader.read();
            }
            while (checker(currentChar, type)) {
                builder.append((char)currentChar);
                currentChar = reader.read();
            }
            isLineBreak = false;
            if (currentChar == (int)'\n') {
                isLineBreak = true;
            }
        } catch (IOException e) {
            System.out.println("IOException caught: " + e);
        }
        return builder.toString();
    }

    public String next() {
        skipWhiteSpace();
        String next = readNext(CheckerType.SEQUENCE);
        resetBuffer();
        return next;
    }

    public String nextLine() {
        skipBlankTillLineBreak();
        String line = readNext(CheckerType.LINE);
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

