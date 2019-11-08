package markup;
import java.util.List;

public class UnorderedList extends AbstractList implements toList  {
    public UnorderedList(List <ListItem> content) {
        super(content);
    }

    @Override
    public void toTex(StringBuilder builder) {
        toTex(builder, "\\begin{itemize}", "\\end{itemize}");
    }
}
