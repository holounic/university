package md2html;
import java.util.*;

public class Converter {
    private static final Map<String, String> markup = Map.of(
        "*", "em",
        "**", "strong",
        "++", "u",
        "_", "em",
        "__", "strong",
        "--", "s",
        "`", "code",
        "~", "mark"
    );

    private static final Map<String, String> symbols = Map.of(
        "<", "&lt;",
        ">", "&gt;",
        "&", "&amp;"
    );

    private String source;
    private int pointer;
    StringBuilder builder;

    public Converter(String source) {
        this.source = source + "\1\2";
        this.pointer = 0;
        this.builder = new StringBuilder(source.length());
    }

    private boolean closing(String opening, String sequence) {
        return opening.equals(sequence);
    }

    private boolean opening(String sequence) {
        if (Character.isWhitespace(source.charAt(pointer + sequence.length()))){
            return false;
        }
        return markup.containsKey(sequence);
    }

    private int textType() {
        int type = 0;
        while (source.charAt(type) == '#') {
            ++type;
        }
        if (!Character.isWhitespace(source.charAt(type)) || type > 6) {
            type = 0;
        }
        return type;
    }

    private void parseLink() {
        StringBuilder linkBuilder = new StringBuilder();
        parse(linkBuilder, "]", false);
        ++pointer;
        builder.append("<a href=\'");
        parse(builder, ")", true);
        builder.append("\'>");
        builder.append(linkBuilder);
        builder.append("</a>");

    }

    private void parse(StringBuilder builder, String opening, boolean ignoreMarkup) {
        int closingLength = opening.length();
        String[] sequences = new String[3];

        while (source.charAt(pointer)!= '\1') {
            sequences[1] = Character.toString(source.charAt(pointer));
            sequences[2] = source.substring(pointer, pointer + 2);

            if (closing(opening, sequences[closingLength])) {
                pointer += closingLength;
                return;
            }
            if (ignoreMarkup) {
                ++pointer;
                builder.append(sequences[1]);
                continue;
            }

            if (opening(sequences[2])) {
                pointer += 2;
                newElement(builder, markup.get(sequences[2]), sequences[2]);
                continue;
            }

            if (opening(sequences[1])) {
                pointer += 1;
                newElement(builder, markup.get(sequences[1]), sequences[1]);
                continue;
            }

            if (sequences[1].equals("\\")) {
                builder.append(source.charAt(pointer + 1));
                pointer += 2;
                continue;
            }

            if (symbols.get(sequences[1]) != null) {
                ++pointer;
                builder.append(symbols.get(sequences[1]));
                continue;
            }

            if (sequences[1].equals("[")) {
                ++pointer;
                parseLink();
                continue;
            }

            builder.append(sequences[1]);
            pointer++;
        }
    }

    private void newElement(StringBuilder builder, String tag, String sequence) {
        builder.append("<" + tag + ">");
        parse(builder, sequence, false);
        builder.append("</" + tag + ">");
    }

    public String convert() {
        int textType = textType();
        pointer = (textType == 0 ? 0 : textType + 1);

        newElement(builder, (textType == 0 ? "p" : "h" + Integer.toString(textType)), "\1");
        return builder.toString();
    }
}