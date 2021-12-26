public class TextWrapper {
    private final String text;
    private final int textSize;

    private static final char END = '\0';

    private int index = 0;

    public TextWrapper(String text) {
         this.text = text;
         this.textSize = text.length();
    }

    public boolean hasNext() {
        return index < textSize;
    }

    public char peek() {
        return safeInteraction(index);
    }

    public char next() {
        return safeInteraction(index++);
    }

    public char prev() {
        return text.charAt(index--);
    }

    public int getPosition() {
        return index;
    }

    private char safeInteraction(int index) {
        return index < textSize ? text.charAt(index) : END;
    }

}
