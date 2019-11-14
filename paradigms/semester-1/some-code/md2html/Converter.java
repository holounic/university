package md2html;
import java.util.*;

public class Converter {
    private static final Map<String, String> mdSymbols = Map.of(
            "*", "em",
            "**", "strong",
            "++", "u",
            "_", "em",
            "__", "strong",
            "--", "s",
            "`", "code",
            "~", "mark"
    );

    private static final Map<Character, String> htmlSymbols = Map.of(
            '<', "&lt;",
            '>', "&gt;",
            '&', "&amp;"
    );
    private String text;
    private int pointer;
    private StringBuilder builder;

    public Converter(String mdText) {
        this.text = mdText;
        builder = new StringBuilder(text.length());
        pointer = 0;
    }

    private int parseHead() {
        StringBuilder ignored = new StringBuilder();
        while (text.charAt(pointer) == '#' && pointer < text.length() && pointer < 7) {
            ignored.append(text.charAt(pointer++));
        }
        if (!Character.isWhitespace(text.charAt(pointer)) || pointer > 6 || pointer == 0) {
            builder.insert(0, "<p>");
            builder.append(ignored);
            return 0;
        }
        builder.append("<h" + pointer + ">");
        return ++pointer;
    }

private boolean isOpenedMDSymbols(short length) {
    if (pointer + length > text.length()) {
        return false;
    }
    if (!mdSymbols.containsKey(text.substring(pointer, pointer + length))) {
        return false;
    }
    if (pointer + length + 1 > text.length()) {
        return true;
    }
    return !(Character.isWhitespace(text.charAt(pointer + length + 1)));
}

    private boolean isClosedMDSymbols(short length) {
        if (pointer == 0 || pointer + length > text.length()) {
            return false;
        }
        if (!mdSymbols.containsKey(text.substring(pointer, pointer + length))) {
            return false;
        }
        return !Character.isWhitespace(text.charAt(pointer - 1));
    }

    private boolean alone() {
        if (pointer == 0 || pointer + 1 >= text.length()) {
            return false;
        }
        return (Character.isWhitespace(text.charAt(pointer - 1)) && Character.isWhitespace(text.charAt(pointer + 1)));
    }


    private void parseBody(String openingSequence) {
        while (pointer < text.length()) {
            char current = text.charAt(pointer);

            if (htmlSymbols.keySet().contains(current)) {
                builder.append(htmlSymbols.get(current));
                ++pointer;
                continue;
            }

            if (alone()) {
                builder.append(current);
                ++pointer;
                continue;
            }

            if (current == '\\' && mdSymbols.containsKey(
                Character.toString(text.charAt(pointer + 1)))) {
                builder.append(text.charAt(pointer + 1));
                pointer += 2;
                continue;
            }

            if (isClosedMDSymbols((short)1)) {
                String sequence = text.substring(pointer, pointer + 1);
                if (sequence.equals(openingSequence)) {
                    ++pointer;
                    return;
                }
            }

            if (isClosedMDSymbols((short)2)) {
                String sequence = text.substring(pointer, pointer + 2);
                if (sequence.equals(openingSequence)) {
                    pointer += 2;
                    return;
                }
            }

            if (isOpenedMDSymbols((short)2)) {
                String sequence = text.substring(pointer, pointer + 2);
                pointer += 2;
                builder.append("<" + mdSymbols.get(sequence) + ">");
                parseBody(sequence);
                builder.append("</" + mdSymbols.get(sequence) + ">");
                continue;
            }

            if (isOpenedMDSymbols((short)1)) {
                String sequence = text.substring(pointer, pointer + 1);
                ++pointer;
                builder.append("<" + mdSymbols.get(sequence) + ">");
                parseBody(sequence);
                builder.append("</" + mdSymbols.get(sequence) + ">");
                continue;
            }

            builder.append(current);
            ++pointer;
        }
    }

    public String convert() {
        int blockSpecifier = parseHead() - 1;
        parseBody("openingemptysequence");
        builder.append((blockSpecifier == -1 ? "</p>" : "</h" + blockSpecifier + ">"));
        return builder.toString();
    }


}
