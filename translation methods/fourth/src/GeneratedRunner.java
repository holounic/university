import arithmetics.GeneratedParser;
import util.base.BaseNode;

import java.io.*;
import java.text.ParseException;

public class GeneratedRunner {
    private static final String PYTHON_TEXT = "nice or not nice";
    private static final String PYTHON_FILE_NAME = "py_graph.txt";

    private static final String ARITHMETICS_TEXT = "10+3 * 10 - 1";
    private static final String ARITHMETICS_FILE_NAME = "a_graph.txt";
    private static int index = 0;

    public static void main(String[] args) throws ParseException {
        System.out.println("===RUNNING FIRST TASK===");
        runFirst(); 
        System.out.println("===RUNNING SECOND TASK===");
        runSecond();
    }

    public static void runFirst() throws ParseException {
        arithmetics.GeneratedParser arithmeticsParser = new arithmetics.GeneratedParser(ARITHMETICS_TEXT);
        BaseNode tree = arithmeticsParser.e();
        System.out.println(((GeneratedParser.EAttributes) tree).val);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ARITHMETICS_FILE_NAME)))) {
            writer.write(visualize(tree));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runSecond() throws ParseException {
        python.GeneratedParser cppParser = new python.GeneratedParser(PYTHON_TEXT);
        BaseNode tree = cppParser.e();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PYTHON_FILE_NAME)))) {
            writer.write(visualize(tree));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String visualize(BaseNode t) {
        StringBuilder sb = new StringBuilder("digraph {");
        dfs(t, -1, 0, sb);
        sb.append("}");
        return sb.toString();
    }
    // dot -Tsvg a.txt > a.svg

    private static void dfs(BaseNode node, int pId, int curId, StringBuilder sb) {
        if (!node.children.isEmpty()) {
            sb.append(String.format("\t%d [label = \"%s\"]\n", curId, node.name));
            if (pId != -1) {
                sb.append(String.format("\t%d -> %d\n", pId, curId));
            }
            for (var child : node.children) {
                dfs(child, curId, ++index, sb);
            }
        } else {
            sb.append(String.format("\t%d [label = \"%s\"]\n", curId, node.name));
            sb.append(String.format("\t%s -> %d\n", pId, curId));
            index++;
        }
    }
}
