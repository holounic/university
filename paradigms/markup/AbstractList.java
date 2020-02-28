package markup;
import java.util.List;

public abstract class AbstractList implements toList {
    private List <ListItem> textField;
    public AbstractList(List<ListItem> content) {
        this.textField = content;
    }

    protected void toTex(StringBuilder builder, String beginSpecifier, String endSpecifier) {
        builder.append(beginSpecifier);
        for (ListItem content : this.textField) {
            content.toTex(builder);
        }
        builder.append(endSpecifier);
    }

    @Override
    public void toTex(StringBuilder builder) {
        toTex(builder, "", "");
    }

}
