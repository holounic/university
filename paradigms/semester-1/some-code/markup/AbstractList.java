package markup;
import java.util.List;

public abstract class AbstractList implements toList {
    protected List <ListItem> textField;
    protected String texSpecifierBegin = "";
    protected String texSpecifierEnd = "";
    public AbstractList(List<ListItem> content) {
        this.textField = content;
    }
    @Override
    public void toTex(StringBuilder builder) {
        builder.append(this.texSpecifierBegin);
        for (ListItem content : textField) {
           content.toTex(builder);
        }
        builder.append(this.texSpecifierEnd);
    }
}
