package markup;
import java.util.List;

public class OrderedList extends AbstractList implements toList {
    public OrderedList(List<ListItem> content) {
        super(content);
    }

    @Override
    public void toTex(StringBuilder builder) {
        toTex(builder, "\\begin{enumerate}", "\\end{enumerate}");
    }

}
