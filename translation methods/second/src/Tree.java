import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tree {
    public final String value;
    public final List<Tree> children = new ArrayList<>();

    public Tree(String value) {
        this.value = value;
    }

    public Tree(String value, Tree...children) {
        this(value);
        this.children.addAll(Arrays.asList(children));
    }

    public Tree(String value, String...children) {
        this(value);
        this.children.addAll(Arrays.stream(children).map(Tree::new).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        children.forEach(child -> builder.append(String.format("%s -> %s\n", value, child.value)));
        for (Tree child : children) {
            builder.append(child.toString());
        }
        return builder.toString();
    }

}
