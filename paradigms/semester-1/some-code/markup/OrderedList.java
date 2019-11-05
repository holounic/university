package markup;
import java.util.List;

public class OrderedList extends AbstractList implements toList {
    public OrderedList(List<ListItem> content) {
        super(content);
        this.texSpecifierBegin = "\\begin{enumerate}";
        this.texSpecifierEnd = "\\end{enumerate}";
    }


}
