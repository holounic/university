import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static int index = 0;
    static Map<String, Integer> a = new HashMap<>();
    static final String STRING_TO_PARSE = "char char_func(const int **unicorn);";
    static final String FILE_NAME = "graph.txt";

    public static void main(String[] args) throws ParseException {
        Parser parser = new Parser();
        Tree tree = parser.parse(STRING_TO_PARSE);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME)))) {
            writer.write(visualize(tree));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String visualize(Tree t) {
        StringBuilder sb = new StringBuilder("digraph {");
        dfs(t, -1, 0, sb);
        sb.append("}");
        return sb.toString();
        // dot -Tsvg graph.txt > g.svg
    }

    private static void dfs(Tree t,  int prevId, int thisId, StringBuilder sb) {
        if (!t.children.isEmpty()) {
            sb.append(String.format("\t%d [label = \"%s\"]\n", thisId, t.value));
            if (prevId != -1) {
                sb.append(String.format("\t%d -> %d\n", prevId, thisId));
            }
            for (Tree ch : t.children) {
                dfs(ch, thisId, ++index, sb);
            }
        } else {
            sb.append(String.format("\t%d [label = \"%s\"]\n", thisId, t.value));
            sb.append(String.format("\t%s -> %d\n", prevId, thisId));
            index++;
        }
    }
}
