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

    public Converter(String text) {
        this.text = text;
        pointer = 0;
    }

    private int parseHead(StringBuilder builder) {
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
        return ++pointer - 1;
    }

    private boolean isOpeningMDSymbol(short length) {
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

    private boolean isClosingMDSymbol(short length) {
        if (pointer == 0 || pointer + length > text.length()) {
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

    private void newBlock(String openingSequence, StringBuilder builder) {
        pointer += openingSequence.length();
        builder.append("<" + mdSymbols.get(openingSequence) + ">");
        parseBody(openingSequence, builder);
        builder.append("</" + mdSymbols.get(openingSequence) + ">");
    }

    private void parseBody(String openingSequence, StringBuilder builder) {
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

            if (current == '\\' && mdSymbols.containsKey(Character.toString(text.charAt(pointer + 1)))) {
                builder.append(text.charAt(pointer + 1));
                pointer += 2;
                continue;
            }

            if (isClosingMDSymbol((short)1)) {
                String sequence = text.substring(pointer, pointer + 1);
                if (sequence.equals(openingSequence)) {
                    ++pointer;
                    return;
                }
            }

            if (isClosingMDSymbol((short)2)) {
                String sequence = text.substring(pointer, pointer + 2);
                if (sequence.equals(openingSequence)) {
                    pointer += 2;
                    return;
                }
            }

            if (current == '[' && pointer + 1 < text.length()) {
                ++pointer;
                parseLink(builder);
                continue;
            }

            if (isOpeningMDSymbol((short)2)) {
                String sequence = text.substring(pointer, pointer + 2);
                newBlock(sequence, builder);
                continue;
            }

            if (isOpeningMDSymbol((short)1)) {
                String sequence = text.substring(pointer, pointer + 1);
                newBlock(sequence, builder);
                continue;
            }

            builder.append(current);
            ++pointer;
        }
    }

    public void parseLink(StringBuilder builder) {
        StringBuilder linkBuilder = new StringBuilder();

        parseBody("]", linkBuilder);

        builder.append("<a href=\'");
        ++pointer;
        char current = text.charAt(pointer);
        while (current != ')') {
            builder.append(current);
            current = text.charAt(++pointer);
        }
        ++pointer;
        builder.append("\'>");
        builder.append(linkBuilder);
        builder.append("</a>");
    }

    public String convert() {
        StringBuilder builder = new StringBuilder(text.length());
        int blockSpecifier = parseHead(builder);
        parseBody("", builder);
        builder.append((blockSpecifier == 0 ? "</p>" : "</h" + blockSpecifier + ">"));
        return builder.toString();
    }
}
