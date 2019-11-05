package markup;
import java.util.List;

public class UnorderedList extends AbstractList implements toList  {
    public UnorderedList(List <ListItem> content) {
        super(content);
        this.texSpecifierBegin = "\\begin{itemize}";
        this.texSpecifierEnd = "\\end{itemize}";
    }

    @Override
    public void toTex(StringBuilder builder) {
        super.toTex(builder);
    }
}
